package com.github.fluent.hibernate.request;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;

import com.github.fluent.hibernate.IRequest;
import com.github.fluent.hibernate.cfg.HibernateSessionFactory;
import com.github.fluent.hibernate.internal.util.InternalUtils;
import com.github.fluent.hibernate.transformer.FluentHibernateResultTransformer;

/**
 * @param <T>
 *            type of return value.
 *
 * @author DoubleF1re
 * @author V.Ladynev
 */
public final class HibernateSqlRequest<T> {

    private final String query;

    private final HibernateQueryParameters params = new HibernateQueryParameters();

    private int maxResults;

    private Class<?> transformToClass;

    private HibernateSqlRequest(String query) {
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
    public HibernateSqlRequest<T> p(String name, Object val) {
        params.add(name, val);
        return this;
    }

    public HibernateSqlRequest<T> p(String name, Object... vals) {
        params.add(name, Arrays.asList(vals));
        return this;
    }

    public HibernateSqlRequest<T> maxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    // TODO transformer not works for pid a nested fields
    public HibernateSqlRequest<T> transform(Class<?> clazz) {
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
            hibernateQuery
                    .setResultTransformer(new FluentHibernateResultTransformer(transformToClass));
        }

        return hibernateQuery;
    }

    // TODO may be return long?
    public int count() {
        Number result = HibernateSessionFactory.doInTransaction(new IRequest<Number>() {
            @Override
            public Number doInTransaction(Session session) {
                return (Number) createHibernateQuery(session).uniqueResult();
            }
        });

        return result == null ? 0 : result.intValue();
    }

    private Query createHibernateQuery(Session session) {
        SQLQuery result = session.createSQLQuery(query);
        params.setParametersToQuery(result);
        return result;
    }

    public static <T> HibernateSqlRequest<T> create(String query) {
        return new HibernateSqlRequest<T>(query);
    }

}
