package com.github.fluent.hibernate.builder;

import com.github.fluent.hibernate.request.HibernateRequest;

/**
 * @author alexey.pchelnikov.
 */
public interface IBuilder {

    <T> void build(HibernateRequest<T> hibernateRequest);
}
