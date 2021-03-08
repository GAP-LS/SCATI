package com.suchorski.scati.daos;

import java.util.Date;
import java.util.List;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import com.suchorski.scati.generics.GenericDAO;
import com.suchorski.scati.models.Aplicacao;
import com.suchorski.scati.models.Aplicacao_;
import com.suchorski.scati.models.Usuario;
import com.suchorski.scati.utils.RandomUtils;

public class AplicacaoDAO extends GenericDAO<Aplicacao, Long> {

	public AplicacaoDAO() {
		super(Aplicacao.class);
	}
	
	public Aplicacao findByChavePublica(String chavePublica) throws NoResultException {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Aplicacao> criteriaQuery = criteriaBuilder.createQuery(Aplicacao.class);
		Root<Aplicacao> root = criteriaQuery.from(Aplicacao.class);
		ParameterExpression<String> parameterExpression = criteriaBuilder.parameter(String.class);
		criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(Aplicacao_.chavePublica), parameterExpression));
		TypedQuery<Aplicacao> typedQuery = getEntityManager().createQuery(criteriaQuery);
		typedQuery.setParameter(parameterExpression, chavePublica);
		return typedQuery.getSingleResult();
	}
	
	@Override
	public List<Aplicacao> list() {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Aplicacao> criteriaQuery = criteriaBuilder.createQuery(Aplicacao.class);
		Root<Aplicacao> root = criteriaQuery.from(Aplicacao.class);
		criteriaQuery.select(root).orderBy(criteriaBuilder.asc(root.get(Aplicacao_.nome)));
		TypedQuery<Aplicacao> typedQuery = getEntityManager().createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}
	
	public void adicionar(String nome, String sigla, Usuario usuario) {
		Aplicacao aplicacao = new Aplicacao();
		aplicacao.setNome(nome);
		aplicacao.setSigla(sigla);
		aplicacao.setUsuario(usuario);
		aplicacao.setChavePublica(RandomUtils.generateCookie(1));
		aplicacao.setChaveSecreta(RandomUtils.generateCookie(4));
		aplicacao.setDataCriacao(new Date());
		save(aplicacao);
	}
	
	public void remover(Aplicacao aplicacao) {
		deleteById(aplicacao.getId());
	}
	
}
