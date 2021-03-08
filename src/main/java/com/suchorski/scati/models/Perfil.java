package com.suchorski.scati.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OrderBy;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of={"codigo", "aplicacao"})
public class Perfil implements Serializable {

	private static final long serialVersionUID = 3733433488754724610L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(insertable=false, updatable=false)
	private long id;

	@Column(unique=true)
	private String codigo;

	private String descricao;

	private String titulo;

	@ManyToMany(mappedBy="perfils")
	@OrderBy("nome_completo asc")
	private List<Usuario> usuarios;
	
	@ManyToOne
	private Aplicacao aplicacao;
	
}