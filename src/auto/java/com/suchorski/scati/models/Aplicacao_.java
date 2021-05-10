package com.suchorski.scati.models;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-04-05T11:13:30.444-0300")
@StaticMetamodel(Aplicacao.class)
public class Aplicacao_ {
	public static volatile SingularAttribute<Aplicacao, Long> id;
	public static volatile SingularAttribute<Aplicacao, String> chavePublica;
	public static volatile SingularAttribute<Aplicacao, String> chaveSecreta;
	public static volatile SingularAttribute<Aplicacao, Date> dataCriacao;
	public static volatile SingularAttribute<Aplicacao, String> nome;
	public static volatile SingularAttribute<Aplicacao, String> sigla;
	public static volatile SingularAttribute<Aplicacao, Usuario> usuario;
	public static volatile ListAttribute<Aplicacao, Token> tokens;
	public static volatile SetAttribute<Aplicacao, Perfil> perfils;
	public static volatile SetAttribute<Aplicacao, Usuario> usuariosGerentes;
}
