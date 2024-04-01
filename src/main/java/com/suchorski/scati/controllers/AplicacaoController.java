package com.suchorski.scati.controllers;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import org.omnifaces.util.Messages;

import com.suchorski.scati.daos.OpcaoDAO;
import com.suchorski.scati.models.Opcao;
import com.suchorski.scati.services.HibernateService;

import lombok.Getter;

@Named("app")
@ApplicationScoped
public class AplicacaoController {

	public static final String CONTEXT = "scati";
	public static final String NAME = "SCATI";
	public static final String TITLE = "Sistema de Controle de Acesso de TI";
	public static final String VERSION = "1.0.3";
	public static final long OPCAO_ID = 1;
	public static final String OM = "gapls";
	public static final boolean ENABLED_FROM_AD = false;

	private OpcaoDAO opcaoDAO;

	@Getter private Opcao opcao;

	@PostConstruct
	public void init() {
		HibernateService.startup();
		opcaoDAO = new OpcaoDAO();
		opcao = opcaoDAO.getById(OPCAO_ID);
	}

	@PreDestroy
	public void destroy() {
		opcaoDAO.close();
		HibernateService.shutdown();
	}

	public void modoManutencao() {
		opcao.setModoManutencao(!opcao.isModoManutencao());
		opcaoDAO.update(opcao);
		if (!opcao.isModoManutencao()) {
			Messages.create("MODO MANUTENÇÃO!").detail("Desativado.").add();			
		} else {
			Messages.create("MODO MANUTENÇÃO!").error().detail("Ativado.").add();			
		}
	}

	public void salvarOpcao() {
		opcaoDAO.save(opcao);
	}

	public String getContext() {
		return CONTEXT;
	}

	public String getName() {
		return NAME;
	}

	public String getTitle() {
		return TITLE;
	}

	public String getVersion() {
		return VERSION;
	}

	public long getOpcaoId() {
		return OPCAO_ID;
	}
	
	public String getOm() {
		return OM;
	}
	
	public String getOmPng() {
		return String.format("%s.png", OM);
	}
	
	public boolean getEnabledFromAd() {
		return ENABLED_FROM_AD;
	}

}
