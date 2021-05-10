package com.suchorski.scati.models;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-04-05T11:13:30.482-0300")
@StaticMetamodel(Patente.class)
public class Patente_ {
	public static volatile SingularAttribute<Patente, Long> id;
	public static volatile SingularAttribute<Patente, String> abreviacao;
	public static volatile SingularAttribute<Patente, Integer> ordem;
	public static volatile SingularAttribute<Patente, String> sigla;
	public static volatile SingularAttribute<Patente, String> titulo;
	public static volatile SingularAttribute<Patente, TipoPatente> tipoPatente;
	public static volatile ListAttribute<Patente, Usuario> usuarios;
}
