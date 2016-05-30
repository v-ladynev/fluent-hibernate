package com.github.fluent.hibernate.request;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;

import com.github.fluent.hibernate.request.HibernateQuery.IQueryFactory;

/**
 * @param <T>
 *            type of return value.
 *
 * @author DoubleF1re
 * @author V.Ladynev
 */
public final class HibernateHqlRequest<T> {

    private final HibernateQuery<T> query;

    private HibernateHqlRequest(String query) {
        this.query = createQuery(query);
    }

    private HibernateQuery<T> createQuery(final String query) {
        return new HibernateQuery<T>(new IQueryFactory() {
            @Override
            public Query create(Session session) {
                return session.createQuery(query);
            }
        });
    }

    /**
     * Add a named query parameter.
     *
     * @param name
     *            name of parameter
     * @param val
     *            parameter value
     */
    public HibernateHqlRequest<T> p(String name, Object val) {
        query.p(name, val);
        return this;
    }

    public HibernateHqlRequest<T> p(String name, Object... vals) {
        query.p(name, Arrays.asList(vals));
        return this;
    }

    public HibernateHqlRequest<T> maxResults(int maxResults) {
        query.maxResults(maxResults);
        return this;
    }

    // TODO transformer not works for pid a nested fields
    public HibernateHqlRequest<T> transform(Class<?> clazz) {
        query.transform(clazz);
        return this;
    }

    public HibernateHqlRequest<T> useTransformer(ResultTransformer transformer) {
        query.useTransformer(transformer);
        return this;
    }

    public T first() {
        return query.first();
    }

    public List<T> list() {
        return query.list();
    }

    // TODO may be return long?
    public int count() {
        return query.count();
    }

    public static <T> HibernateHqlRequest<T> create(String query) {
        return new HibernateHqlRequest<T>(query);
    }

}
