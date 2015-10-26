package com.github.fluent.hibernate.request;

import org.hibernate.Session;

import com.github.fluent.hibernate.IRequest;
import com.github.fluent.hibernate.factory.HibernateSessionFactory;

/**
 * @author DoubleF1re
 * @author V.Ladynev
 */
public class HibernateObjectQuery<T> {

    public static <T> T save(final T entity) {
        return HibernateSessionFactory.doInTransaction(new IRequest<T>() {
            @Override
            public T doInTransaction(Session session) {
                session.save(entity);
                return entity;
            }
        });
    }

    public static <T> void saveOrUpdate(final T entity) {
        HibernateSessionFactory.doInTransaction(new IRequest<Void>() {
            @Override
            public Void doInTransaction(Session session) {
                session.saveOrUpdate(entity);
                return null;
            }
        });
    }

    public static <T> void saveAll(final Iterable<T> entities) {
        HibernateSessionFactory.doInTransaction(new IRequest<Void>() {
            @Override
            public Void doInTransaction(Session session) {
                for (T entity : entities) {
                    session.save(entity);
                }
                return null;
            }
        });
    }

    public static <T> void saveOrUpdateAll(final Iterable<T> entities) {
        HibernateSessionFactory.doInTransaction(new IRequest<Void>() {
            @Override
            public Void doInTransaction(Session session) {
                // TODO need batch update
                for (T entity : entities) {
                    session.saveOrUpdate(entity);
                }
                return null;
            }
        });
    }

    public static <T> void delete(final T entity) {
        HibernateSessionFactory.doInTransaction(new IRequest<Void>() {
            @Override
            public Void doInTransaction(Session session) {
                session.delete(entity);
                return null;
            }
        });
    }

    public static <T> void deleteAll(final Iterable<T> entities) {
        HibernateSessionFactory.doInTransaction(new IRequest<Void>() {
            @Override
            public Void doInTransaction(Session session) {
                for (T entity : entities) {
                    session.delete(entity);
                }
                return null;
            }
        });
    }

}
