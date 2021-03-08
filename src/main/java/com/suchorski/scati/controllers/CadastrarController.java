package com.suchorski.scati.controllers;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.context.Conversation;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;
import javax.persistence.NoResultException;

import org.omnifaces.util.Messages;

import com.suchorski.scati.ad.fab.LoginUnicoController;
import com.suchorski.scati.ad.fab.LoginUnicoUsuario;
import com.suchorski.scati.ad.local.LoginLocalController;
import com.suchorski.scati.daos.PatenteDAO;
import com.suchorski.scati.daos.UsuarioDAO;
import com.suchorski.scati.exceptions.ApplicationException;
import com.suchorski.scati.models.Patente;
import com.suchorski.scati.models.Usuario;

import lombok.Getter;
import lombok.Setter;

@Named("cadastro")
@ConversationScoped
public class CadastrarController implements Serializable {
	
	private static final long serialVersionUID = -1466765037841059676L;

	@Resource(lookup="java:jboss/mail/ati")
	private Session session;

	@Inject private AplicacaoController app;
	@Inject private Conversation conversation;
	@Inject private LoginUnicoController loginUnico;
	@Inject private LoginLocalController loginLocal;
	
	private PatenteDAO patenteDAO;
	private UsuarioDAO usuarioDAO;
	
	@Getter List<Usuario> moderadores;
	@Getter boolean isVisitante;

	@Getter @Setter private String cpf;
	@Getter @Setter private String senhaCpf;
	@Getter @Setter private boolean registrado;
	@Getter @Setter private String usuario;
	@Getter @Setter private String senhaUsuario;
	@Getter @Setter private Usuario moderador;
	@Getter @Setter private boolean aceitarTermo;
	
	@PostConstruct
	public void init() {
		conversation.begin();
		session.setDebug(false);
		session.getProperties().put("mail.debug", false);
		aceitarTermo = false;
		patenteDAO = new PatenteDAO();
		usuarioDAO = new UsuarioDAO();
	}
	
	@PreDestroy
	public void destroy() {
		usuarioDAO.close();
		patenteDAO.close();
	}
	
	public String cancelar() {
		conversation.end();
		return "login?faces-redirect=true";
	}

	public String prosseguir() throws NamingException {
		try {
			LoginUnicoUsuario loginUnicoUsuario = loginUnico.findUsuario(cpf, senhaCpf);
			isVisitante = LoginLocalController.isVisitante(app.getOpcao().getListOmsApoiadas(), loginUnicoUsuario);
			if (registrado) {
				loginLocal.login(usuario, senhaUsuario);
				if (isVisitante) {
					moderadores = usuarioDAO.listModeradores(patenteDAO.findBySigla(loginUnicoUsuario.getPatente()));
				}
			} else if (loginLocal.hasValue(app.getOpcao().getAdAtributoCpf(), cpf)) {
				registrado = true;
				Messages.create("Aviso!").warn().detail("Usuário já possui registro na rede.").add();
				return "";					
			} else {
				moderadores = usuarioDAO.listModeradores(patenteDAO.findBySigla(loginUnicoUsuario.getPatente()));
			}
			boolean hasAccount;
			try (UsuarioDAO gambiarra = new UsuarioDAO()) { // GAMBIARRA
				gambiarra.findByCpf(cpf); // GAMBIARRA
				hasAccount = true;
			} catch (NoResultException e) {
				hasAccount = false;
			}
			if (hasAccount) {
				Messages.create("Aviso!").flash().warn().detail("Usuário já possui cadastro.").add();
				return "login?faces-redirect=true";
			}
			aceitarTermo = true;
			return "cadastrar?faces-redirect=true";
		} catch (ApplicationException e) {
			Messages.create("Aviso!").warn().detail(e.getLocalizedMessage()).add();
		}
		return "";
	}
	
	public String registrar() throws NamingException, AddressException, UnsupportedEncodingException, MessagingException {
		try {
			LoginUnicoUsuario loginUnicoUsuario = loginUnico.findUsuario(cpf, senhaCpf);
			Patente patente = patenteDAO.findBySigla(loginUnicoUsuario.getPatente());
			Usuario u = new Usuario(loginUnicoUsuario, patente, moderador);
			if (registrado) {
				loginLocal.migrar(loginUnicoUsuario, loginLocal.findByUsernameWithoutCpfSaramPin(usuario));
				moderador = null;
				u.setDataInsercao(new Date());
			}
			u.setVisitante(isVisitante);
			UsuarioDAO gambiarra = new UsuarioDAO(); // GAMBIARRA
			gambiarra.save(u); // GAMBIARRA
			gambiarra.close(); // GAMBIARRA
			if (!registrado) {
				sendMail(moderador.getZimbra(), "Usuário pendente de moderação",
						"Você possui usuários pendentes de moderação no sistema SCATI!\r\n\r\n"
								+ "Usuário aguardando moderação: " + loginUnicoUsuario.getDisplayName() + "\r\n"
								+ "Link para acesso ao sistema: http://" + System.getenv("SCATI_DOMAIN") + "/scati/moderar_usuarios.xhtml"
						);
			}
			conversation.end();
			if (!registrado) {
				Messages.create("Sucesso!").flash().detail("Aguarde moderação do chefe selecionado.").add();
			} else {				
				Messages.create("Sucesso!").flash().detail("Usuário cadastrado com sucesso.").add();
			}
			return "login?faces-redirect=true";
		} catch (ApplicationException e) {
			Messages.create("Aviso!").warn().detail(e.getLocalizedMessage()).add();
		}
		return "";
	}
	
	private void sendMail(String email, String subject, String message) throws AddressException, MessagingException, UnsupportedEncodingException {
		if (!app.getOpcao().isModoSenhaMestra()) {
			Message m = new MimeMessage(session);
			m.setReplyTo(InternetAddress.parse(app.getOpcao().getMReplyto()));
			m.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
			m.setSubject(subject);
			m.setText(message + "\r\n\r\n" + app.getOpcao().getMAssinatura());
			Transport.send(m);
		}
	}

}
