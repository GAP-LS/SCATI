package com.suchorski.scati.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;
import org.primefaces.model.DualListModel;

import com.github.adminfaces.template.exception.AccessDeniedException;
import com.suchorski.scati.daos.AplicacaoDAO;
import com.suchorski.scati.daos.UsuarioDAO;
import com.suchorski.scati.generics.AccessNeededController;
import com.suchorski.scati.models.Aplicacao;
import com.suchorski.scati.models.Perfil;
import com.suchorski.scati.models.Usuario;

import lombok.Getter;
import lombok.Setter;

@Named("administrarUsuarios")
@ViewScoped
public class AdministrarUsuariosController implements AccessNeededController, Serializable {

	private static final long serialVersionUID = -8417748412184590670L;

	@Inject private SessaoController sessao;
	
	private UsuarioDAO usuarioDAO;
	private AplicacaoDAO aplicacaoDAO;

	@Getter private List<Usuario> usuariosAtivos;
	@Getter private List<Aplicacao> aplicacaos;
	
	@Getter @Setter private Usuario usuario;
	@Getter @Setter private DualListModel<Perfil> perfis;
	@Getter @Setter private Aplicacao aplicacao;
	
	@PostConstruct
	public void init() {
		usuarioDAO = new UsuarioDAO();
		aplicacaoDAO = new AplicacaoDAO();
		usuariosAtivos = usuarioDAO.listAtivos();
		usuario = usuariosAtivos.get(0);
		aplicacaos = aplicacaoDAO.list();
		aplicacao = aplicacaos.get(0);
		mudarOpcoes();
	}
	
	@PreDestroy
	public void destroy() {
		aplicacaoDAO.close();
		usuarioDAO.close();
	}
	
	public void primeiraAplicacao() {
		aplicacao = aplicacaos.get(0);
		mudarOpcoes();
	}
	
	public void mudarOpcoes() {
		List<Perfil> source = new ArrayList<Perfil>();
		List<Perfil> target = new ArrayList<Perfil>();
		List<Perfil> list = new ArrayList<Perfil>(aplicacao.getPerfils());
		list.forEach(p -> {
			if (usuario.hasPerfil(p.getCodigo())) {
				target.add(p);
			} else {
				source.add(p);
			}
		});
		perfis = new DualListModel<Perfil>(source, target);
	}
	
	public void salvar() {
		usuarioDAO.updatePerfis(usuario, perfis.getSource(), perfis.getTarget());
		Messages.create("Sucesso!").detail("Perfis atualizados!").add();
	}

	@Override
	public void grantAccess() throws AccessDeniedException {
		checkAccess(sessao.getUsuario(), "AUS DEV");
	}
	
}