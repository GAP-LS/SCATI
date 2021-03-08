package com.suchorski.scati.daos;

import com.suchorski.scati.generics.GenericDAO;
import com.suchorski.scati.models.Opcao;

public class OpcaoDAO extends GenericDAO<Opcao, Long> {
	
	public OpcaoDAO() {
		super(Opcao.class);
	}
	
}
