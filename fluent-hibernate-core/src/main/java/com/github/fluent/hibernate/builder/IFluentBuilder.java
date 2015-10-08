package com.github.fluent.hibernate.builder;

import com.github.fluent.hibernate.HibernateRequest;

/**
 * Created by alexey.pchelnikov.
 */
public interface IFluentBuilder {

    <T> void build(HibernateRequest<T> hibernateRequest);
}
