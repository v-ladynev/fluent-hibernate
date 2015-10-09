package com.github.fluent.hibernate;

import java.util.List;

import com.github.fluent.hibernate.request.HibernateHqlRequest;
import com.github.fluent.hibernate.request.HibernateObjectQuery;
import com.github.fluent.hibernate.request.HibernateRequest;
import com.github.fluent.hibernate.request.HibernateUpdate;

/**
 * Hibernate fluent API entry point.
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

    public static <T> T save(T entity) {
        return HibernateObjectQuery.save(entity);
    }

    public static <T> void saveOrUpdate(T entity) {
        HibernateObjectQuery.saveOrUpdate(entity);
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

    // TODO add invoking IRequest

}
