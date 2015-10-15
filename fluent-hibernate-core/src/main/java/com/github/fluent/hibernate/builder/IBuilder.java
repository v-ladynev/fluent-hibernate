package com.github.fluent.hibernate.builder;

import com.github.fluent.hibernate.request.HibernateRequest;

/**
 * @autor alexey.pchelnikov.
 */
public interface IBuilder {

    <T> void build(HibernateRequest<T> hibernateRequest);
}
