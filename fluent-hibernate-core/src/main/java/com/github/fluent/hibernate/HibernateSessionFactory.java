package com.github.fluent.hibernate;

import java.io.File;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

import com.github.fluent.hibernate.util.InternalUtils;

/**
 * This class holds a Hibernate session factory. Use {@link HibernateSessionFactory.Builder} to
 * create a session factory.
 *
 * @author V.Ladynev
 */
public final class HibernateSessionFactory {

    /** Session factory. */
    private static volatile SessionFactory sessionFactory;

    private HibernateSessionFactory() {

    }

    /**
     * Destroy {@link SessionFactory} and release all resources (caches, connection pools, etc).
     */
    public static synchronized void closeSessionFactory() {
        if (sessionFactory != null) {
            sessionFactory.close();
            sessionFactory = null;
        }
    }

    public static <T> T doInTransaction(IRequest<T> request) {
        Transaction txn = null;
        Session session = openSession();

        T result = null;

        try {
            txn = session.beginTransaction();
            result = request.doInTransaction(session);
            txn.commit();
        } catch (Throwable th) {
            InternalUtils.HibernateUtils.rollback(txn);
            throw new RuntimeException(th);
        } finally {
            InternalUtils.HibernateUtils.close(session);
        }

        return result;
    }

    /**
     * It is need to use for only simply persisten objects. It can work incorrect for associations.
     *
     * @return result of request
     */
    public static <T> T doInStatlessTransaction(IStatelessRequest<T> request) {
        Transaction txn = null;
        StatelessSession session = openStatelessSession();

        T result = null;

        try {
            txn = session.beginTransaction();
            result = request.doInTransaction(session);
            txn.commit();
        } catch (Throwable th) {
            InternalUtils.HibernateUtils.rollback(txn);
            throw new RuntimeException(th);
        } finally {
            InternalUtils.HibernateUtils.close(session);
        }

        return result;
    }

    /**
     * Open a {@link Session}.
     *
     * @return the created session
     */
    private static Session openSession() {
        assertSessionFactory();
        return sessionFactory.openSession();
    }

    /**
     * Open a new stateless session.
     *
     * @return the created stateless session
     */
    private static StatelessSession openStatelessSession() {
        assertSessionFactory();
        return sessionFactory.openStatelessSession();
    }

    private static void assertSessionFactory() {
        if (sessionFactory == null) {
            throw new IllegalStateException(
                    "Firstly create a session factory with HibernateSessionFactory.Builder");
        }
    }

    private static synchronized void createSessionFactory(ServiceRegistry serviceRegistry) {
        if (sessionFactory != null) {
            return;
        }

        try {
            sessionFactory = new Configuration().buildSessionFactory(serviceRegistry);
        } catch (Throwable th) {
            StandardServiceRegistryBuilder.destroy(serviceRegistry);
            throw new RuntimeException(th);
        }
    }

    private static synchronized void setExistingSessionFactory(SessionFactory sessionFactory) {
        closeSessionFactory();
        HibernateSessionFactory.sessionFactory = sessionFactory;
    }

    /**
     * Fluent API for session factory configuration and build. The simplest way to create a session
     * factory:
     *
     * <code>
     * HibernateSessionFactory.Builder.configureFromDefaultHibernateCfgXml().createSessionFactory();
     * </code>
     *
     * @author V.Ladynev
     */
    public static final class Builder {

        private final StandardServiceRegistryBuilder registryBuilder;

        private Builder(StandardServiceRegistryBuilder registryBuilder) {
            this.registryBuilder = registryBuilder;
        }

        public static Builder configureFromDefaultHibernateCfgXml() {
            return new Builder(new StandardServiceRegistryBuilder().configure());
        }

        public static Builder configureFromHibernateCfgXml(String hibernateCfgXml) {
            return new Builder(new StandardServiceRegistryBuilder().configure(hibernateCfgXml));
        }

        public static void configureFromExistingSessionFactory(SessionFactory sessionFactory) {
            HibernateSessionFactory.setExistingSessionFactory(sessionFactory);
        }

        public Builder loadHibernatePropertiesFromFile(File pathToPropertiesFile) {
            registryBuilder.loadProperties(pathToPropertiesFile);
            return this;
        }

        public Builder loadHibernatePropertiesFromClassPathResource(String classPathResourceName) {
            registryBuilder.loadProperties(classPathResourceName);
            return this;
        }

        public void createSessionFactory() {
            HibernateSessionFactory.createSessionFactory(registryBuilder.build());
        }

    }

}
