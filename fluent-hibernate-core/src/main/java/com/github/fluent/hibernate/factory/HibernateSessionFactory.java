package com.github.fluent.hibernate.factory;

import java.io.File;
import java.util.Properties;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.cfg.AvailableSettings;

import com.github.fluent.hibernate.IRequest;
import com.github.fluent.hibernate.IStatelessRequest;
import com.github.fluent.hibernate.internal.util.InternalUtils;

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
            throw InternalUtils.toRuntimeException(th);
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

    private static synchronized void setExistingSessionFactory(SessionFactory sessionFactory) {
        closeSessionFactory();
        HibernateSessionFactory.sessionFactory = sessionFactory;
    }

    /**
     * Fluent API for a session factory configuration and build. The simplest way to create a
     * session factory:
     *
     * <code>
     * HibernateSessionFactory.Builder.configureFromDefaultHibernateCfgXml().createSessionFactory();
     * </code>
     *
     * @author V.Ladynev
     */
    public static final class Builder {

        private final ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

        private final Properties properties = new Properties();

        private Builder() {

        }

        public static Builder configureFromDefaultHibernateCfgXml() {
            return configureFromHibernateCfgXml(null);
        }

        public static Builder configureFromHibernateCfgXml(String hibernateCfgXml) {
            Builder result = new Builder();
            result.configurationBuilder.configure(hibernateCfgXml);
            return result;
        }

        public static Builder configureWithoutHibernateCfgXml() {
            return new Builder();
        }

        public Builder annotatedClasses(Class<?>... annotatedClasses) {
            configurationBuilder.addAnnotatedClasses(annotatedClasses);
            return this;
        }

        public Builder packagesToScan(String... packagesToScan) {
            configurationBuilder.addPackagesToScan(packagesToScan);
            return this;
        }

        public static void configureFromExistingSessionFactory(SessionFactory sessionFactory) {
            HibernateSessionFactory.setExistingSessionFactory(sessionFactory);
        }

        public Builder addHibernatePropertiesFromFile(File pathToPropertiesFile) {
            configurationBuilder.addPropertiesFromFile(pathToPropertiesFile);
            return this;
        }

        public Builder addHibernatePropertiesFromClassPathResource(String classPathResourceName) {
            configurationBuilder.addPropertiesFromClassPath(classPathResourceName);
            return this;
        }

        public Builder connectionUrl(String connectionUrl) {
            properties.put(AvailableSettings.URL, connectionUrl);
            return this;
        }

        public Builder userName(String userName) {
            properties.put(AvailableSettings.USER, userName);
            return this;
        }

        public Builder password(String password) {
            properties.put(AvailableSettings.PASS, password);
            return this;
        }

        public void createSessionFactory() {
            configurationBuilder.addProperties(properties);
            setExistingSessionFactory(configurationBuilder.buildSessionFactory());
        }

    }

}
