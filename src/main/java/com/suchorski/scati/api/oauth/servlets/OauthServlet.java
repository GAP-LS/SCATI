package com.suchorski.scati.api.oauth.servlets;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;

import javax.inject.Inject;
import javax.persistence.NoResultException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.adminfaces.template.exception.AccessDeniedException;
import com.suchorski.scati.controllers.AplicacaoController;
import com.suchorski.scati.controllers.SessaoController;
import com.suchorski.scati.daos.AplicacaoDAO;
import com.suchorski.scati.daos.TokenDAO;
import com.suchorski.scati.exceptions.BadRequestException;
import com.suchorski.scati.models.Aplicacao;
import com.suchorski.scati.models.Token;

@WebServlet("/oauth/v2/login")
public class OauthServlet extends HttpServlet {

	private static final long serialVersionUID = 1234244645991222650L;

	@Inject private AplicacaoController app;
	@Inject private SessaoController sessao;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try (AplicacaoDAO aplicacaoDAO = new AplicacaoDAO(); TokenDAO tokenDAO = new TokenDAO()) {
			String responseType = request.getParameter("response_type");
			String clientId = request.getParameter("client_id");
			String redirectUri = request.getParameter("redirect_uri");
			String scope = request.getParameter("scope");
			String state = request.getParameter("state");
			try {
				if (Arrays.asList(responseType, clientId, redirectUri, scope, state).stream().anyMatch(s -> s == null)) {
					throw new BadRequestException("Requisição inválida.");
				}
				if (!"code".equalsIgnoreCase(responseType)) {
					throw new BadRequestException("Tipo de resposta inválida.");
				}
				Aplicacao aplicacao = aplicacaoDAO.findByChavePublica(clientId);
				if (sessao.isLoggedIn()) {
					if (sessao.getUsuarioAtualizado().hasPerfil(scope) || "none".equals(scope)) {
						Token token = tokenDAO.gerarToken(sessao.getUsuario(), aplicacao);
						response.sendRedirect(String.format("%s?code=%s&state=%s", redirectUri, token.getToken(), state));
					} else {
						throw new AccessDeniedException(String.format("Usuário precisa do perfil: %s.", scope.toUpperCase()));
					}
				} else {
					String redirect = URLEncoder.encode(String.format("/%s/oauth/v2/login?response_type=%s&client_id=%s&redirect_uri=%s&scope=%s&state=%s", app.getContext(), responseType, clientId, redirectUri, scope, state), "UTF-8");
					response.sendRedirect(String.format("/%s/login.xhtml?redir=%s", app.getContext(), redirect));
				}
			} catch (NoResultException e) {
				response.sendRedirect("404.xhtml");
			}
		}
	}

}
