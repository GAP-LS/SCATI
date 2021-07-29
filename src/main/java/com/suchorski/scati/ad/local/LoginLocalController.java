package com.suchorski.scati.ad.local;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.BasicAttribute;
import javax.naming.directory.BasicAttributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.ModificationItem;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import com.suchorski.scati.ad.fab.LoginUnicoUsuario;
import com.suchorski.scati.ad.utils.SenhaAD;
import com.suchorski.scati.controllers.AplicacaoController;
import com.suchorski.scati.exceptions.ApplicationException;
import com.suchorski.scati.models.Usuario;
import com.suchorski.scati.utils.RandomUtils;

@Named("loginLocal")
@RequestScoped
public class LoginLocalController {
	
	@Resource(lookup="java:jboss/mail/ati")
	private Session session;

	/* Gambiarra */
//	@Resource(lookup="java:global/ldap/local")
//	private InitialDirContext context;
	private LdapContext context;

	@Inject
	private AplicacaoController app;
	
	@PostConstruct
	public void init() {
		session.setDebug(false);
		session.getProperties().put("mail.debug", false);
		/* Gambiarra */
		try {
			Hashtable<String, String> env = new Hashtable<>();
			env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			env.put(Context.PROVIDER_URL, System.getenv("SCATI_LDAP_CONNECTION"));
			env.put(Context.SECURITY_PROTOCOL, "ssl");
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, System.getenv("SCATI_LDAP_USERNAME"));
			env.put(Context.SECURITY_CREDENTIALS, System.getenv("SCATI_LDAP_PASSWORD"));
			env.put("java.naming.ldap.factory.socket", "com.suchorski.scati.utils.MySSLSocketFactory");
			context = new InitialLdapContext(env, null);
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}

