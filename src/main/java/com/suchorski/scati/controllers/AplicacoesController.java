package com.suchorski.scati.controllers;

import java.io.Serializable;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.PersistenceException;

import org.omnifaces.util.Messages;

import com.github.adminfaces.template.exception.AccessDeniedException;
import com.suchorski.scati.daos.AplicacaoDAO;
import com.suchorski.scati.daos.PerfilDAO;
import com.suchorski.scati.generics.AccessNeededController;
import com.suchorski.scati.models.Aplicacao;
import com.suchorski.scati.models.Perfil;

import lombok.Getter;
import lombok.Setter;

@Named("aplicacoes")
@ViewScoped
public class AplicacoesController implements AccessNeededController, Serializable {
	
	private static final long serialVersionUID = -1973420138817476149L;

	@Inject SessaoController sessao;
	
	private AplicacaoDAO aplicacaoDAO;
	private PerfilDAO perfilDAO;
	
	@Getter @Setter private String nome;
	@Getter @Setter private String sigla;
	@Getter @Setter private Aplicacao aplicacao;
	
	@Getter @Setter private String codigo;
	@Getter @Setter private String titulo;
	@Getter @Setter private String descricao;
	@Getter @Setter private Perfil perfil;
	
	@PostConstruct
	public void init() {
		aplicacaoDAO = new AplicacaoDAO();
		perfilDAO = new PerfilDAO();
	}
	
	@PreDestroy
	public void destroy() {
		perfilDAO.close();
		aplicacaoDAO.close();
	}
	
	public void adicionarAplicacao() {
		try {
			aplicacaoDAO.adicionar(nome, sigla, sessao.getUsuario());
			sessao.atualizaUsuario();
			nome = sigla = "";
			Messages.create("Sucesso!").detail("Aplicação adicionada.").add();
		} catch (PersistenceException e) {
			Messages.create("Erro!").error().detail("Aplicação existente.").add();
		}
	}
	
	public void excluirAplicacao() {
		aplicacaoDAO.remover(aplicacao);
		sessao.atualizaUsuario();
		Messages.create("Sucesso!").detail("Aplicação removida.").add();
	}
	
	public void adicionarPerfil() {
		try {
			perfilDAO.adicionar(codigo, titulo, descricao, aplicacao);
			sessao.atualizaUsuario();
			codigo = titulo = descricao = "";
			Messages.create("Sucesso!").detail("Perfil adicionado.").add();
		} catch (PersistenceException e) {
			Messages.create("Erro!").error().detail("Código de perfil existente.").add();
		}
	}
	
	public void removerPerfil() {
		perfilDAO.remover(perfil);
		sessao.atualizaUsuario();
		Messages.create("Sucesso!").detail("Perfil removido.").add();
	}
	
	@Override
	public void grantAccess() throws AccessDeniedException {
		checkAccess(sessao.getUsuario(), "GAP DEV");
	}
	
}