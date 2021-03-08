package com.suchorski.scati.models;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of={"chavePublica", "chaveSecreta"})
public class Aplicacao implements Serializable {

	private static final long serialVersionUID = -2160508349562018972L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(insertable=false, updatable=false)
	private long id;

	@Column(name="chave_publica", updatable=false, unique=true)
	private String chavePublica;

	@Column(name="chave_secreta", updatable=false, unique=true)
	private String chaveSecreta;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_criacao", updatable=false)
	private Date dataCriacao;

	@Column(unique=true)
	private String nome;

	@Column(unique=true)
	private String sigla;
	
	@ManyToOne
	private Usuario usuario;

	@OneToMany(mappedBy="aplicacao", orphanRemoval=true)
	private List<Token> tokens;
	
	@OneToMany(mappedBy="aplicacao", cascade=CascadeType.ALL)
	@OrderBy("codigo asc")
	private Set<Perfil> perfils;
	
	@ManyToMany(mappedBy="aplicacaosGerenciadas")
	@OrderBy("nome_completo asc")
	private Set<Usuario> usuariosGerentes;
	
	@Override
	public String toString() {
		return String.format("%s - %s", sigla, nome);
	}
	
}