package com.github.fluent.hibernate;

import java.util.List;

/**
 * Пространство имён для работы с Hibernate.
 *
 * @author DoubleF1re
 * @author V.Ladynev
 */
public final class H {

    private H() {

    }

    public static <T> HibernateRequest<T> request(Class<?> clazz) {
        return HibernateRequest.<T> create(clazz);
    }

    public static <T> HibernateHqlRequest<T> request(String query) {
        return HibernateHqlRequest.<T> create(query);
    }

    public static HibernateUpdate update(String updateQuery) {
        return HibernateUpdate.create(updateQuery);
    }

    public static <T> T getById(Class<T> clazz, Object id) {
        return H.<T> request(clazz).idEq(id).first();
    }

    public static <T> T saveOrUpdate(T entity) {
        return HibernateObjectQuery.saveOrUpdate(entity);
    }

    public static <T> void saveOrUpdateAll(List<T> entities) {
        HibernateObjectQuery.saveOrUpdateAll(entities);
    }

    public static <T> void saveAll(List<T> entities) {
        HibernateObjectQuery.saveAll(entities);
    }

    public static <T> void delete(T entity) {
        HibernateObjectQuery.delete(entity);
    }

    public static <T> void deleteById(Class<T> clazz, Object id) {
        HibernateObjectQuery.delete(getById(clazz, id));
    }

    // TODO добавить выполненние IRequest

}
