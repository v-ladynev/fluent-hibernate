package com.github.fluent.hibernate.request;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;

import com.github.fluent.hibernate.IRequest;
import com.github.fluent.hibernate.cfg.HibernateSessionFactory;
import com.github.fluent.hibernate.internal.util.InternalUtils;
import com.github.fluent.hibernate.transformer.FluentHibernateResultTransformer;

/**
 * @param <T>
 *            type of return value.
 *
 * @author V.Ladynev
 */
class HibernateQuery<T> {

    private final HibernateQueryParameters params = new HibernateQueryParameters();

    private int maxResults;

    private ResultTransformer transformer;

    private IQueryFactory queryFactory;

    public HibernateQuery(IQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    /**
     * Add a named query parameter.
     *
     * @param name
     *            name of parameter
     * @param val
     *            parameter value
     */
    public HibernateQuery<T> p(String name, Object val) {
        params.add(name, val);
        return this;
    }

    public HibernateQuery<T> p(String name, Object... vals) {
        params.add(name, Arrays.asList(vals));
        return this;
    }

    public HibernateQuery<T> maxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    // TODO transformer not works for pid a nested fields
    public HibernateQuery<T> transform(Class<?> clazz) {
        transformer = new FluentHibernateResultTransformer(clazz);
        return this;
    }

    public HibernateQuery<T> useTransformer(ResultTransformer transformer) {
        this.transformer = transformer;
        return this;
    }

    public T first() {
        return InternalUtils.CollectionUtils.first(list());
    }

    public List<T> list() {
        return HibernateSessionFactory.doInTransaction(new IRequest<List<T>>() {
            @Override
            public List<T> doInTransaction(Session session) {
                return tuneForSelect(createQuery(session)).list();
            }
        });
    }

    private Query tuneForSelect(Query hibernateQuery) {
        if (maxResults != 0) {
            hibernateQuery.setMaxResults(maxResults);
        }

        if (transformer != null) {
            hibernateQuery.setResultTransformer(transformer);
        }

        return hibernateQuery;
    }

    // TODO may be return long?
    public int count() {
        Number result = HibernateSessionFactory.doInTransaction(new IRequest<Number>() {
            @Override
            public Number doInTransaction(Session session) {
                return (Number) createQuery(session).uniqueResult();
            }
        });

        return result == null ? 0 : result.intValue();
    }

    private Query createQuery(Session session) {
        Query result = queryFactory.create(session);
        params.setParametersToQuery(result);
        return result;
    }

    public interface IQueryFactory {

        Query create(Session session);

    };

}
