package com.suchorski.scati.controllers;

import java.io.IOException;
import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Specializes;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;

import org.omnifaces.util.Messages;

import com.github.adminfaces.template.session.AdminSession;
import com.suchorski.scati.ad.fab.LoginUnicoUsuario;
import com.suchorski.scati.ad.local.LoginLocalController;
import com.suchorski.scati.daos.PatenteDAO;
import com.suchorski.scati.daos.UsuarioDAO;
import com.suchorski.scati.exceptions.ApplicationException;
import com.suchorski.scati.models.Usuario;

import lombok.Getter;
import lombok.extern.jbosslog.JBossLog;

@Named("sessao")
@SessionScoped
@Specializes
@JBossLog
public class SessaoController extends AdminSession implements Serializable {

	private static final long serialVersionUID = -7873036463749541420L;

	@Inject private AplicacaoController app;
	@Inject private LoginLocalController loginLocal;

	private UsuarioDAO usuarioDAO;

	@Getter private Usuario usuario;

	@PostConstruct
	public void init() {
		log.info("Initializing session...");
		usuarioDAO = new UsuarioDAO();
		initData();
	}

	@PreDestroy
	public void destroy() {
		logoff();
		usuarioDAO.close();
		log.info("Finalized session");
	}

	public void logon(LoginUnicoUsuario loginUnicoUsuario) throws ApplicationException, NamingException {
		try (PatenteDAO patenteDAO = new PatenteDAO()) {
			usuario = usuarioDAO.findByCpf(loginUnicoUsuario.getCpf());
			usuario.update(loginUnicoUsuario, patenteDAO.findBySigla(loginUnicoUsuario.getPatente()));
			usuarioDAO.update(usuario);
			loginLocal.atualizar(loginUnicoUsuario);
			if (app.getOpcao().isModoManutencao() && !usuario.hasPerfil("DEV")) {
				Messages.create("Modo Manutenção!").flash().error().detail("Sistema em modo manutenção. Tente novamente mais tarde.").add();
			} else {
				if (usuario.isModerando()) {
					initData();
					throw new ApplicationException("Usuário aguardando moderação.");
				}
				if (usuario.isBloqueado()) {
					initData();
					throw new ApplicationException("Usuário bloqueado.");
				}
			}
		} catch (NoResultException e) {
			throw new ApplicationException("Usuário não cadastrado.");
		}
	}

	public void logoff() {
		initData();
	}

	public void idle() throws IOException {
		initData();
		Messages.create("Sessão expirada!").flash().error().detail("Você precisa realizar o login novamente.").add();
		FacesContext.getCurrentInstance().getExternalContext().redirect("expirado.xhtml");
	}

	public Usuario getUsuarioAtualizado() {
		atualizaUsuario();
		return usuario;
	}

	public void atualizaUsuario() {
		try {
			if (usuario != null) {
				usuarioDAO.refresh(usuario);
			}
		} catch (EntityNotFoundException e) {
			usuarioDAO.detach(usuario);
			usuario = usuarioDAO.getById(usuario.getId());
		}
	}
	
	private void initData() {
		usuario = null;
	}

	@Override
	public boolean isLoggedIn() {
		if (app.getOpcao().isModoManutencao() && usuario != null && !usuario.hasPerfil("DEV")) {
			return false;
		}
		return usuario != null;
	}

}
