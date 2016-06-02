package com.github.fluent.hibernate.request.builder;

import com.github.fluent.hibernate.request.HibernateRequest;

/**
 * @author alexey.pchelnikov.
 */
public class EqBuilder implements IBuilder {

    private final String propertyName;

    private final Object value;

    // TODO mutually exclusive
    private boolean ifNotNull;

    private boolean orIsNull;

    /*package*/ EqBuilder(String propertyName, Object value) {
        this.propertyName = propertyName;
        this.value = value;
    }

    public IBuilder ifNotNull() {
        ifNotNull = true;
        return this;
    }

    @Override
    public <T> void build(HibernateRequest<T> hibernateRequest) {
        if (orIsNull) {
            hibernateRequest.eqOrIsNull(propertyName, value);
        } else if (!ifNotNull || value != null) {
            hibernateRequest.eq(propertyName, value);
        }
    }

    public IBuilder orIsNull() {
        orIsNull = true;
        return this;
    }
}
