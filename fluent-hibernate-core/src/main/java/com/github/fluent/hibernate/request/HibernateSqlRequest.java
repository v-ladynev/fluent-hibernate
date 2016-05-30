package com.github.fluent.hibernate.request;

import java.util.Arrays;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.transform.ResultTransformer;

import com.github.fluent.hibernate.internal.util.InternalUtils.CollectionUtils;
import com.github.fluent.hibernate.request.HibernateQuery.IQueryFactory;

/**
 * @param <T>
 *            type of return value.
 *
 * @author V.Ladynev
 */
public final class HibernateSqlRequest<T> {

    private final HibernateQuery<T> query;

    private List<IToAddToSQLQuery> toAddToSQLQuery = CollectionUtils.newArrayList();

    private HibernateSqlRequest(String query) {
        this.query = createQuery(query);
    }

    private HibernateQuery<T> createQuery(final String query) {
        return new HibernateQuery<T>(new IQueryFactory() {
            @Override
            public Query create(Session session) {
                SQLQuery result = session.createSQLQuery(query);

                for (IToAddToSQLQuery toAdd : toAddToSQLQuery) {
                    toAdd.addToQuery(result);
                }

                return result;

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

    public HibernateSqlRequest<T> p(String name, Object val) {
        query.p(name, val);
        return this;
    }

    public HibernateSqlRequest<T> p(String name, Object... vals) {
        query.p(name, Arrays.asList(vals));
        return this;
    }

    public HibernateSqlRequest<T> maxResults(int maxResults) {
        query.maxResults(maxResults);
        return this;
    }

    public HibernateSqlRequest<T> transform(Class<?> clazz) {
        query.transform(clazz);
        return this;
    }

    public HibernateSqlRequest<T> useTransformer(ResultTransformer transformer) {
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

    public HibernateSqlRequest<T> addEntity(Class<?> entityType) {
        return addEntity(null, entityType);
    }

    public HibernateSqlRequest<T> addEntity(String tableAlias, Class<?> entityType) {
        toAddToSQLQuery.add(new SQLEntityToAdd(tableAlias, entityType));
        return this;
    }

    public HibernateSqlRequest<T> addJoin(String tableAlias, String path) {
        return addJoin(tableAlias, path, null);
    }

    public HibernateSqlRequest<T> addJoin(String tableAlias, String ownerTableAlias,
            String joinPropertyName) {
        toAddToSQLQuery.add(new SQLJoinToAdd(tableAlias, ownerTableAlias, joinPropertyName));
        return this;
    }

    public static <T> HibernateSqlRequest<T> create(String query) {
        return new HibernateSqlRequest<T>(query);
    }

}
