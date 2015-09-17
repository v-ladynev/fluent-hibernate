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
     * Добавляет параметр запроса.
     *
     * @param name
     *            имя параметра
     * @param val
     *            значение
     */
    public HibernateUpdate p(String name, Object val) {
        params.add(name, val);
        return this;
    }

    public HibernateUpdate p(String name, Object... vals) {
        params.add(name, Arrays.asList(vals));
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
