package com.github.fluent.hibernate.cfg;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

import com.github.fluent.hibernate.IRequest;
import com.github.fluent.hibernate.IStatelessRequest;
import com.github.fluent.hibernate.internal.util.InternalUtils;

/**
 * This class holds a Hibernate session factory. The simplest way to create a session factory:
 *
 * <code>
 * Fluent.factory().build();
 * </code> <br>
 * Don't forget to destroy it with
 *
 * <code>
 * Fluent.factory().close();
 * </code>
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
    static synchronized void closeSessionFactory() {
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
            rollback(txn);
            throw InternalUtils.toRuntimeException(th);
        } finally {
            close(session);
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
            rollback(txn);
            throw new RuntimeException(th);
        } finally {
            close(session);
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
                    "Firstly create a session factory with Fluent.factory().build()");
        }
    }

    static synchronized void setExistingSessionFactory(SessionFactory sessionFactory) {
        closeSessionFactory();
        HibernateSessionFactory.sessionFactory = sessionFactory;
    }

    private static void rollback(Transaction txn) {
        if (txn != null) {
            txn.rollback();
        }
    }

    private static void close(Session session) {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    private static void close(StatelessSession session) {
        if (session != null) {
            session.close();
        }
    }

}
