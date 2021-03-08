package com.suchorski.scati.controllers;

import java.util.List;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;

import com.github.adminfaces.template.exception.AccessDeniedException;
import com.suchorski.scati.generics.AccessNeededController;

@Named("configuracoes")
@RequestScoped
public class ConfiguracoesController implements AccessNeededController {

	@Inject private AplicacaoController app;
	@Inject private SessaoController sessao;

	private List<String> oms;
	
	@PostConstruct
	public void init() {
		oms = app.getOpcao().getListOmsApoiadas();
	}
	
	public void salvar() {
		app.getOpcao().setListOmsApoiadas(oms.stream().map(String::toUpperCase).sorted().collect(Collectors.toList()));
		app.salvarOpcao();
		Messages.create("Sucesso!").detail("Configurações salvas.").add();
	}

	@Override
	public void grantAccess() throws AccessDeniedException {
		checkAccess(sessao.getUsuario(), "ASC DEV");
	}
	
}