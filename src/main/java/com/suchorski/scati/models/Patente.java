package com.suchorski.scati.models;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of={"sigla"})
public class Patente implements Serializable {

	private static final long serialVersionUID = 3816933065351512588L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(insertable=false, updatable=false)
	private long id;

	private String abreviacao;

	@Column(unique=true)
	private int ordem;

	@Column(unique=true)
	private String sigla;

	private String titulo;

	@ManyToOne
	@JoinColumn(name="tipo_patente_id")
	private TipoPatente tipoPatente;

	@OneToMany(mappedBy="patente")
	@OrderBy("nome_completo asc")
	private List<Usuario> usuarios;

}