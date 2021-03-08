package com.suchorski.scati.models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of={"id"})
public class Opcao implements Serializable {

	private static final long serialVersionUID = -4860462030629067501L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(insertable=false, updatable=false)
	private long id;

	@Column(name="ad_atributo_name")
	private String adAtributoName;

	@Column(name="ad_atributo_login")
	private String adAtributoLogin;
	
	@Column(name="ad_atributo_login_mail")
	private String adAtributoLoginMail;
	
	@Column(name="ad_atributo_cpf")
	private String adAtributoCpf;

	@Column(name="ad_atributo_pin")
	private String adAtributoPin;

	@Column(name="ad_atributo_saram")
	private String adAtributoSaram;

	@Column(name="ad_dominio")
	private String adDominio;

	@Column(name="ad_ou_usuarios")
	private String adOuUsuarios;

	@Column(name="ad_ou_visitantes")
	private String adOuVisitantes;

	@Column(name="ad_tamanho_pin")
	private int adTamanhoPin;

	@Column(name="m_assinatura")
	private String mAssinatura;

	@Column(name="m_replyto")
	private String mReplyto;

	@Column(name="maximo_busca")
	private int maximoBusca;

	@Column(name="modo_manutencao")
	private boolean modoManutencao;

	@Column(name="modo_senha_mestra")
	private boolean modoSenhaMestra;

	@Column(name="oms_apoiadas")
	private String omsApoiadas;

	@Column(name="senha_mestra")
	private String senhaMestra;
	
	/* Additional methods */
	public void setListOmsApoiadas(List<String> omsApoiadas) {
		this.omsApoiadas = omsApoiadas.stream().distinct().collect(Collectors.joining("|"));
	}
	
	public List<String> getListOmsApoiadas() {
		return Arrays.asList(omsApoiadas.split("\\|"));
	}

}