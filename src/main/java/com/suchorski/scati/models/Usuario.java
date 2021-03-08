package com.suchorski.scati.models;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Where;

import com.suchorski.scati.ad.fab.LoginUnicoUsuario;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter
@Setter
@NoArgsConstructor
@EqualsAndHashCode(of={"cpf", "saram"})
public class Usuario implements Serializable {
	
	private static final long serialVersionUID = 6803534215019913788L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(insertable=false, updatable=false)
	private long id;

	private boolean bloqueado;
	
	@Column(updatable=false)
	private String cpf;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_atualizacao")
	private Date dataAtualizacao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_criacao", updatable=false)
	private Date dataCriacao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_insercao", nullable=true)
	private Date dataInsercao;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="data_remocao", insertable=false, nullable=true)
	private Date dataRemocao;

	@Column(updatable=false)
	private String login;

	@Column(name="nome_completo")
	private String nomeCompleto;

	@Column(name="nome_guerra")
	private String nomeGuerra;

	@Column(name="om_atual")
	private String omAtual;

	@Column(name="om_prestacao")
	private String omPrestacao;

	@Column(updatable=false)
	private String saram;

	private boolean visitante;

	private String zimbra;

	@OneToMany(mappedBy="usuario", orphanRemoval=true, cascade=CascadeType.ALL)
	@OrderBy(value="nome asc")
	private List<Aplicacao> aplicacaos;

	@OneToMany(mappedBy="liberador")
	@OrderBy("data_retirada desc")
	private List<Cautela> cautelasLiberadas;

	@OneToMany(mappedBy="recebedor")
	@OrderBy("data_devolucao desc")
	private List<Cautela> cautelasRecebidas;

	@OneToMany(mappedBy="usuario")
	@Where(clause="data_devolucao is null")
	@OrderBy("data_retirada desc")
	private List<Cautela> cautelasAbertas;

	@OneToMany(mappedBy="usuario")
	@Where(clause="data_devolucao is not null")
	@OrderBy("data_devolucao desc")
	private List<Cautela> cautelasFechadas;

	@OneToMany(mappedBy="usuario")
	private List<Token> tokens;

	@ManyToOne
	private Patente patente;

	@ManyToMany
	@JoinTable(
			name="usuario_has_perfil"
			, joinColumns={
					@JoinColumn(name="usuario_id")
			}
			, inverseJoinColumns={
					@JoinColumn(name="perfil_id")
			}
			)
	@OrderBy("titulo asc")
	private Set<Perfil> perfils;

	@ManyToOne
	private Usuario moderador;

	@OneToMany(mappedBy="moderador")
	@Where(clause="data_insercao is null")
	@OrderBy("nome_guerra asc")
	private List<Usuario> usuariosModerandos;

	@OneToMany(mappedBy="moderador")
	@Where(clause="data_insercao is not null")
	@OrderBy("nome_guerra asc")
	private List<Usuario> usuariosModerados;

	@OneToMany(mappedBy="moderador")
	@Where(clause="data_insercao is not null and data_remocao is null and visitante = 1")
	@OrderBy("nome_guerra asc")
	private List<Usuario> visitantes;

	@ManyToOne
	private Usuario finalizador;

	@OneToMany(mappedBy="finalizador")
	@Where(clause="data_insercao is not null")
	@OrderBy("nome_guerra asc")
	private List<Usuario> usuariosFinalizados;
	
	@ManyToMany
	@JoinTable(
			name="usuario_gerencia_aplicacao"
			, joinColumns={
					@JoinColumn(name="usuario_id")
			}
			, inverseJoinColumns={
					@JoinColumn(name="aplicacao_id")
			}
			)
	@OrderBy("nome asc")
	private Set<Aplicacao> aplicacaosGerenciadas;
	
	/* Changed method */
	public Usuario getModerador() {
		if (this.moderador == null && hasPerfil("DEV")) {
			return this;
		}
		return this.moderador;
	}

	/* Changed method */
	public Usuario getFinalizador() {
		if (this.finalizador == null && hasPerfil("DEV")) {
			return this;
		}
		return this.finalizador;
	}

	@Override
	public String toString() {
		return String.format("%s %s [%s]", patente.getSigla(), nomeGuerra, omPrestacao);
	}

	/* Additional contructors */
	public Usuario(LoginUnicoUsuario loginUnicoUsuario, Patente patente, Usuario moderador) {
		this.cpf = loginUnicoUsuario.getCpf();
		this.dataAtualizacao = new Date();
		this.dataCriacao = new Date();
		this.login = loginUnicoUsuario.getLogin();
		this.nomeCompleto = loginUnicoUsuario.getNomeCompleto();
		this.nomeGuerra = loginUnicoUsuario.getNomeGuerra();
		this.omAtual = loginUnicoUsuario.getOmAtual();
		this.omPrestacao = loginUnicoUsuario.getOmPrestacaoServico();
		this.saram = loginUnicoUsuario.getSaram();
		this.zimbra = loginUnicoUsuario.getZimbra();
		this.patente = patente;
		this.moderador = moderador;
	}

	/* Additional methods */
	public String getNome() {
		return String.format("%s %s", patente.getSigla(), nomeGuerra);
	}

	public String getOm() {
		if (omAtual.equals(omPrestacao)) {
			return omAtual;
		}
		return String.format("%s (Do: %s)", omPrestacao, omAtual);
	}

	public boolean isModerando() {
		return dataInsercao == null && dataRemocao == null;
	}

	public boolean isLiberado() {
		return dataRemocao == null && dataInsercao != null;
	}

	public boolean isHabilitado() {
		return !bloqueado && isLiberado();
	}

	public boolean isPodeModerar() {
		return patente.getTipoPatente().getOrdem() == 10 || hasPerfil("DEV");
	}

	public boolean hasPerfil(String codigos) {
		return Arrays.asList(codigos.split(" ")).stream().anyMatch(c -> getPerfils().stream().anyMatch(p -> p.getCodigo().equalsIgnoreCase(c)));
	}

	public void update(LoginUnicoUsuario loginUnicoUsuario, Patente patente) {
		update(loginUnicoUsuario, patente, this.visitante);
	}
	
	public void update(LoginUnicoUsuario loginUnicoUsuario, Patente patente, boolean isVisitante) {
		this.nomeCompleto = loginUnicoUsuario.getNomeCompleto();
		this.nomeGuerra = loginUnicoUsuario.getNomeGuerra();
		this.omAtual = loginUnicoUsuario.getOmAtual();
		this.omPrestacao = loginUnicoUsuario.getOmPrestacaoServico();
		this.patente = patente;
		this.visitante = isVisitante;
	}
	

}