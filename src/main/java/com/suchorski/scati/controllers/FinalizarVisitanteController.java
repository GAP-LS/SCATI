package com.suchorski.scati.controllers;

import java.io.Serializable;
import java.util.List;

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

import lombok.Getter;
import lombok.Setter;

@Named("finalizarVisitantes")
@ViewScoped
public class FinalizarVisitanteController implements AccessNeededController, Serializable {
	
	private static final long serialVersionUID = -6093738777542916262L;

	@Inject	private SessaoController sessao;
	@Inject private LoginLocalController loginLocal;
	
	private UsuarioDAO usuarioDAO;
	
	@Getter private List<Usuario> visitantes;
	
	@Setter private Usuario finalizado;
	
	@PostConstruct
	public void init() {
		usuarioDAO = new UsuarioDAO();
		visitantes = usuarioDAO.listNaoFinalizados(sessao.getUsuario(), true);
	}
	
	@PreDestroy
	public void destroy() {
		usuarioDAO.close();
	}
	
	public void finalizar() throws NamingException {
		if (!finalizado.getCautelasAbertas().isEmpty()) {
			Messages.create("Erro!").error().detail("Usuário possui cautelas pendentes.").add();
		} else if (!finalizado.getVisitantes().isEmpty()) {
			Messages.create("Erro!").error().detail("Usuário possui visitantes pendentes.").add();
		} else {  
			loginLocal.remover(finalizado);
			usuarioDAO.finalizar(finalizado, sessao.getUsuario());
			visitantes = usuarioDAO.listNaoFinalizados(sessao.getUsuario(), true);
			sessao.atualizaUsuario();
			Messages.create("Sucesso!").detail("Visitante finalizado com sucesso.").add();
		}
	}

	@Override
	public void grantAccess() throws AccessDeniedException {
		checkAccess(sessao.getUsuario(), "DEV");
	}
	
}
