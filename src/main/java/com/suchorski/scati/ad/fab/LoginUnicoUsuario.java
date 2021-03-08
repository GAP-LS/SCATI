package com.suchorski.scati.ad.fab;

import java.io.Serializable;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode(of={"cpf", "saram"})
public class LoginUnicoUsuario implements Serializable {
	
	private static final long serialVersionUID = 8973466543669147775L;
	
	private String nomeCompleto; 		// cn
	private String descricao; 			// description
	private boolean contaExpirada; 		// FABcontaexpirada
	private String nomeGuerra; 			// FABguerra
	private boolean emailLiberado; 		// FABmail
	private String emailRecuperacao; 	// FABmailrecover
	private String saram; 				// FABnrordem
	private String omAtual; 			// FABom
	private String omPrestacaoServico; 	// FABomprest
	private String patente; 			// FABpostograd
	private boolean internetLiberada; 	// FABproxy
	private int nivelInternet; 			// FABproxyNivel
	private boolean status; 			// FABstatus
	private boolean termoAssinado; 		// FABtermcompr
	private boolean vpn;				// FABvpnweb
	private String login; 				// FABzimbraUID
	private String zimbra; 				// mail
	private String cpf; 				// uid
	
	public LoginUnicoUsuario(Attributes attrs) throws NamingException {
		int nivel;
		try {			
			nivel = Integer.parseInt(attrs.get("FABproxyNivel").get().toString());
		} catch (NumberFormatException e) {
			nivel = -1;
		}
		this.nomeCompleto = attrs.get("cn").get().toString();
		this.descricao = attrs.get("description").get().toString();
		this.contaExpirada = attrs.get("FABcontaexpirada").get().toString().equalsIgnoreCase("TRUE");
		this.nomeGuerra = attrs.get("FABguerra").get().toString();
		this.emailLiberado = attrs.get("FABmail").get().toString().equalsIgnoreCase("TRUE");
		this.emailRecuperacao = attrs.get("FABmailrecover").get().toString();
		this.saram = attrs.get("FABnrordem").get().toString();
		this.omAtual = attrs.get("FABom").get().toString();
		this.omPrestacaoServico = attrs.get("FABomprest").get().toString();
		this.patente = attrs.get("FABpostograd").get().toString();
		this.internetLiberada = (nivel > 0 && attrs.get("FABproxy").get().toString().equalsIgnoreCase("TRUE"));
		this.nivelInternet = nivel;
		this.status = attrs.get("FABstatus").get().toString().equalsIgnoreCase("TRUE");
		this.termoAssinado = attrs.get("FABtermcompr").get().toString().equalsIgnoreCase("TRUE");
		this.vpn = attrs.get("FABvpnweb").get().toString().equalsIgnoreCase("TRUE");
		this.login = attrs.get("FABzimbraUID").get().toString();
		this.zimbra = attrs.get("mail").get().toString();
		this.cpf = attrs.get("uid").get().toString();
	}
	
	public String getCn() {
		return String.format("%s %s %s [%s]", nomeGuerra, patente, omPrestacaoServico, cpf);
	}
	
	public String getDisplayName() {
		return String.format("%s %s [%s]", patente, nomeGuerra, omPrestacaoServico);
	}
	
	public String getOm() {
		if (omAtual.equals(omPrestacaoServico)) {
			return omAtual;
		}
		return String.format("%s (Prestação: %s)", omAtual, omPrestacaoServico);
	}
	
	public boolean isLiberado() {
		return status && termoAssinado && !contaExpirada; 
	}
	
	@Override
	public String toString() {
		return String.format("%s %s %s", patente, nomeGuerra, omPrestacaoServico);
	}
	
	public static String[] getReturningAttributes() {
		return new String[] {
				"cn",         "description",    "FABcontaexpirada", "FABguerra",
				"FABmail",    "FABmailrecover", "FABnrordem",       "FABom",
				"FABomprest", "FABpostograd",   "FABproxy",         "FABproxyNivel",
				"FABstatus",  "FABtermcompr",   "FABvpnweb",        "FABzimbraUID",
				"mail",       "uid"
		};
	}
	
}
