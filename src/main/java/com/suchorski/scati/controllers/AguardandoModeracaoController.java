package com.suchorski.scati.controllers;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;

import org.omnifaces.util.Messages;

import com.github.adminfaces.template.exception.AccessDeniedException;
import com.suchorski.scati.ad.fab.LoginUnicoController;
import com.suchorski.scati.ad.fab.LoginUnicoUsuario;
import com.suchorski.scati.ad.local.LoginLocalController;
import com.suchorski.scati.daos.UsuarioDAO;
import com.suchorski.scati.exceptions.ApplicationException;
import com.suchorski.scati.generics.AccessNeededController;
import com.suchorski.scati.models.Usuario;

import lombok.Getter;
import lombok.Setter;

@Named("aguardandoModeracao")
@ViewScoped
public class AguardandoModeracaoController implements AccessNeededController, Serializable {
	
	private static final long serialVersionUID = -2873135968444099130L;

	@Inject	private SessaoController sessao;
	@Inject private LoginUnicoController loginUnico;
	@Inject private LoginLocalController loginLocal;
	
	private UsuarioDAO usuarioDAO;
	
	@Getter private List<Usuario> aguardandoModeracao;
	
	@Setter private Usuario usuarioForcado;
	
	@PostConstruct
	public void init() {
		usuarioDAO = new UsuarioDAO();
		aguardandoModeracao = usuarioDAO.listNaoModerados();
	}
	
	@PreDestroy
	public void destroy() {
		usuarioDAO.close();
	}
	
	public void forcarModeracao() throws NamingException, AddressException, UnsupportedEncodingException, MessagingException {
		try {
			LoginUnicoUsuario loginUnicoUsuario = loginUnico.findByCpfOrSaram(usuarioForcado.getCpf());
			loginLocal.criar(loginUnicoUsuario);
			usuarioDAO.liberar(usuarioForcado, sessao.getUsuario());
			sessao.atualizaUsuario();
			Messages.create("Sucesso!").detail("Usuário moderado com sucesso.").add();
		} catch (ApplicationException e) {
			Messages.create("Aviso!").warn().detail(e.getLocalizedMessage()).add();
		}
	}
	
	@Override
	public void grantAccess() throws AccessDeniedException {
		checkAccess(sessao.getUsuario(), "CUS DEV");
	}
	
}
