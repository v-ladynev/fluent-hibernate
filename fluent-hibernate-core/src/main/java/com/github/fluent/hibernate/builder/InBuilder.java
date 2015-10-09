package com.github.fluent.hibernate.builder;

import java.util.Collection;

import com.github.fluent.hibernate.internal.util.InternalUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.github.fluent.hibernate.HibernateRequest;

/**
 * Created by alexey.pchelnikov.
 */
public class InBuilder implements IFluentBuilder {

    private final String propertyName;
    private final Collection<?> values;

    private boolean nothingIfEmpty;

    public InBuilder(String propertyName, Collection<?> values) {
        this.propertyName = propertyName;
        this.values = values;
    }

    public IFluentBuilder nothingIfEmpty() {
        nothingIfEmpty = true;
        return this;
    }

    private Criterion getFalseRestriction() {
        return Restrictions.sqlRestriction("1<>1");
    }

    @Override
    public <T> void build(HibernateRequest<T> hibernateRequest) {
        if (nothingIfEmpty && InternalUtils.CollectionUtils.isEmpty(values)) {
            hibernateRequest.add(getFalseRestriction());
        } else {
            hibernateRequest.in(propertyName, values);
        }
    }

}
