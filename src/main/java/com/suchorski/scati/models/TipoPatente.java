package com.suchorski.scati.models;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name="tipo_patente")
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of={"id"})
public class TipoPatente implements Serializable {

	private static final long serialVersionUID = 5831047293943116770L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(insertable=false, updatable=false)
	private long id;

	@Column(unique=true)
	private int ordem;

	@Column(unique=true)
	private String titulo;

}