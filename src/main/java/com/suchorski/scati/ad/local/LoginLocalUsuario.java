package com.suchorski.scati.ad.local;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of={"cpf", "saram"})
public class LoginLocalUsuario {
	
	private String nome; 				// {nameParam}
	private String nomeCompleto; 		// sn
	private String nomeGuerra; 			// givenName
	private String patente; 			// initials
	private String nomeExibicao; 		// displayName
	private String login; 				// {loginParam}
	private String loginMail; 			// {loginMailParam}
	private String email; 				// mail
	private String cpf; 				// {cpfParam}
	private String saram; 				// {saramParam}
	private String pin; 				// {pinParam}
	
	public LoginLocalUsuario(Attributes attrs, String name, String login, String loginMail, String cpf, String saram, String pin) throws NamingException {
		this.nome = attrs.get(name).get().toString();
		this.nomeCompleto = attrs.get("sn").get().toString();
		this.nomeGuerra = attrs.get("givenName").get().toString();
		this.patente = attrs.get("initials").get().toString();
		this.nomeExibicao = attrs.get("displayName").get().toString();
		this.login = attrs.get(login).get().toString();
		this.loginMail = attrs.get(loginMail).get().toString();
		this.email = attrs.get("mail").get().toString();
		this.cpf = attrs.get(cpf).get().toString();
		this.saram = attrs.get(saram).get().toString();
		this.pin = attrs.get(pin).get().toString();
	}

	public LoginLocalUsuario(Attributes attrs, String name, String login, String loginMail) throws NamingException {
		this.nome = attrs.get(name).get().toString();
		this.nomeCompleto = attrs.get("sn").get().toString();
		this.nomeGuerra = attrs.get("givenName").get().toString();
		this.patente = attrs.get("initials").get().toString();
		this.nomeExibicao = attrs.get("displayName").get().toString();
		this.login = attrs.get(login).get().toString();
		this.loginMail = attrs.get(loginMail).get().toString();
	}
	
	public String getCn() {
		return getNome();
	}
	
	public static String[] getReturningAttributes(String name, String login, String loginMail, String cpf, String saram, String pin) {
		return new String[] {
				name,     	 	"cn",    		"sn", 		"givenName",
				"initials",    	"displayName", 	login, 		loginMail,
				"mail", 		cpf,   			saram, 		pin
		};
	}
}
