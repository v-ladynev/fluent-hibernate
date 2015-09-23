package com.github.fluent.hibernate;

import java.util.Collection;
import java.util.List;

import org.hibernate.Query;

/**
 * Named query parameters.
 *
 * @author DoubleF1re
 * @author V.Ladynev
 */
public class HibernateQueryParameters {

    private final List<Parameter> parameters = InternalUtils.CollectionUtils.newArrayList();

    public HibernateQueryParameters() {

    }

    /**
     * Constructor. Add one named query parameter.
     *
     * @param name
     *            name of parameter
     *
     * @param val
     *            parameter value
     */
    public HibernateQueryParameters(String name, Object val) {
        add(name, val);
    }

    public static final HibernateQueryParameters create() {
        return new HibernateQueryParameters();
    }

    /**
     * Add a named query parameter.
     *
     * @param name
     *            name of parameter
     * @param val
     *            parameter value
     */
    public HibernateQueryParameters add(String name, Object val) {
        parameters.add(new Parameter(name, val));
        return this;
    }

    public void setParametersToQuery(Query query) {
        for (Parameter parameter : parameters) {
            parameter.setToQuery(query);
        }
    }

    private static final class Parameter {
        private final String name;

        private final Object val;

        public Parameter(String name, Object val) {
            this.name = name;
            this.val = val;
        }

        public void setToQuery(Query query) {
            if (val instanceof Collection<?>) {
                query.setParameterList(name, (Collection<?>) val);
            } else {
                query.setParameter(name, val);
            }
        }
    }

}
