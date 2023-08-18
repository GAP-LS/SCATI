package com.suchorski.scati.daos;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Root;

import com.suchorski.scati.generics.GenericDAO;
import com.suchorski.scati.models.Aplicacao;
import com.suchorski.scati.models.Patente;
import com.suchorski.scati.models.Patente_;
import com.suchorski.scati.models.Perfil;
import com.suchorski.scati.models.Perfil_;
import com.suchorski.scati.models.TipoPatente;
import com.suchorski.scati.models.TipoPatente_;
import com.suchorski.scati.models.Usuario;
import com.suchorski.scati.models.Usuario_;

public class UsuarioDAO extends GenericDAO<Usuario, Long> {

	public UsuarioDAO() {
		super(Usuario.class);
	}
	
	public Usuario findByCpf(String cpf) throws NoResultException {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
		Root<Usuario> root = criteriaQuery.from(Usuario.class);
		ParameterExpression<String> parameterExpression = criteriaBuilder.parameter(String.class);
		criteriaQuery.select(root).where(
				criteriaBuilder.and(
						criteriaBuilder.equal(root.get(Usuario_.cpf), parameterExpression),
						criteriaBuilder.isNull(root.get(Usuario_.dataRemocao))
				));
		TypedQuery<Usuario> typedQuery = getEntityManager().createQuery(criteriaQuery);
		typedQuery.setParameter(parameterExpression, cpf);
		return typedQuery.getSingleResult();
	}

