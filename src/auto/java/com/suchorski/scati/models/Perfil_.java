package com.suchorski.scati.models;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-12-26T11:02:14.801-0200")
@StaticMetamodel(Perfil.class)
public class Perfil_ {
	public static volatile SingularAttribute<Perfil, Long> id;
	public static volatile SingularAttribute<Perfil, String> codigo;
	public static volatile SingularAttribute<Perfil, String> descricao;
	public static volatile SingularAttribute<Perfil, String> titulo;
	public static volatile ListAttribute<Perfil, Usuario> usuarios;
	public static volatile SingularAttribute<Perfil, Aplicacao> aplicacao;
}
