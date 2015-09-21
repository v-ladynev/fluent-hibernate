package com.github.fluent.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

/**
 *
 * @author V.Ladynev
 */
public class HibernateSessionFactory {

    /** Session factory. */
    private static volatile SessionFactory sessionFactory;

    public static Session openSession() {
        assertSessionFactory();
        return sessionFactory.openSession();
    }

    private static StatelessSession openStatelessSession() {
        assertSessionFactory();
        return sessionFactory.openStatelessSession();
    }

    private static void assertSessionFactory() {
        if (sessionFactory == null) {
            throw new IllegalStateException("Create session factory first");
        }
    }

    public synchronized static void createSessionFactory(String hibernateCfgXml) {
        if (sessionFactory != null) {
            return;
        }

        Configuration ac = new Configuration().configure(hibernateCfgXml);
        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                ac.getProperties()).build();
        sessionFactory = ac.buildSessionFactory(serviceRegistry);
    }

    public synchronized static void closeSessionFactory() {
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

}
