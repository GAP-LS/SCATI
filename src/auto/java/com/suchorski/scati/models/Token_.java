package com.suchorski.scati.models;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-12-26T11:02:14.869-0200")
@StaticMetamodel(Token.class)
public class Token_ {
	public static volatile SingularAttribute<Token, Long> id;
	public static volatile SingularAttribute<Token, Date> dataCriacao;
	public static volatile SingularAttribute<Token, String> token;
	public static volatile SingularAttribute<Token, Aplicacao> aplicacao;
	public static volatile SingularAttribute<Token, Usuario> usuario;
}
