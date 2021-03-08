package com.suchorski.scati.controllers;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.naming.NamingException;
import javax.persistence.NoResultException;

import org.omnifaces.util.Messages;

import com.github.adminfaces.template.exception.AccessDeniedException;
import com.suchorski.scati.ad.fab.LoginUnicoController;
import com.suchorski.scati.ad.fab.LoginUnicoUsuario;
import com.suchorski.scati.daos.UsuarioDAO;
import com.suchorski.scati.exceptions.ApplicationException;
import com.suchorski.scati.generics.AccessNeededController;
import com.suchorski.scati.models.Usuario;

import lombok.Getter;
import lombok.Setter;

@Named("consultarUsuarios")
@ViewScoped
public class ConsultarUsuariosController implements AccessNeededController, Serializable {
	
	private static final long serialVersionUID = 4447780319790878501L;

	@Inject private SessaoController sessao;
	@Inject private LoginUnicoController loginUnico;
	
	private UsuarioDAO usuarioDAO;
	
	@Getter @Setter private String value;
	@Getter @Setter private String type;
	@Getter @Setter private String local;
	@Getter @Setter private Usuario usuario;
	@Getter @Setter private LoginUnicoUsuario loginUnicoUsuario;
	@Getter @Setter private List<LoginUnicoUsuario> encontrados;
	@Getter @Setter private List<Usuario> historicoCadastros;

	@PostConstruct
	public void init() {
		usuarioDAO = new UsuarioDAO();
		type = "nid";
		local = "om";
	}
	
	@PreDestroy
	public void destroy() {
		usuarioDAO.close();
	}
	
	public void buscar() throws NamingException {
		try {
			loginUnicoUsuario = null;
			usuario = null;
			encontrados = null;
			switch (type) {
			case "nid":
				loginUnicoUsuario = loginUnico.findByCpfOrSaram(value);
				usuario = usuarioDAO.findByCpf(value);
				break;
			case "nguerra":
				encontrados = loginUnico.listaUsuarios("FABguerra", value, "om".equals(local));
				if (encontrados.size() == 1) {
					loginUnicoUsuario = encontrados.get(0);
					usuario = usuarioDAO.findByCpf(loginUnicoUsuario.getCpf());
					historicoCadastros = usuarioDAO.listaHistorico(usuario);
				}
				break;
			case "ncompleto":
				encontrados = loginUnico.listaUsuarios("cn", value, "om".equals(local));
				if (encontrados.size() == 1) {
					loginUnicoUsuario = encontrados.get(0);
					usuario = usuarioDAO.findByCpf(loginUnicoUsuario.getCpf());
					historicoCadastros = usuarioDAO.listaHistorico(usuario);
				}
				break;
			}
		} catch (ApplicationException e) {
			Messages.create("Aviso!").warn().detail(e.getLocalizedMessage()).add();
		} catch (NoResultException e) {
			usuario = null;
		}
	}
	
	public void selecionar() throws NamingException {
		try {
			encontrados = null;
			loginUnicoUsuario = loginUnico.findByCpfOrSaram(value);
			usuario = usuarioDAO.findByCpf(value);
		} catch (ApplicationException e) {
			Messages.create("Aviso!").warn().detail(e.getLocalizedMessage()).add();
		} catch (NoResultException e) {
			usuario = null;
		}
	}

	@Override
	public void grantAccess() throws AccessDeniedException {
		checkAccess(sessao.getUsuarioAtualizado(), "CUS DEV");
	}
	
}
