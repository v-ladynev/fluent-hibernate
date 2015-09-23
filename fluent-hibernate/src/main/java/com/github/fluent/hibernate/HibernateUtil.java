package com.github.fluent.hibernate;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

/**
 *
 * @author V.Ladynev
 */
public final class HibernateUtil {

    private HibernateUtil() {

    }

    public static void rollback(Transaction txn) {
        if (txn != null) {
            txn.rollback();
        }
    }

    public static void close(Session session) {
        if (session != null && session.isOpen()) {
            session.close();
        }
    }

    public static void close(StatelessSession session) {
        if (session != null) {
            session.close();
        }
    }

}
