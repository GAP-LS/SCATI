package com.suchorski.scati.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.suchorski.scati.others.Info;

import lombok.Getter;

@Named("v1")
@RequestScoped
public class InformativoAPIV1Controller {
	
	@Getter private List<Info> requestDataInput;
	@Getter private List<Info> requestDataOutput;
	
	@PostConstruct
	public void init() {
		requestDataInput = new ArrayList<Info>();
		requestDataInput.add(new Info("login", "CPF do militar"));
		requestDataInput.add(new Info("senha", "Senha do Login Único"));
		
		requestDataOutput = new ArrayList<Info>();
		requestDataOutput.add(new Info("valid", "True/False para o status"));
		requestDataOutput.add(new Info("message", "Mensagem informativa"));
		requestDataOutput.add(new Info("cpf", "CPF do militar"));
		requestDataOutput.add(new Info("saram", "SARAM do militar"));
		requestDataOutput.add(new Info("nomeCompleto", "Nome completo do militar"));
		requestDataOutput.add(new Info("nomeGuerra", "Nome de guerra do militar"));
		requestDataOutput.add(new Info("patente", "Patente do militar"));
		requestDataOutput.add(new Info("om", "OM do militar"));
		requestDataOutput.add(new Info("zimbra", "E-mail ZIMBRA do militar"));
		requestDataOutput.add(new Info("perfis", "Perfis do militar"));
	}

}
