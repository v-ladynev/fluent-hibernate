package com.github.fluent.hibernate;

import java.util.List;

import org.hibernate.Session;

/**
 * @author DoubleF1re
 * @author V.Ladynev
 */
class HibernateObjectQuery<T> {

    @SuppressWarnings("unchecked")
    public static <T> T save(final T entity) {
        return HibernateSessionFactory.doInTransaction(new IRequest<T>() {
            @Override
            public T doInTransaction(Session session) {
                return (T) session.save(entity);
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

    public static <T> void saveAll(final List<T> entities) {
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

    public static <T> void saveOrUpdateAll(final List<T> entities) {
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

}
