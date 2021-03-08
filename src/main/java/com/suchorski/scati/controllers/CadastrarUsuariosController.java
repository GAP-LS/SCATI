package com.suchorski.scati.controllers;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.naming.NamingException;
import javax.persistence.NoResultException;

import org.omnifaces.util.Messages;

import com.github.adminfaces.template.exception.AccessDeniedException;
import com.suchorski.scati.ad.fab.LoginUnicoController;
import com.suchorski.scati.ad.fab.LoginUnicoUsuario;
import com.suchorski.scati.ad.local.LoginLocalController;
import com.suchorski.scati.daos.PatenteDAO;
import com.suchorski.scati.daos.UsuarioDAO;
import com.suchorski.scati.exceptions.ApplicationException;
import com.suchorski.scati.generics.AccessNeededController;
import com.suchorski.scati.models.Patente;
import com.suchorski.scati.models.Usuario;

import lombok.Getter;
import lombok.Setter;

@Named("cadastrarUsuarios")
@ViewScoped
public class CadastrarUsuariosController implements AccessNeededController, Serializable {
	
	private static final long serialVersionUID = -4546059847717926438L;

	@Resource(lookup="java:jboss/mail/ati")
	private Session session;

	@Inject private AplicacaoController app;
	@Inject private SessaoController sessao;
	@Inject private LoginUnicoController loginUnico;
	@Inject private LoginLocalController loginLocal;
	
	private PatenteDAO patenteDAO;
	private UsuarioDAO usuarioDAO;
	
	@Getter @Setter private String cpf;
	@Getter @Setter	private LoginUnicoUsuario cadastradoLu; 
	
	@PostConstruct
	public void init() {
		patenteDAO = new PatenteDAO();
		usuarioDAO = new UsuarioDAO();
	}
	
	@PreDestroy
	public void destroy() {
		usuarioDAO.close();
		patenteDAO.close();
	}
	
	public void buscar() throws NamingException {
		boolean hasCadastro;
		try {
			usuarioDAO.findByCpf(cpf);
			cadastradoLu = null;
			hasCadastro = true;
		} catch (NoResultException e) {
			hasCadastro = false;
		}
		if (!hasCadastro) {
			try {
				cadastradoLu = loginUnico.findByCpfOrSaram(cpf);
			} catch (ApplicationException e) {
				Messages.create("Aviso!").warn().detail(e.getLocalizedMessage()).add();
			}
		} else {
			Messages.create("Aviso!").warn().detail("Usuário já possui cadastro").add();
		}
	}
	
	public void cadastrar() throws AddressException, UnsupportedEncodingException, NamingException, MessagingException {
		Patente patente = patenteDAO.findBySigla(cadastradoLu.getPatente());
		Usuario u = new Usuario(cadastradoLu, patente, sessao.getUsuario());
		u.setVisitante(LoginLocalController.isVisitante(app.getOpcao().getListOmsApoiadas(), cadastradoLu));
		usuarioDAO.save(u);
		loginLocal.criar(cadastradoLu);
		usuarioDAO.liberar(u, sessao.getUsuario());
		cadastradoLu = null;
		Messages.create("Sucesso!").detail("Usuário cadastrado com sucesso.").add();
	}
	
	@Override
	public void grantAccess() throws AccessDeniedException {
		checkAccess(sessao.getUsuario(), "DEV");
	}
	
}
