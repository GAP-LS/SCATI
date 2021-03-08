package com.suchorski.scati.daos;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import com.suchorski.scati.generics.GenericDAO;
import com.suchorski.scati.models.Patente;
import com.suchorski.scati.models.Patente_;

public class PatenteDAO extends GenericDAO<Patente, Long> {

	public PatenteDAO() {
		super(Patente.class);
	}
	
	public Patente findBySigla(String sigla) throws NoResultException {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Patente> criteriaQuery = criteriaBuilder.createQuery(Patente.class);
		Root<Patente> root = criteriaQuery.from(Patente.class);
		ParameterExpression<String> parameterExpression = criteriaBuilder.parameter(String.class);
		criteriaQuery.select(root).where(criteriaBuilder.equal(root.get(Patente_.sigla), parameterExpression));
		TypedQuery<Patente> typedQuery = getEntityManager().createQuery(criteriaQuery);
		typedQuery.setParameter(parameterExpression, sigla);
		return typedQuery.getSingleResult();
	}
	
}
