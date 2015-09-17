package com.github.fluent.hibernate;

import java.util.Arrays;

import org.hibernate.Query;
import org.hibernate.Session;

/**
 *
 * @author DoubleF1re
 * @author V.Ladynev
 */
public final class HibernateUpdate {

    private final HibernateQueryParameters params = new HibernateQueryParameters();

    private final String updateQuery;

    public HibernateUpdate(String updateQuery) {
        this.updateQuery = updateQuery;
    }

    public static HibernateUpdate create(String updateQuery) {
        HibernateUpdate result = new HibernateUpdate(updateQuery);
        return result;
    }

    /**
     * Add a request parameter.
     *
     * @param name
     *            parameter name
     * @param value
     *            value
     */
    public HibernateUpdate p(String name, Object value) {
        params.add(name, value);
        return this;
    }

    public HibernateUpdate p(String name, Object... valsues) {
        params.add(name, Arrays.asList(valsues));
        return this;
    }

    public int update() {
        return HibernateSessionFactory.doInTransaction(new IRequest<Integer>() {
            @Override
            public Integer doInTransaction(Session session) {
                Query query = session.createQuery(updateQuery);
                params.setParametersToQuery(query);
                return query.executeUpdate();
            }
        });

    }

}
