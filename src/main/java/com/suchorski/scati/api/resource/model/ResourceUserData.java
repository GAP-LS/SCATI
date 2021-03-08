package com.suchorski.scati.api.resource.model;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.suchorski.scati.ad.fab.LoginUnicoUsuario;
import com.suchorski.scati.models.Aplicacao;
import com.suchorski.scati.models.Usuario;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceUserData {
	
	private boolean valid;
	private String message;
	private String cpf;
	private String saram;
	private String nomeCompleto;
	private String nomeGuerra;
	private String patente;
	private String om;
	private String zimbra;
	private List<String> perfis;
	
	public ResourceUserData(String message) {
		this.valid = false;
		this.message = message;
	}
	
	public ResourceUserData(Usuario usuario, Aplicacao aplicacao) {
		this.valid = true;
		this.cpf = usuario.getCpf();
		this.saram = usuario.getSaram();
		this.nomeCompleto = usuario.getNomeCompleto();
		this.nomeGuerra = usuario.getNomeGuerra();
		this.patente = usuario.getPatente().getSigla();
		this.om = usuario.getOmPrestacao();
		this.zimbra = usuario.getZimbra();
		this.perfis = usuario.getPerfils().stream().filter(p -> aplicacao.getPerfils().contains(p)).map(p -> p.getCodigo()).collect(Collectors.toList());
	}

	public ResourceUserData(LoginUnicoUsuario usuario) {
		this.valid = true;
		this.cpf = usuario.getCpf();
		this.saram = usuario.getSaram();
		this.nomeCompleto = usuario.getNomeCompleto();
		this.nomeGuerra = usuario.getNomeGuerra();
		this.patente = usuario.getPatente();
		this.om = usuario.getOmPrestacaoServico();
		this.zimbra = usuario.getZimbra();
		this.perfis = new ArrayList<String>();
	}

}
