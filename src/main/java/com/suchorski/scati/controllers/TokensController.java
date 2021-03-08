package com.suchorski.scati.controllers;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;

import com.suchorski.scati.daos.TokenDAO;
import com.suchorski.scati.models.Token;

import lombok.Getter;
import lombok.Setter;

@Named("meusTokens")
@RequestScoped
public class TokensController {

	@Inject private SessaoController sessao;
	
	private TokenDAO tokenDAO;
	
	@Getter @Setter private Token token;
	
	@PostConstruct
	public void init() {
		tokenDAO = new TokenDAO();
	}
	
	@PreDestroy
	public void destroy() {
		tokenDAO.close();
	}
	
	public void finalizar() {
		tokenDAO.remover(token);
		sessao.atualizaUsuario();
		Messages.create("Sucesso!").detail("Token removido com sucesso.").add();
	}
	
}