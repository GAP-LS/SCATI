package com.suchorski.scati.api.oauth.servlets;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.suchorski.scati.controllers.AplicacaoController;
import com.suchorski.scati.controllers.SessaoController;
import com.suchorski.scati.daos.TokenDAO;

@WebServlet("/oauth/v2/logoff")
public class LogoffServlet extends HttpServlet {

	private static final long serialVersionUID = 7614910112782423921L;

	@Inject private AplicacaoController app;
	@Inject private SessaoController sessao;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String token = request.getParameter("token");
		try (TokenDAO tokenDAO = new TokenDAO()) {
			if (token != null && !token.isEmpty()) {
				tokenDAO.delete(tokenDAO.findByToken(token));
				sessao.logoff();
			}
		}
		response.sendRedirect(String.format("/%s/login.xhtml", app.getContext()));
	}

}
