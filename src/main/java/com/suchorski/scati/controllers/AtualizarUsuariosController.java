package com.suchorski.scati.controllers;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.persistence.NoResultException;

import org.omnifaces.util.Messages;

import com.github.adminfaces.template.exception.AccessDeniedException;
import com.suchorski.scati.ad.fab.LoginUnicoController;
import com.suchorski.scati.ad.fab.LoginUnicoUsuario;
import com.suchorski.scati.ad.local.LoginLocalController;
import com.suchorski.scati.daos.PatenteDAO;
import com.suchorski.scati.daos.UsuarioDAO;
import com.suchorski.scati.exceptions.ApplicationException;
import com.suchorski.scati.generics.AccessNeededController;
import com.suchorski.scati.models.Usuario;

import lombok.Getter;
import lombok.Setter;

@Named("atualizarUsuarios")
@ViewScoped
public class AtualizarUsuariosController implements AccessNeededController, Serializable {
	
	private static final long serialVersionUID = 1643046939885980828L;

	@Inject	private AplicacaoController app;
	@Inject	private SessaoController sessao;
	@Inject private LoginUnicoController loginUnico;
	@Inject private LoginLocalController loginLocal;
	
	private UsuarioDAO usuarioDAO;
	
	@Getter private List<Usuario> usuarios;
	
	@Setter private Usuario atualizado;
	
	@PostConstruct
	public void init() {
		usuarioDAO = new UsuarioDAO();
		usuarios = usuarioDAO.listNaoFinalizados(sessao.getUsuario(), false);
	}
	
	@PreDestroy
	public void destroy() {
		usuarioDAO.close();
	}
	
	public void atualizar() throws NamingException, ApplicationException {
		try (PatenteDAO patenteDAO = new PatenteDAO()) {
			LoginUnicoUsuario loginUnicoUsuario = loginUnico.findByCpfOrSaram(atualizado.getCpf());
			atualizado.update(loginUnicoUsuario, patenteDAO.findBySigla(loginUnicoUsuario.getPatente()));
			usuarioDAO.atualizar(atualizado, LoginLocalController.isVisitante(app.getOpcao().getListOmsApoiadas(), loginUnicoUsuario));
			loginLocal.atualizar(loginUnicoUsuario);
			usuarios = usuarioDAO.listNaoFinalizados(sessao.getUsuario(), false);
			Messages.create("Sucesso!").detail("Usuário atualizado com sucesso.").add();
		} catch (NoResultException e) {
			throw new ApplicationException("Usuário não cadastrado.");
		}
	}
	
	public void atualizarTodos() throws NamingException, ApplicationException {
		try (PatenteDAO patenteDAO = new PatenteDAO()) {
			for (Usuario v : usuarios) {
				LoginUnicoUsuario loginUnicoUsuario = loginUnico.findByCpfOrSaram(v.getCpf());
				v.update(loginUnicoUsuario, patenteDAO.findBySigla(loginUnicoUsuario.getPatente()));
				usuarioDAO.atualizar(v, LoginLocalController.isVisitante(app.getOpcao().getListOmsApoiadas(), loginUnicoUsuario));
				loginLocal.atualizar(loginUnicoUsuario);
			}
			Messages.create("Sucesso!").detail("Usuários atualizados com sucesso.").add();
		} catch (NoResultException e) {
			throw new ApplicationException("Erro ao atualizar os usuários.");
		}
	}

	@Override
	public void grantAccess() throws AccessDeniedException {
		checkAccess(sessao.getUsuario(), "DEV");
	}
	
}
