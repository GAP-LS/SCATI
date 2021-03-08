package com.suchorski.scati.controllers;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;

import org.omnifaces.util.Messages;

import com.github.adminfaces.template.exception.AccessDeniedException;
import com.suchorski.scati.ad.local.LoginLocalController;
import com.suchorski.scati.daos.UsuarioDAO;
import com.suchorski.scati.generics.AccessNeededController;
import com.suchorski.scati.models.Usuario;

import lombok.Setter;

@Named("visitantes")
@ViewScoped
public class VisitantesController implements AccessNeededController, Serializable {

	private static final long serialVersionUID = -6029015728602389221L;

	@Inject	private SessaoController sessao;
	@Inject private LoginLocalController loginLocal;
	
	private UsuarioDAO usuarioDAO;
	
	@Setter private Usuario visitante;
	
	@PostConstruct
	public void init() {
		usuarioDAO = new UsuarioDAO();
	}
	
	@PreDestroy
	public void destroy() {
		usuarioDAO.close();
	}
	
	public void finalizar() throws NamingException {
		loginLocal.remover(visitante);
		usuarioDAO.finalizar(visitante, sessao.getUsuario());
		Messages.create("Sucesso!").detail("Visitante finalizado com sucesso.").add();
	}

	@Override
	public void grantAccess() throws AccessDeniedException {
		if (!sessao.getUsuario().isPodeModerar()) {
			throw new AccessDeniedException();
		}
		checkAccess(sessao.getUsuario(), "DEV");
	}
	
}
