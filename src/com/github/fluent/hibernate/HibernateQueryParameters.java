package com.github.fluent.hibernate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Query;

/**
 * Именованные параметры запроса.
 *
 * @author DoubleF1re
 * @author V.Ladynev
 */
public class HibernateQueryParameters {

    private final List<Parameter> parameters;

    /**
     * Создание пустых параметров запроса.
     */
    public HibernateQueryParameters() {
        parameters = new ArrayList<Parameter>(4);
    }

    /**
     * Создание параметров запроса с одним параметром.
     *
     * @param name
     *            имя параметра
     * @param val
     *            значение
     */
    public HibernateQueryParameters(String name, Object val) {
        parameters = new ArrayList<Parameter>(1);
        add(name, val);
    }

    public static final HibernateQueryParameters create() {
        return new HibernateQueryParameters();
    }

    /**
     * Добавляет параметр запроса.
     *
     * @param name
     *            имя параметра
     * @param val
     *            значение
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

    /**
     * Объект параметров.
     */
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
