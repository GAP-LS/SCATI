package com.suchorski.scati.controllers;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;

import org.omnifaces.util.Messages;

import com.github.adminfaces.template.exception.AccessDeniedException;
import com.suchorski.scati.ad.fab.LoginUnicoController;
import com.suchorski.scati.daos.CautelaDAO;
import com.suchorski.scati.daos.UsuarioDAO;
import com.suchorski.scati.exceptions.ApplicationException;
import com.suchorski.scati.generics.AccessNeededController;
import com.suchorski.scati.models.Cautela;
import com.suchorski.scati.models.Usuario;

import lombok.Getter;
import lombok.Setter;

@Named("pagarCautelas")
@RequestScoped
public class PagarCautelasController implements AccessNeededController, Serializable {

	private static final long serialVersionUID = 1877653467648891587L;

	@Inject private SessaoController sessao;
	@Inject private LoginUnicoController loginUnico;
	
	private UsuarioDAO usuarioDAO;
	private CautelaDAO cautelaDAO;
	
	@Getter private List<Usuario> usuarios;

	@Getter @Setter private Usuario usuario;
	@Getter @Setter private Cautela cautela;
	@Getter @Setter private String descricao;
	@Getter @Setter private String assinatura;
	
	@PostConstruct
	public void init() {
		usuarioDAO = new UsuarioDAO();
		usuarios = usuarioDAO.listAtivos();
		usuario = usuarios.get(0);
		cautelaDAO = new CautelaDAO();
	}
	
	@PreDestroy
	public void destroy() {
		cautelaDAO.close();
		usuarioDAO.close();
	}
	
	public void assinar() throws NamingException {
		try {
			loginUnico.login(usuario.getCpf(), assinatura);
			cautelaDAO.registrar(descricao, usuario, sessao.getUsuario());
			Messages.create("Sucesso!").detail("Cautela registrada.").add();
		} catch (ApplicationException e) {
			Messages.create("Aviso!").error().detail("Assinatura incorreta.").add();
		}
		descricao = null;
		assinatura = null;
	}
	
	@Override
	public void grantAccess() throws AccessDeniedException {
		checkAccess(sessao.getUsuario(), "GCP DEV");
	}
	
}