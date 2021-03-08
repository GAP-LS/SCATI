package com.suchorski.scati.daos;

import com.suchorski.scati.generics.GenericDAO;
import com.suchorski.scati.models.Aplicacao;
import com.suchorski.scati.models.Perfil;

public class PerfilDAO extends GenericDAO<Perfil, Long> {
	
	public PerfilDAO() {
		super(Perfil.class);
	}
	
	public void adicionar(String codigo, String titulo, String descricao, Aplicacao aplicacao) {
		Perfil p = new Perfil();
		p.setCodigo(codigo);
		p.setTitulo(titulo);
		p.setDescricao(descricao);
		p.setAplicacao(aplicacao);
		save(p);
	}
	
	public void remover(Perfil perfil) {
		deleteById(perfil.getId());
	}
	
}
