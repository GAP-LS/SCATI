package com.suchorski.scati.controllers;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;

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

import lombok.Setter;

@Named("moderar")
@ViewScoped
public class ModerarController implements AccessNeededController, Serializable {
	
	private static final long serialVersionUID = -8220747562487981573L;

	@Inject	private SessaoController sessao;
	@Inject private LoginUnicoController loginUnico;
	@Inject private LoginLocalController loginLocal;
	
	private PatenteDAO patenteDAO;
	private UsuarioDAO usuarioDAO;
	
	@Setter private Usuario moderado;
	
	@PostConstruct
	public void init() {
		patenteDAO = new PatenteDAO();
		usuarioDAO = new UsuarioDAO();
	}
	
	@PreDestroy
	public void destroy() {
		usuarioDAO.close();
		patenteDAO.close();
	}
	
	public void moderar() throws NamingException, AddressException, UnsupportedEncodingException, MessagingException {
		try {
			LoginUnicoUsuario loginUnicoUsuario = loginUnico.findByCpfOrSaram(moderado.getCpf());
			loginLocal.criar(loginUnicoUsuario);
			usuarioDAO.liberar(moderado, sessao.getUsuario());
			sessao.atualizaUsuario();
			Messages.create("Sucesso!").detail("Usuário moderado com sucesso.").add();
		} catch (ApplicationException e) {
			Messages.create("Aviso!").warn().detail(e.getLocalizedMessage()).add();
		}
	}

	@Override
	public void grantAccess() throws AccessDeniedException {
		if (sessao.getUsuario().isPodeModerar()) {
			return;
		}
		checkAccess(sessao.getUsuario(), "DEV");
	}
	
}
