package com.suchorski.scati.services;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.jboss.logging.Logger;

public class HibernateService {
	
	private static final Logger logger = Logger.getLogger(HibernateService.class);
	
	private static final String DB_NAME = "scati";
	private static StandardServiceRegistry registry;
	private static EntityManagerFactory entityManagerFactory;
	
	public static void startup() {
		if (entityManagerFactory == null) {
			logger.debug("Starting up Hibernate Factory");
			registry = new StandardServiceRegistryBuilder().configure().build();
			entityManagerFactory = Persistence.createEntityManagerFactory(DB_NAME);
		}
	}
	
	public static void shutdown() {
		logger.debug("Shutting down up Hibernate Factory");
		if (entityManagerFactory != null) {
			entityManagerFactory.close();
			StandardServiceRegistryBuilder.destroy(registry);
			entityManagerFactory = null;
			registry = null;
		}
	}
	
	public static EntityManager createEntityManager() {
		startup();
		return entityManagerFactory.createEntityManager();
	}

}
