package com.suchorski.scati.controllers;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;

import org.omnifaces.util.Messages;

import com.github.adminfaces.template.exception.AccessDeniedException;
import com.suchorski.scati.ad.fab.LoginUnicoController;
import com.suchorski.scati.ad.fab.LoginUnicoUsuario;
import com.suchorski.scati.ad.local.LoginLocalController;
import com.suchorski.scati.ad.local.LoginLocalUsuario;
import com.suchorski.scati.daos.PatenteDAO;
import com.suchorski.scati.daos.UsuarioDAO;
import com.suchorski.scati.generics.AccessNeededController;
import com.suchorski.scati.models.Patente;
import com.suchorski.scati.models.Usuario;

import lombok.Getter;
import lombok.Setter;

@Named("migrar")
@ViewScoped
public class MigrarController implements AccessNeededController, Serializable {

	private static final long serialVersionUID = 4356483759517630804L;

	@Inject private AplicacaoController app;
	@Inject	private SessaoController sessao;
	@Inject private LoginUnicoController loginUnico;
	@Inject private LoginLocalController loginLocal;

	private PatenteDAO patenteDAO;
	private UsuarioDAO usuarioDAO;

	@Getter private List<LoginLocalUsuario> usuarios;
	
	@Setter private LoginLocalUsuario usuario;

	@PostConstruct
	public void init() {
		try {
			patenteDAO = new PatenteDAO();
			usuarioDAO = new UsuarioDAO();
			usuarios = loginLocal.listarComCpf();
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	@PreDestroy
	public void destroy() {
		usuarioDAO.close();
		patenteDAO.close();
	}
	
	public void migrar() {
		try {
			LoginUnicoUsuario loginUnicoUsuario = loginUnico.findByCpfOrSaram(usuario.getCpf());
			Patente patente = patenteDAO.findBySigla(usuario.getPatente());
			Usuario u = new Usuario(loginUnicoUsuario, patente, null);
			loginLocal.atualizar(loginUnicoUsuario);
			u.setDataInsercao(new Date());
			u.setVisitante(LoginLocalController.isVisitante(app.getOpcao().getListOmsApoiadas(), loginUnicoUsuario));
			UsuarioDAO gambiarra = new UsuarioDAO(); // GAMBIARRA
			gambiarra.save(u); // GAMBIARRA
			gambiarra.close(); // GAMBIARRA
			usuarios = loginLocal.listarComCpf();
			Messages.create("Sucesso!").flash().detail("Usuário migrado com sucesso.").add();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void grantAccess() throws AccessDeniedException {
		checkAccess(sessao.getUsuario(), "DEV");
	}

}
