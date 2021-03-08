package com.suchorski.scati.controllers;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.github.adminfaces.template.exception.AccessDeniedException;
import com.suchorski.scati.daos.UsuarioDAO;
import com.suchorski.scati.generics.AccessNeededController;
import com.suchorski.scati.models.Usuario;

import lombok.Getter;
import lombok.Setter;

@Named("consultarCautelas")
@RequestScoped
public class ConsultarCautelasController implements AccessNeededController {
	
	@Inject SessaoController sessao;
	
	private UsuarioDAO usuarioDAO;
	
	@Getter private List<Usuario> usuarios;
	
	@Getter @Setter private Usuario usuario;
	
	@PostConstruct
	public void init() {
		usuarioDAO = new UsuarioDAO();
		usuarios = usuarioDAO.listAtivos();
		usuario = usuarios.get(0);
	}
	
	@PreDestroy
	public void destroy() {
		usuarioDAO.close();
	}
	
	@Override
	public void grantAccess() throws AccessDeniedException {
		checkAccess(sessao.getUsuario(), "CUC DEV");
	}
	
}