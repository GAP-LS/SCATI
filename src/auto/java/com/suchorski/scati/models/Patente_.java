package com.suchorski.scati.models;

import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-12-26T11:02:14.762-0200")
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
