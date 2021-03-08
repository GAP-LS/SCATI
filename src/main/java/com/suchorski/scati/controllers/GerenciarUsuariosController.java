package com.suchorski.scati.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.omnifaces.util.Messages;
import org.primefaces.model.DualListModel;

import com.github.adminfaces.template.exception.AccessDeniedException;
import com.suchorski.scati.daos.UsuarioDAO;
import com.suchorski.scati.generics.AccessNeededController;
import com.suchorski.scati.models.Aplicacao;
import com.suchorski.scati.models.Perfil;
import com.suchorski.scati.models.Usuario;

import lombok.Getter;
import lombok.Setter;

@Named("gerenciarUsuarios")
@RequestScoped
public class GerenciarUsuariosController implements AccessNeededController {

	@Inject private SessaoController sessao;
	
	private UsuarioDAO usuarioDAO;

	@Getter private List<Usuario> usuariosAtivos;
	@Getter private List<Aplicacao> aplicacaos;
	
	@Getter @Setter private Usuario usuario;
	@Getter @Setter private DualListModel<Perfil> perfis;
	@Getter @Setter private Aplicacao aplicacao;
	
	@PostConstruct
	public void init() {
		usuarioDAO = new UsuarioDAO();
		usuariosAtivos = usuarioDAO.listAtivos();
		usuario = usuariosAtivos.get(0);
		aplicacaos = new ArrayList<Aplicacao>(sessao.getUsuarioAtualizado().getAplicacaosGerenciadas());
		aplicacao = aplicacaos.get(0);
		mudarOpcoes();
	}
	
	@PreDestroy
	public void destroy() {
		usuarioDAO.close();
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
		Messages.create("Sucesso!").detail("Perfis atualizados.").add();
	}

	@Override
	public void grantAccess() throws AccessDeniedException {
		if (sessao.getUsuario().getAplicacaosGerenciadas().isEmpty()) {
			throw new AccessDeniedException();
		}
	}
	
}