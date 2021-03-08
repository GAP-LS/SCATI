package com.suchorski.scati.models;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SetAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2021-02-01T09:48:57.894-0300")
@StaticMetamodel(Usuario.class)
public class Usuario_ {
	public static volatile SingularAttribute<Usuario, Long> id;
	public static volatile SingularAttribute<Usuario, Boolean> bloqueado;
	public static volatile SingularAttribute<Usuario, String> cpf;
	public static volatile SingularAttribute<Usuario, Date> dataAtualizacao;
	public static volatile SingularAttribute<Usuario, Date> dataCriacao;
	public static volatile SingularAttribute<Usuario, Date> dataInsercao;
	public static volatile SingularAttribute<Usuario, Date> dataRemocao;
	public static volatile SingularAttribute<Usuario, String> login;
	public static volatile SingularAttribute<Usuario, String> nomeCompleto;
	public static volatile SingularAttribute<Usuario, String> nomeGuerra;
	public static volatile SingularAttribute<Usuario, String> omAtual;
	public static volatile SingularAttribute<Usuario, String> omPrestacao;
	public static volatile SingularAttribute<Usuario, String> saram;
	public static volatile SingularAttribute<Usuario, Boolean> visitante;
	public static volatile SingularAttribute<Usuario, String> zimbra;
	public static volatile ListAttribute<Usuario, Aplicacao> aplicacaos;
	public static volatile ListAttribute<Usuario, Cautela> cautelasLiberadas;
	public static volatile ListAttribute<Usuario, Cautela> cautelasRecebidas;
	public static volatile ListAttribute<Usuario, Cautela> cautelasAbertas;
	public static volatile ListAttribute<Usuario, Cautela> cautelasFechadas;
	public static volatile ListAttribute<Usuario, Token> tokens;
	public static volatile SingularAttribute<Usuario, Patente> patente;
	public static volatile SetAttribute<Usuario, Perfil> perfils;
	public static volatile SingularAttribute<Usuario, Usuario> moderador;
	public static volatile ListAttribute<Usuario, Usuario> usuariosModerandos;
	public static volatile ListAttribute<Usuario, Usuario> usuariosModerados;
	public static volatile ListAttribute<Usuario, Usuario> visitantes;
	public static volatile SingularAttribute<Usuario, Usuario> finalizador;
	public static volatile ListAttribute<Usuario, Usuario> usuariosFinalizados;
	public static volatile SetAttribute<Usuario, Aplicacao> aplicacaosGerenciadas;
}
