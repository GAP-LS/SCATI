package com.suchorski.scati.models;

import java.io.Serializable;
import javax.persistence.*;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of={"id"})
public class Cautela implements Serializable {

	private static final long serialVersionUID = -3622383168256243753L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(insertable=false, updatable=false)
	private long id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_devolucao", insertable=false, nullable=true)
	private Date dataDevolucao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_retirada", updatable=false)
	private Date dataRetirada;

	private String descricao;

	@ManyToOne
	private Usuario liberador;

	@ManyToOne
	private Usuario recebedor;

	@ManyToOne
	private Usuario usuario;

}