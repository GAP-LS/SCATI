package com.suchorski.scati.controllers;

import java.io.UnsupportedEncodingException;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;

import org.omnifaces.util.Messages;

import com.suchorski.scati.ad.fab.LoginUnicoController;
import com.suchorski.scati.ad.fab.LoginUnicoUsuario;
import com.suchorski.scati.ad.local.LoginLocalController;
import com.suchorski.scati.ad.local.LoginLocalUsuario;
import com.suchorski.scati.exceptions.ApplicationException;

import lombok.Getter;

@Named("index")
@RequestScoped
public class IndexController {
	
	@Inject SessaoController sessao;
	@Inject LoginUnicoController loginUnico;
	@Inject LoginLocalController loginLocal;
	
	@Getter LoginUnicoUsuario loginUnicoUsuario;
	@Getter LoginLocalUsuario loginLocalUsuario;
	
	@PostConstruct
	public void init() {
		try {
			if (sessao.getUsuario() != null) {
				loginUnicoUsuario = loginUnico.findByCpfOrSaram(sessao.getUsuario().getCpf());
				loginLocalUsuario = loginLocal.findByCpfOrSaram(sessao.getUsuario().getCpf());
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public void gerarSenha() throws NamingException, AddressException, UnsupportedEncodingException, MessagingException {
		loginLocal.gerarSenha(sessao.getUsuario());
		Messages.create("Sucesso!").detail("Senha resetada e enviada por ZIMBRA.").add();
	}
	
	public void gerarPin() throws NamingException, ApplicationException, AddressException, UnsupportedEncodingException, MessagingException {
		loginLocal.gerarPin(sessao.getUsuario());
		loginLocalUsuario = loginLocal.findByCpfOrSaram(sessao.getUsuario().getCpf());
		Messages.create("Sucesso!").detail("PIN gerado e enviado por ZIMBRA.").add();
	}
	
}