	@PreDestroy
	public void destroy() {
		try {
			if (context != null) {
				context.close();
				context = null;
			}
		} catch (NamingException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	protected void finalize() throws Throwable {
		destroy();
		super.finalize();
	}

	public void criar(LoginUnicoUsuario loginUnicoUsuario) throws NamingException, AddressException, UnsupportedEncodingException, MessagingException {
		String pin = generatePin();
		String username = generateUsername(loginUnicoUsuario.getLogin());
		SenhaAD senha = SenhaAD.randomize();
		Attributes attrs = new BasicAttributes();
		Attribute objectClass = new BasicAttribute("objectClass");
		objectClass.add("top");
		objectClass.add("person");
		objectClass.add("organizationalPerson");
		objectClass.add("user");
//		objectClass.add("inetOrgPerson"); // LDAP Teste
		attrs.put(objectClass);
		Attribute cn = new BasicAttribute("cn", loginUnicoUsuario.getCn());
		attrs.put(cn);
		Attribute name = new BasicAttribute(app.getOpcao().getAdAtributoName(), loginUnicoUsuario.toString());
		attrs.put(name);
		Attribute displayName = new BasicAttribute("displayName", loginUnicoUsuario.getDisplayName());
		attrs.put(displayName);
		Attribute givenName = new BasicAttribute("givenName", loginUnicoUsuario.getNomeGuerra());
		attrs.put(givenName);
		Attribute initials = new BasicAttribute("initials", loginUnicoUsuario.getPatente());
		attrs.put(initials);
		Attribute mail = new BasicAttribute("mail", loginUnicoUsuario.getZimbra());
		attrs.put(mail);
		Attribute pinImpressora = new BasicAttribute(app.getOpcao().getAdAtributoPin(), pin);
		attrs.put(pinImpressora);
		Attribute saram = new BasicAttribute(app.getOpcao().getAdAtributoSaram(), loginUnicoUsuario.getSaram());
		attrs.put(saram);
		Attribute sn = new BasicAttribute("sn", loginUnicoUsuario.getNomeCompleto());
		attrs.put(sn);
		Attribute cpf = new BasicAttribute(app.getOpcao().getAdAtributoCpf(), loginUnicoUsuario.getCpf());
		attrs.put(cpf);
//		Attribute uid = new BasicAttribute("uid", loginUnicoUsuario.getCpf());
//		attrs.put(uid);
		Attribute sAMAccountName = new BasicAttribute(app.getOpcao().getAdAtributoLogin(), username);
		attrs.put(sAMAccountName);
		Attribute userPrincipalName = new BasicAttribute(app.getOpcao().getAdAtributoLoginMail(), String.format("%s@%s", username, app.getOpcao().getAdDominio()));
		attrs.put(userPrincipalName);
		Attribute userPassword = new BasicAttribute("userPassword", senha.getSha1());
		attrs.put(userPassword);
		Attribute unicodePwd = new BasicAttribute("unicodePwd", senha.getUnicode());
		attrs.put(unicodePwd);
		Attribute userAccountControl = new BasicAttribute("userAccountControl", "512");
		attrs.put(userAccountControl);
		Attribute pwdLastSet = new BasicAttribute("pwdLastSet", "0");
		attrs.put(pwdLastSet);
		String login = String.format("cn=%s,ou=%s,ou=%s", loginUnicoUsuario.getCn(), getOu(loginUnicoUsuario), app.getOpcao().getAdOuUsuarios());
		context.createSubcontext(login, attrs);
		sendMail(loginUnicoUsuario.getZimbra(), "Criação de usuário",
				"Usuário criado com sucesso!\r\n\r\n"
						+ "Seu usuário de rede é: " + loginUnicoUsuario.getLogin() + "\r\n"
						+ "Sua senha de rede é: " + senha.getSenha() + "\r\n"
						+ "Seu PIN para utilizar a impressora é: " + pin
				);
	}

	public void migrar(LoginUnicoUsuario loginUnicoUsuario, LoginLocalUsuario loginLocalUsuario) throws NamingException {
		String pin = generatePin();
		SearchControls searchControls = new SearchControls();
		searchControls.setReturningAttributes(LoginLocalUsuario.getReturningAttributes(app.getOpcao().getAdAtributoName(), app.getOpcao().getAdAtributoLogin(), app.getOpcao().getAdAtributoLoginMail(), app.getOpcao().getAdAtributoCpf(), app.getOpcao().getAdAtributoSaram(), app.getOpcao().getAdAtributoPin()));
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		NamingEnumeration<SearchResult> naming = context.search("", String.format("(%s=%s)", app.getOpcao().getAdAtributoLogin(), loginLocalUsuario.getLogin()), searchControls);
		if (naming.hasMoreElements()) {
			SearchResult result = (SearchResult) naming.next();
			ModificationItem[] items = new ModificationItem[8];
			items[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, (Attribute) new BasicAttribute("sn", loginUnicoUsuario.getNomeCompleto()));
			items[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, (Attribute) new BasicAttribute("givenName", loginUnicoUsuario.getNomeGuerra()));
			items[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, (Attribute) new BasicAttribute("initials", loginUnicoUsuario.getPatente()));
			items[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, (Attribute) new BasicAttribute("displayName", loginUnicoUsuario.getDisplayName()));
			items[4] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, (Attribute) new BasicAttribute("mail", loginUnicoUsuario.getZimbra()));
			items[5] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, (Attribute) new BasicAttribute(app.getOpcao().getAdAtributoCpf(), loginUnicoUsuario.getCpf()));
			items[6] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, (Attribute) new BasicAttribute(app.getOpcao().getAdAtributoSaram(), loginUnicoUsuario.getSaram()));
			items[7] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, (Attribute) new BasicAttribute(app.getOpcao().getAdAtributoPin(), pin));
			context.modifyAttributes(result.getName(), items);
			String newCn = String.format("cn=%s,ou=%s,ou=%s", loginUnicoUsuario.getCn(), getOu(loginUnicoUsuario), app.getOpcao().getAdOuUsuarios());
			context.rename(result.getName(), newCn.replace("\"", ""));
		}
	}

	public LoginLocalUsuario findUsuario(String username, String password) throws NamingException, ApplicationException {
		login(username, password);
		SearchControls searchControls = new SearchControls();
		searchControls.setReturningAttributes(LoginLocalUsuario.getReturningAttributes(app.getOpcao().getAdAtributoName(), app.getOpcao().getAdAtributoLogin(), app.getOpcao().getAdAtributoLoginMail(), app.getOpcao().getAdAtributoCpf(), app.getOpcao().getAdAtributoSaram(), app.getOpcao().getAdAtributoPin()));
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		NamingEnumeration<SearchResult> naming;
		naming = context.search("", String.format("(&(%s=%s))", app.getOpcao().getAdAtributoLogin(), username), searchControls);
		if (naming.hasMoreElements()) {
			SearchResult result = (SearchResult) naming.next();
			Attributes attrs = result.getAttributes();
			return new LoginLocalUsuario(attrs, app.getOpcao().getAdAtributoName(), app.getOpcao().getAdAtributoLogin(), app.getOpcao().getAdAtributoLoginMail(), app.getOpcao().getAdAtributoCpf(), app.getOpcao().getAdAtributoSaram(), app.getOpcao().getAdAtributoPin());
		}
		throw new ApplicationException("Usuário não encontrado.");
	}

	public LoginLocalUsuario findByCpfOrSaram(String cpfSaram) throws NamingException, ApplicationException {
		SearchControls searchControls = new SearchControls();
		searchControls.setReturningAttributes(LoginLocalUsuario.getReturningAttributes(app.getOpcao().getAdAtributoName(), app.getOpcao().getAdAtributoLogin(), app.getOpcao().getAdAtributoLoginMail(), app.getOpcao().getAdAtributoCpf(), app.getOpcao().getAdAtributoSaram(), app.getOpcao().getAdAtributoPin()));
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		NamingEnumeration<SearchResult> naming = context.search(String.format("ou=%s", app.getOpcao().getAdOuUsuarios()), String.format("(|(%s=%s)(%s=%s))", app.getOpcao().getAdAtributoCpf(), cpfSaram, app.getOpcao().getAdAtributoSaram(), cpfSaram), searchControls);
		if (naming.hasMoreElements()) {
			SearchResult result = (SearchResult) naming.next();
			Attributes attrs = result.getAttributes();
			return new LoginLocalUsuario(attrs, app.getOpcao().getAdAtributoName(), app.getOpcao().getAdAtributoLogin(), app.getOpcao().getAdAtributoLoginMail(), app.getOpcao().getAdAtributoCpf(), app.getOpcao().getAdAtributoSaram(), app.getOpcao().getAdAtributoPin());
		}
		throw new ApplicationException("Usuário não encontrado.");
	}

	public LoginLocalUsuario findByUsernameWithoutCpfSaramPin(String username) throws NamingException, ApplicationException {
		SearchControls searchControls = new SearchControls();
		searchControls.setReturningAttributes(LoginLocalUsuario.getReturningAttributes(app.getOpcao().getAdAtributoName(), app.getOpcao().getAdAtributoLogin(), app.getOpcao().getAdAtributoLoginMail(), app.getOpcao().getAdAtributoCpf(), app.getOpcao().getAdAtributoSaram(), app.getOpcao().getAdAtributoPin()));
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		NamingEnumeration<SearchResult> naming = context.search("", String.format("(&(%s=%s))", app.getOpcao().getAdAtributoLogin(), username), searchControls);
		if (naming.hasMoreElements()) {
			SearchResult result = (SearchResult) naming.next();
			Attributes attrs = result.getAttributes();
			return new LoginLocalUsuario(attrs, app.getOpcao().getAdAtributoName(), app.getOpcao().getAdAtributoLogin(), app.getOpcao().getAdAtributoLoginMail());
		}
		throw new ApplicationException("Usuário não encontrado.");
	}

	public void atualizar(LoginUnicoUsuario loginUnicoUsuario) throws NamingException {
		SearchControls searchControls = new SearchControls();
		searchControls.setReturningAttributes(new String[] {"cn", app.getOpcao().getAdAtributoName(), "displayName", "givenName", "initials", "sn", app.getOpcao().getAdAtributoPin()});
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		NamingEnumeration<SearchResult> naming = context.search("", String.format("(%s=%s)", app.getOpcao().getAdAtributoCpf(), loginUnicoUsuario.getCpf()), searchControls);
		if (naming.hasMoreElements()) {
			SearchResult result = (SearchResult) naming.next();
			ModificationItem[] items = new ModificationItem[4];
			items[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, (Attribute) new BasicAttribute("displayName", loginUnicoUsuario.getDisplayName()));
			items[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, (Attribute) new BasicAttribute("givenName", loginUnicoUsuario.getNomeGuerra()));
			items[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, (Attribute) new BasicAttribute("initials", loginUnicoUsuario.getPatente()));
			items[3] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, (Attribute) new BasicAttribute("sn", loginUnicoUsuario.getNomeCompleto()));
			context.modifyAttributes(result.getName(), items);
			String newCn = String.format("cn=%s,ou=%s,ou=%s", loginUnicoUsuario.getCn(), getOu(loginUnicoUsuario), app.getOpcao().getAdOuUsuarios());
			context.rename(result.getName(), newCn.replace("\"", ""));
		}
	}

	public void gerarSenha(Usuario usuario) throws NamingException, AddressException, UnsupportedEncodingException, MessagingException {
		SenhaAD senha = SenhaAD.randomize();
		SearchControls searchControls = new SearchControls();
		searchControls.setReturningAttributes(new String[] {"userPassword", "unicodePwd", "pwdLastSet"});
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		NamingEnumeration<SearchResult> naming = context.search("", String.format("(%s=%s)", app.getOpcao().getAdAtributoCpf(), usuario.getCpf()), searchControls);
		SearchResult result = (SearchResult) naming.next();
		ModificationItem[] items = new ModificationItem[3];
		items[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, (Attribute) new BasicAttribute("unicodePwd", senha.getUnicode()));
		items[1] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, (Attribute) new BasicAttribute("userPassword", senha.getSha1()));
		items[2] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, (Attribute) new BasicAttribute("pwdLastSet", "0"));
		context.modifyAttributes(result.getName(), items);
		sendMail(usuario.getZimbra(), "Atualização de senha",
				"Senha atualizada com sucesso!\r\n\r\n"
						+ "Sua nova senha de rede é: " + senha.getSenha()
				);
	}

	public void definePin(Usuario usuario, String pin) throws NamingException, ApplicationException, AddressException, UnsupportedEncodingException, MessagingException {
		if (checkPin(pin)) {
			throw new ApplicationException("PIN selecionado já existe");
		}
		SearchControls searchControls = new SearchControls();
		searchControls.setReturningAttributes(new String[] {"cn", app.getOpcao().getAdAtributoPin()});
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		NamingEnumeration<SearchResult> naming = context.search("", String.format("(%s=%s)", app.getOpcao().getAdAtributoCpf(), usuario.getCpf()), searchControls);
		if (naming.hasMore()) {
			SearchResult result = (SearchResult) naming.next();
			ModificationItem[] items = new ModificationItem[1];
			items[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, (Attribute) new BasicAttribute(app.getOpcao().getAdAtributoPin(), pin));
			context.modifyAttributes(result.getName(), items);
			sendMail(usuario.getZimbra(), "Atualização de PIN",
					"PIN atualizado com sucesso!\r\n\r\n"
							+ "Seu PIN para utilizar a impressora é " + pin
					);
		}
	}

	public void gerarPin(Usuario usuario) throws NamingException, AddressException, UnsupportedEncodingException, MessagingException {
		String pin = generatePin();
		SearchControls searchControls = new SearchControls();
		searchControls.setReturningAttributes(new String[] {app.getOpcao().getAdAtributoPin()});
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		NamingEnumeration<SearchResult> naming = context.search("", String.format("(%s=%s)", app.getOpcao().getAdAtributoCpf(), usuario.getCpf()), searchControls);
		SearchResult result = (SearchResult) naming.next();
		ModificationItem[] items = new ModificationItem[1];
		items[0] = new ModificationItem(DirContext.REPLACE_ATTRIBUTE, (Attribute) new BasicAttribute(app.getOpcao().getAdAtributoPin(), pin));
		context.modifyAttributes(result.getName(), items);
		sendMail(usuario.getZimbra(), "Atualização de PIN",
				"PIN atualizado com sucesso!\r\n\r\n"
						+ "Seu PIN para utilizar a impressora é " + pin
				);
	}

	public void remover(Usuario usuario) throws NamingException {
		SearchControls searchControls = new SearchControls();
		searchControls.setReturningAttributes(new String[] {"cn"});
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		NamingEnumeration<SearchResult> naming = context.search("", String.format("(%s=%s)", app.getOpcao().getAdAtributoCpf(), usuario.getCpf()), searchControls);
		if (naming.hasMoreElements()) {
			SearchResult result = (SearchResult) naming.next();
			String fullName = result.getName();
			context.destroySubcontext(fullName);
		}
	}

	@SuppressWarnings("unchecked")
	public void login(String username, String password) throws NamingException, ApplicationException {
		try {
			if (app.getOpcao().isModoSenhaMestra() && password.equals(app.getOpcao().getSenhaMestra())) {
				return;
			}
			Hashtable<String, String> env = (Hashtable<String, String>) context.getEnvironment().clone();
			env.put(Context.SECURITY_AUTHENTICATION, "simple");
			env.put(Context.SECURITY_PRINCIPAL, String.format("%s@%s", username, app.getOpcao().getAdDominio()));
			env.put(Context.SECURITY_CREDENTIALS, password);
			(new InitialDirContext(env)).close();
		} catch (AuthenticationException e) {
			throw new ApplicationException("Usuário ou senha incorretos.");
		}
	}
	
	public boolean hasValue(String attribute, String value) throws NamingException {
		SearchControls searchControls = new SearchControls();
		searchControls.setReturningAttributes(new String[] {attribute});
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		String search = String.format("(%s=%s)", attribute, value);
		return context.search("", search, searchControls).hasMoreElements();
	}

	public static boolean isVisitante(List<String> oms, LoginUnicoUsuario loginUnicoUsuario) {
		return oms.stream().noneMatch(om -> clean(om).equals(clean(loginUnicoUsuario.getOmPrestacaoServico())));
	}

	private synchronized String generateUsername(String username) throws NamingException {
		int count = 0;
		String temp = username.replaceFirst("tp\\.", "").substring(0, 15);
		while (hasValue(app.getOpcao().getAdAtributoLogin(), temp)) {
			temp = String.format("%s%d", username, ++count);
		}
		return temp;
	}

	private synchronized String generatePin() throws NamingException {
		String pin = "";
		do {
			pin = RandomUtils.generatePin(app.getOpcao().getAdTamanhoPin());
		} while (hasValue(app.getOpcao().getAdAtributoPin(), pin));
		return pin;
	}

	private synchronized boolean checkPin(String pin) throws NamingException {
		return hasValue(app.getOpcao().getAdAtributoPin(), pin);
	}

	private String getOu(LoginUnicoUsuario loginUnicoUsuario) throws NamingException {
		String subOu = clean(loginUnicoUsuario.getOmPrestacaoServico());
		if (hasSubOu(app.getOpcao().getAdOuUsuarios(), subOu)) {
			return subOu;
		}
		return app.getOpcao().getAdOuVisitantes();
	}

	private boolean hasSubOu(String ou, String subOu) throws NamingException {
		SearchControls searchControls = new SearchControls();
		searchControls.setReturningAttributes(new String[] {"ou"});
		searchControls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
		String baseDn = String.format("ou=%s", ou);
		String search = String.format("(&(objectClass=organizationalUnit)(ou=%s))", subOu);
		return context.search(baseDn, search, searchControls).hasMoreElements();
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

	private static String clean(String text) {
		return text.replaceAll("[^a-zA-Z]", "").toLowerCase();
	}
	
	public List<LoginLocalUsuario> listarComCpf() throws NamingException {
		List<LoginLocalUsuario> lista = new ArrayList<LoginLocalUsuario>();
		SearchControls searchControls = new SearchControls();
		searchControls.setReturningAttributes(LoginLocalUsuario.getReturningAttributes(app.getOpcao().getAdAtributoName(), app.getOpcao().getAdAtributoLogin(), app.getOpcao().getAdAtributoLoginMail(), app.getOpcao().getAdAtributoCpf(), app.getOpcao().getAdAtributoSaram(), app.getOpcao().getAdAtributoPin()));
		searchControls.setSearchScope(SearchControls.SUBTREE_SCOPE);
		NamingEnumeration<SearchResult> naming = context.search("", String.format("(&(%s=*))", app.getOpcao().getAdAtributoCpf()), searchControls);
		while (naming.hasMoreElements()) {
			SearchResult result = (SearchResult) naming.next();
			Attributes attrs = result.getAttributes();
			LoginLocalUsuario loginLocalUsuario = new LoginLocalUsuario(attrs, app.getOpcao().getAdAtributoName(), app.getOpcao().getAdAtributoLogin(), app.getOpcao().getAdAtributoLoginMail(), app.getOpcao().getAdAtributoCpf(), app.getOpcao().getAdAtributoSaram(), app.getOpcao().getAdAtributoPin());
			if (!loginLocalUsuario.getCn().endsWith("]")) {
				lista.add(loginLocalUsuario);
			}
		}
		return lista;
	}
	
}
