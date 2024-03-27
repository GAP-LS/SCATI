package com.suchorski.scati.controllers;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;

import com.github.adminfaces.template.exception.AccessDeniedException;
import com.suchorski.scati.daos.CautelaDAO;
import com.suchorski.scati.daos.UsuarioDAO;
import com.suchorski.scati.generics.AccessNeededController;
import com.suchorski.scati.models.Cautela;
import com.suchorski.scati.models.Usuario;

import lombok.Getter;
import lombok.Setter;

@Named("receberCautelas")
@RequestScoped
public class ReceberCautelasController implements AccessNeededController, Serializable {

	private static final long serialVersionUID = -2004153197365284884L;

	@Inject private SessaoController sessao;
	
	private UsuarioDAO usuarioDAO;
	private CautelaDAO cautelaDAO;
	
	@Getter private List<Usuario> usuarios;

	@Getter @Setter private Usuario usuario;
	@Getter @Setter private Cautela cautela;
	@Getter @Setter private String descricao;
	@Getter @Setter private String assinatura;
	@Getter @Setter private List<Cautela> cautelasAbertas;
	
	@PostConstruct
	public void init() {
		usuarioDAO = new UsuarioDAO();
		usuarios = usuarioDAO.listAtivos();
		usuario = usuarios.get(0);
		cautelasAbertas = usuario.getCautelasAbertas();
		cautelaDAO = new CautelaDAO();
	}
	
	@PreDestroy
	public void destroy() {
		cautelaDAO.close();
		usuarioDAO.close();
	}
	
	public void receber() {
		cautelaDAO.receber(cautela, sessao.getUsuario());
		usuarioDAO.refresh(usuario);
		cautelasAbertas = usuario.getCautelasAbertas();
		Messages.create("Sucesso!").detail("Cautela recebida.").add();
	}

	@Override
	public void grantAccess() throws AccessDeniedException {
		checkAccess(sessao.getUsuario(), "GCR DEV");
	}
	
}