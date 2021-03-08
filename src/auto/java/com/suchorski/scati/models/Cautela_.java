package com.suchorski.scati.models;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2019-12-26T11:02:14.664-0200")
@StaticMetamodel(Cautela.class)
public class Cautela_ {
	public static volatile SingularAttribute<Cautela, Long> id;
	public static volatile SingularAttribute<Cautela, Date> dataDevolucao;
	public static volatile SingularAttribute<Cautela, Date> dataRetirada;
	public static volatile SingularAttribute<Cautela, String> descricao;
	public static volatile SingularAttribute<Cautela, Usuario> liberador;
	public static volatile SingularAttribute<Cautela, Usuario> recebedor;
	public static volatile SingularAttribute<Cautela, Usuario> usuario;
}
