package com.suchorski.scati.controllers;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;

import org.omnifaces.util.Messages;

import com.github.adminfaces.template.exception.AccessDeniedException;
import com.suchorski.scati.ad.local.LoginLocalController;
import com.suchorski.scati.daos.UsuarioDAO;
import com.suchorski.scati.exceptions.ApplicationException;
import com.suchorski.scati.generics.AccessNeededController;
import com.suchorski.scati.models.Usuario;

import lombok.Getter;
import lombok.Setter;

@Named("pin")
@RequestScoped
public class PINController implements AccessNeededController {

	@Inject SessaoController sessao;
	@Inject LoginLocalController loginLocal;

	private UsuarioDAO usuarioDAO;

	@Getter private List<Usuario> usuarios;

	@Getter @Setter private Usuario usuario;
	@Getter @Setter private String pin;

	@PostConstruct
	public void init() {
		usuarioDAO = new UsuarioDAO();
		usuarios = usuarioDAO.listAtivos();
		usuario = usuarios.get(0);
	}

	@PreDestroy
	public void destroy() {
		usuarioDAO.close();
	}

	public void gerarPin() throws NamingException, AddressException, UnsupportedEncodingException, MessagingException {
		loginLocal.gerarPin(usuario);
		Messages.create("Sucesso!").detail("PIN gerado com sucesso.").add();
	}

	public void definirPin() throws NamingException, AddressException, UnsupportedEncodingException, MessagingException {
		try {
			if (!pin.isEmpty()) {
				loginLocal.definePin(usuario, pin);
				Messages.create("Sucesso!").detail("PIN definido com sucesso.").add();
			} else {
				Messages.create("Aviso!").warn().detail("PIN não pode ser em branco.").add();			
			}
		} catch (ApplicationException e) {
			Messages.create("Erro!").error().detail(e.getLocalizedMessage()).add();
		}
	}

	@Override
	public void grantAccess() throws AccessDeniedException {
		checkAccess(sessao.getUsuario(), "GPI DEV");
	}

}