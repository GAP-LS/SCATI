package com.suchorski.scati.daos;

import java.util.Date;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import com.suchorski.scati.generics.GenericDAO;
import com.suchorski.scati.models.Aplicacao;
import com.suchorski.scati.models.Token;
import com.suchorski.scati.models.Token_;
import com.suchorski.scati.models.Usuario;
import com.suchorski.scati.utils.RandomUtils;

public class TokenDAO extends GenericDAO<Token, Long> {

	public TokenDAO() {
		super(Token.class);
	}
	
	public Token gerarToken(Usuario usuario, Aplicacao aplicacao) {
		Token token = usuario.getTokens().stream().filter(t -> t.getAplicacao().equals(aplicacao)).findFirst().orElse(new Token());
		if (token.getId() == 0) {
			token.setUsuario(usuario);
			token.setAplicacao(aplicacao);
			token.setToken(RandomUtils.generateCookie(2));
			token.setDataCriacao(new Date());
			save(token);
		} else {
			update(token);
		}
		return token;
	}
	
	public Token findByToken(String token) throws NoResultException {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Token> criteriaQuery = criteriaBuilder.createQuery(Token.class);
		Root<Token> root = criteriaQuery.from(Token.class);
		ParameterExpression<String> parameterExpression = criteriaBuilder.parameter(String.class);
		criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(Token_.token), parameterExpression));
		TypedQuery<Token> typedQuery = getEntityManager().createQuery(criteriaQuery);
		typedQuery.setParameter(parameterExpression, token);
		return typedQuery.getSingleResult();
	}
	
	public void remover(Token token) {
		deleteById(token.getId());
	}
	
}
