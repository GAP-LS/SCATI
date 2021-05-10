package com.suchorski.scati.models;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-04-05T11:13:30.465-0300")
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
