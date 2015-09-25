package com.github.fluent.hibernate;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import com.github.fluent.hibernate.util.InternalUtils;

/**
 * @param <T>
 *            type of return value.
 *
 * @author DoubleF1re
 * @author V.Ladynev
 */
public final class HibernateHqlRequest<T> {

    private final String query;

    private final HibernateQueryParameters params = new HibernateQueryParameters();

    private int maxResults;

    private Class<?> transformToClass;

    private HibernateHqlRequest(String query) {
        this.query = query;
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
        params.add(name, val);
        return this;
    }

    public HibernateHqlRequest<T> p(String name, Object... vals) {
        params.add(name, Arrays.asList(vals));
        return this;
    }

    public HibernateHqlRequest<T> maxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    // TODO transformer not works for pid a nested fields
    public HibernateHqlRequest<T> transform(Class<?> clazz) {
        transformToClass = clazz;
        return this;
    }

    public T first() {
        return InternalUtils.CollectionUtils.first(list());
    }

    public List<T> list() {
        return HibernateSessionFactory.doInTransaction(new IRequest<List<T>>() {
            @Override
            public List<T> doInTransaction(Session session) {
                return tuneForSelect(createHibernateQuery(session)).list();
            }
        });
    }

    private Query tuneForSelect(Query hibernateQuery) {
        if (maxResults != 0) {
            hibernateQuery.setMaxResults(maxResults);
        }

        if (transformToClass != null) {
            hibernateQuery.setResultTransformer(new FluentHibernateResultTransformer(
                    transformToClass));
        }

        return hibernateQuery;
    }

    // TODO may be return long?
    public int count() {
        Number result = HibernateSessionFactory.doInTransaction(new IRequest<Number>() {
            @Override
            public Number doInTransaction(Session session) {
                return (Number) createHibernateQuery(session).iterate().next();
            }
        });

        return result == null ? 0 : result.intValue();
    }

    private Query createHibernateQuery(Session session) {
        Query result = session.createQuery(query);
        params.setParametersToQuery(result);
        return result;
    }

    public static <T> HibernateHqlRequest<T> create(String query) {
        return new HibernateHqlRequest<T>(query);
    }

}
