package com.suchorski.scati.daos;

import java.util.Date;

import com.suchorski.scati.generics.GenericDAO;
import com.suchorski.scati.models.Cautela;
import com.suchorski.scati.models.Usuario;

public class CautelaDAO extends GenericDAO<Cautela, Long> {
	
	public CautelaDAO() {
		super(Cautela.class);
	}
	
	public void registrar(String descricao, Usuario usuario, Usuario liberador) {
		Cautela c = new Cautela();
		c.setDescricao(descricao);
		c.setUsuario(usuario);
		c.setLiberador(liberador);
		c.setDataRetirada(new Date());
		save(c);
	}
	
	public void receber(Cautela cautela, Usuario recebedor) {
		cautela.setRecebedor(recebedor);
		cautela.setDataDevolucao(new Date());
		update(cautela);
	}
	
}
