package com.suchorski.scati.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;

import com.suchorski.scati.others.Info;

import lombok.Getter;

@Named("v2")
@RequestScoped
public class InformativoAPIV2Controller {
	
	@Getter private List<Info> requestCodeInput;
	@Getter private List<Info> requestTokenInput;
	@Getter private List<Info> requestTokenOutput;
	@Getter private List<Info> requestUserinfoInput;
	@Getter private List<Info> requestUserinfoInput2;
	@Getter private List<Info> requestUserinfoOutput;
	@Getter private List<Info> requestLogoff;
	
	@PostConstruct
	public void init() {
		requestCodeInput = new ArrayList<Info>();
		requestCodeInput.add(new Info("response_type", "Tipo de resposta. Sempre utilize 'code'", "URI"));
		requestCodeInput.add(new Info("client_id", "ID do cliente da aplicação", "URI"));
		requestCodeInput.add(new Info("redirect_uri", "URL para redirecionamento", "URI"));
		requestCodeInput.add(new Info("scope", "Escopo para verificação de perfil. Utilize 'none' para que a verificação não seja feita", "URI"));
		requestCodeInput.add(new Info("state", "Valor aleatório para controle do cliente", "URI"));

		requestTokenInput = new ArrayList<Info>();
		requestTokenInput.add(new Info("grant_type", "Tipo de autorização. Sempre utilize 'authorization_code'", "FORM"));
		requestTokenInput.add(new Info("code", "Código retornado pelo OAUTH", "FORM"));
		requestTokenInput.add(new Info("redirect_uri", "URL para redirecionamento", "FORM"));
		requestTokenInput.add(new Info("client_id", "Chave pública do cliente", "FORM"));
		requestTokenInput.add(new Info("client_secret", "Chave secreta do cliente", "FORM"));

		requestTokenOutput = new ArrayList<Info>();
		requestTokenOutput.add(new Info("access_token", "Token de acesso"));
		requestTokenOutput.add(new Info("expires_in", "Data de expiração"));
		
		requestUserinfoInput = new ArrayList<>();
		requestUserinfoInput.add(new Info("secret_key", "Cave secreta da aplicação", "URI"));
		requestUserinfoInput.add(new Info("Bearer 'token'", "Token do cliente", "Header: Authorization"));

		requestUserinfoInput2 = new ArrayList<>();
		requestUserinfoInput2.add(new Info("secret_key", "Cave secreta da aplicação", "URI"));
		requestUserinfoInput2.add(new Info("token", "Token do cliente", "URI"));
		
		requestUserinfoOutput = new ArrayList<Info>();
		requestUserinfoOutput.add(new Info("valid", "True/False para o status"));
		requestUserinfoOutput.add(new Info("cpf", "CPF do militar"));
		requestUserinfoOutput.add(new Info("saram", "SARAM do militar"));
		requestUserinfoOutput.add(new Info("nomeCompleto", "Nome completo do militar"));
		requestUserinfoOutput.add(new Info("nomeGuerra", "Nome de guerra do militar"));
		requestUserinfoOutput.add(new Info("patente", "Patente do militar"));
		requestUserinfoOutput.add(new Info("om", "OM do militar"));
		requestUserinfoOutput.add(new Info("zimbra", "E-mail ZIMBRA do militar"));
		requestUserinfoOutput.add(new Info("perfis", "Perfis do militar"));

		requestLogoff = new ArrayList<>();
		requestLogoff.add(new Info("token", "Token do cliente", "URI"));
	}

}