	public boolean hasPending(String cpf) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
		Root<Usuario> root = criteriaQuery.from(Usuario.class);
		ParameterExpression<String> parameterExpression = criteriaBuilder.parameter(String.class);
		criteriaQuery.select(root).where(
				criteriaBuilder.and(
						criteriaBuilder.equal(root.get(Usuario_.cpf), parameterExpression),
						criteriaBuilder.isNull(root.get(Usuario_.dataInsercao)),
						criteriaBuilder.isNull(root.get(Usuario_.dataRemocao))
						));
		TypedQuery<Usuario> typedQuery = getEntityManager().createQuery(criteriaQuery);
		typedQuery.setParameter(parameterExpression, cpf);
		return typedQuery.getResultList().isEmpty();
	}

	public List<Usuario> listAtivos() {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
		Root<Usuario> root = criteriaQuery.from(Usuario.class);
		Join<Usuario, Patente> join1 = root.join(Usuario_.patente, JoinType.LEFT);
		criteriaQuery.select(root).where(criteriaBuilder.and(
				criteriaBuilder.isNotNull(root.get(Usuario_.dataInsercao)),
				criteriaBuilder.isNull(root.get(Usuario_.dataRemocao))))
		.orderBy(
				criteriaBuilder.asc(join1.get(Patente_.ordem)),
				criteriaBuilder.asc(root.get(Usuario_.nomeGuerra))
				);
		TypedQuery<Usuario> typedQuery = getEntityManager().createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}
	

	public List<Usuario> listaHistorico(String cpf) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
		Root<Usuario> root = criteriaQuery.from(Usuario.class);
		ParameterExpression<String> parameterExpression = criteriaBuilder.parameter(String.class);
		criteriaQuery.select(root).where(criteriaBuilder.and(
				criteriaBuilder.equal(root.get(Usuario_.cpf), parameterExpression),
				criteriaBuilder.isNotNull(root.get(Usuario_.dataRemocao))))
		.orderBy(criteriaBuilder.desc(root.get(Usuario_.dataRemocao)));
		TypedQuery<Usuario> typedQuery = getEntityManager().createQuery(criteriaQuery);
		typedQuery.setParameter(parameterExpression, cpf);
		return typedQuery.getResultList();
	}

	public List<Usuario> listModeradores(Patente p) {
		getEntityManager().getTransaction().begin();
		List<Usuario> moderadores = moderadores(getEntityManager(), p);
		if (moderadores.isEmpty()) {
			moderadores = developers(getEntityManager());
		}
		getEntityManager().getTransaction().commit();
		getEntityManager().close();
		if (moderadores.isEmpty()) {
			throw new RuntimeException("Nenhum moderador encontrado. Contate a seção de informática");
		}
		return moderadores;
	}

	private List<Usuario> moderadores(EntityManager entityManager, Patente p) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
		Root<Usuario> root = criteriaQuery.from(Usuario.class);
		Join<Usuario, Patente> join1 = root.join(Usuario_.patente, JoinType.LEFT);
		Join<Patente, TipoPatente> join2 = join1.join(Patente_.tipoPatente, JoinType.LEFT);
		ParameterExpression<Integer> parameterExpression1 = criteriaBuilder.parameter(Integer.class);
		ParameterExpression<Integer> parameterExpression2 = criteriaBuilder.parameter(Integer.class);
		ParameterExpression<Boolean> parameterExpression3 = criteriaBuilder.parameter(Boolean.class);
		criteriaQuery.select(root).where(criteriaBuilder.and(
				criteriaBuilder.equal(join2.get(TipoPatente_.ordem), parameterExpression1),
				criteriaBuilder.le(join1.get(Patente_.ordem), parameterExpression2),
				criteriaBuilder.isNotNull(root.get(Usuario_.dataInsercao)),
				criteriaBuilder.equal(root.get(Usuario_.bloqueado), parameterExpression3)))
		.orderBy(
				criteriaBuilder.asc(join1.get(Patente_.ordem)),
				criteriaBuilder.asc(root.get(Usuario_.nomeGuerra))
				);
		TypedQuery<Usuario> typedQuery = entityManager.createQuery(criteriaQuery);
		typedQuery.setParameter(parameterExpression1, 10); // Oficial
		typedQuery.setParameter(parameterExpression2, p.getOrdem()); // Patente maior ou igual
		typedQuery.setParameter(parameterExpression3, false); // Não está bloqueado
		return typedQuery.getResultList();
	}

	private List<Usuario> developers(EntityManager entityManager) {
		CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
		Root<Usuario> root = criteriaQuery.from(Usuario.class);
		Join<Usuario, Perfil> join = root.join(Usuario_.perfils, JoinType.LEFT);
		ParameterExpression<String> parameterExpression = criteriaBuilder.parameter(String.class);
		criteriaQuery.select(root).where(criteriaBuilder.equal(join.get(Perfil_.codigo), parameterExpression)).orderBy(criteriaBuilder.asc(root.get(Usuario_.nomeGuerra)));
		TypedQuery<Usuario> typedQuery = entityManager.createQuery(criteriaQuery);
		typedQuery.setParameter(parameterExpression, "DEV");
		return typedQuery.getResultList();
	}
	
	public List<Usuario> listNaoModerados() {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
		Root<Usuario> root = criteriaQuery.from(Usuario.class);
		criteriaQuery.select(root).where(criteriaBuilder.and(
				criteriaBuilder.isNull(root.get(Usuario_.dataInsercao)),
				criteriaBuilder.isNotNull(root.get(Usuario_.moderador))))
		.orderBy(criteriaBuilder.asc(root.get(Usuario_.nomeGuerra)));
		TypedQuery<Usuario> typedQuery = getEntityManager().createQuery(criteriaQuery);
		return typedQuery.getResultList();
	}

	public List<Usuario> listNaoFinalizados(Usuario u, boolean visitantes) {
		CriteriaBuilder criteriaBuilder = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Usuario> criteriaQuery = criteriaBuilder.createQuery(Usuario.class);
		Root<Usuario> root = criteriaQuery.from(Usuario.class);
		ParameterExpression<Long> parameterExpression1 = criteriaBuilder.parameter(Long.class);
		ParameterExpression<Boolean> parameterExpression2 = criteriaBuilder.parameter(Boolean.class);
		criteriaQuery.select(root).where(criteriaBuilder.and(
				criteriaBuilder.notEqual(root.get(Usuario_.id), parameterExpression1),
				criteriaBuilder.isNotNull(root.get(Usuario_.dataInsercao)),
				criteriaBuilder.isNull(root.get(Usuario_.dataRemocao)),
				criteriaBuilder.equal(root.get(Usuario_.visitante), parameterExpression2)))
		.orderBy(criteriaBuilder.asc(root.get(Usuario_.nomeGuerra)));
		TypedQuery<Usuario> typedQuery = getEntityManager().createQuery(criteriaQuery);
		typedQuery.setParameter(parameterExpression1, u.getId());
		typedQuery.setParameter(parameterExpression2, visitantes);
		return typedQuery.getResultList();
	}
	
	public void atualizar(Usuario atualizado, boolean isVisitante) {
		atualizado.setVisitante(isVisitante);
		update(atualizado);
	}
	
	public void liberar(Usuario moderado, Usuario moderador) {
		moderado.setModerador(moderador);
		moderado.setDataInsercao(new Date());
		update(moderado);
	}

	public void finalizar(Usuario finalizado, Usuario finalizador) {
		finalizado.setFinalizador(finalizador);
		finalizado.setDataRemocao(new Date());
		update(finalizado);
	}
	
	public void updatePerfis(Usuario usuario, List<Perfil> revogar, List<Perfil> autorizar) {
		Set<Perfil> perfis = usuario.getPerfils();
		revogar.forEach(p -> perfis.remove(p));
		autorizar.forEach(p -> perfis.add(p));
		update(usuario);
	}
	
	public void updateAplicacaosGerenciadas(Usuario usuario, List<Aplicacao> revogar, List<Aplicacao> autorizar) {
		Set<Aplicacao> apps = usuario.getAplicacaosGerenciadas();
		revogar.forEach(p -> apps.remove(p));
		autorizar.forEach(p -> apps.add(p));
		update(usuario);
	}
	
}
