package com.github.fluent.hibernate.builder;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import com.github.fluent.hibernate.HibernateRequest;

/**
 * Created by alexey.pchelnikov.
 */
public class EqFluentBuilder implements IFluentBuilder {

	private final String propertyName;
	private final Object value;

	// TODO mutually exclusive
	private boolean ifNotNull;
	private boolean orIsNull;

	public EqFluentBuilder(String propertyName, Object value) {
		this.propertyName = propertyName;
		this.value = value;
	}

	public IFluentBuilder ifNotNull() {
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

	public IFluentBuilder orIsNull() {
		orIsNull = true;
		return this;
	}
}
