package com.suchorski.scati.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;
import org.primefaces.model.DualListModel;

import com.github.adminfaces.template.exception.AccessDeniedException;
import com.suchorski.scati.daos.UsuarioDAO;
import com.suchorski.scati.generics.AccessNeededController;
import com.suchorski.scati.models.Aplicacao;
import com.suchorski.scati.models.Usuario;

import lombok.Getter;
import lombok.Setter;

@Named("gerenciarAplicacoes")
@RequestScoped
public class GerenciarAplicacoesController implements AccessNeededController {

	@Inject private SessaoController sessao;
	
	private UsuarioDAO usuarioDAO;

	@Getter private List<Usuario> usuariosAtivos;
	
	@Getter @Setter private Usuario usuario;
	@Getter @Setter private DualListModel<Aplicacao> aplicacaos;
	@Getter @Setter private Aplicacao aplicacao;
	
	@PostConstruct
	public void init() {
		usuarioDAO = new UsuarioDAO();
		usuariosAtivos = usuarioDAO.listAtivos();
		usuario = usuariosAtivos.get(0);
		mudarOpcoes();
	}
	
	@PreDestroy
	public void destroy() {
		usuarioDAO.close();
	}
	
	public void mudarOpcoes() {
		List<Aplicacao> source = new ArrayList<Aplicacao>();
		List<Aplicacao> target = new ArrayList<Aplicacao>();
		List<Aplicacao> list = new ArrayList<Aplicacao>(sessao.getUsuario().getAplicacaos());
		list.forEach(p -> {
			if (usuario.getAplicacaosGerenciadas().contains(p)) {
				target.add(p);
			} else {
				source.add(p);
			}
		});
		aplicacaos = new DualListModel<Aplicacao>(source, target);
	}
	
	public void salvar() {
		usuarioDAO.updateAplicacaosGerenciadas(usuario, aplicacaos.getSource(), aplicacaos.getTarget());
		sessao.atualizaUsuario();
		Messages.create("Sucesso!").detail("Aplicações atualizadas!").add();
	}

	@Override
	public void grantAccess() throws AccessDeniedException {
		if (sessao.getUsuario().getAplicacaos().isEmpty()) {
			throw new AccessDeniedException();
		}
	}
	
}