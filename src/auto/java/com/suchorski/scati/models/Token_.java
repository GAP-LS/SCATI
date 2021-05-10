package com.suchorski.scati.models;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-04-05T11:13:30.516-0300")
@StaticMetamodel(Token.class)
public class Token_ {
	public static volatile SingularAttribute<Token, Long> id;
	public static volatile SingularAttribute<Token, Date> dataCriacao;
	public static volatile SingularAttribute<Token, String> token;
	public static volatile SingularAttribute<Token, Aplicacao> aplicacao;
	public static volatile SingularAttribute<Token, Usuario> usuario;
}
