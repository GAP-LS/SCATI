package com.suchorski.scati.controllers;

import java.io.IOException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;

import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;

import com.suchorski.scati.ad.fab.LoginUnicoController;
import com.suchorski.scati.ad.fab.LoginUnicoUsuario;
import com.suchorski.scati.daos.UsuarioDAO;
import com.suchorski.scati.exceptions.ApplicationException;

import lombok.Getter;
import lombok.Setter;

@Named("login")
@RequestScoped
public class LoginController {
	
	@Inject private LoginUnicoController loginUnico;
	@Inject private SessaoController sessao;
	
	private UsuarioDAO usuarioDAO;
	
	@Getter @Setter private String username;
	@Getter @Setter private String password;
	@Getter @Setter private boolean lembrar;
	
	@PostConstruct
	public void init() {
		usuarioDAO = new UsuarioDAO();
	}
	
	@PreDestroy
	public void destroy() {
		usuarioDAO.close();
	}
	
	public String logon() throws NamingException, IOException {
		try {
			LoginUnicoUsuario loginUnicoUsuario = loginUnico.findUsuario(username, password);
			sessao.logon(loginUnicoUsuario);
			String redir = Faces.getRequestParameter("redir");
			if (redir != null) {
				Faces.redirect(redir);
				return "";
			}
			return "index?faces-redirect=true";
		} catch (ApplicationException e) {
			Messages.create("Erro!").flash().warn().detail(e.getLocalizedMessage()).add();
		}
		return "login?faces-redirect=true";
	}
	
	public String logoff() {
		sessao.logoff();
		return "login?faces-redirect=true";
	}
	
}
