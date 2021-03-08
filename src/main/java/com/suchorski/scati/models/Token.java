package com.suchorski.scati.models;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
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
@EqualsAndHashCode(of={"token"})
public class Token implements Serializable {

	private static final long serialVersionUID = 6688294122228404015L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(insertable=false, updatable=false)
	private long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_criacao", updatable=false)
	private Date dataCriacao;

	@Column(updatable=false, unique=true)
	private String token;

	@ManyToOne
	private Aplicacao aplicacao;

	@ManyToOne
	private Usuario usuario;

}