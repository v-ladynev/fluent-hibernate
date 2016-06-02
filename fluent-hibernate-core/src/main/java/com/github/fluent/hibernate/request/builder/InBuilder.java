package com.github.fluent.hibernate.request.builder;

import java.util.Collection;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.github.fluent.hibernate.internal.util.InternalUtils.CollectionUtils;
import com.github.fluent.hibernate.request.HibernateRequest;

/**
 *
 * @author alexey.pchelnikov.
 */
public class InBuilder implements IBuilder {

    private final String propertyName;

    private final Collection<?> values;

    private boolean nothingForEmptyCollection;

    /*package*/InBuilder(String propertyName, Collection<?> values) {
        this.propertyName = propertyName;
        this.values = values;
    }

    public IBuilder nothingForEmptyCollection() {
        nothingForEmptyCollection = true;
        return this;
    }

    @Override
    public <T> void build(HibernateRequest<T> hibernateRequest) {
        if (nothingForEmptyCollection && CollectionUtils.isEmpty(values)) {
            hibernateRequest.add(getFalseRestriction());
        } else {
            hibernateRequest.in(propertyName, values);
        }
    }

    private Criterion getFalseRestriction() {
        return Restrictions.sqlRestriction("1<>1");
    }

}
