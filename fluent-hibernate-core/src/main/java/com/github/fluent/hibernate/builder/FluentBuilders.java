package com.github.fluent.hibernate.builder;

import java.util.Collection;

/**
 * Created by alexey.pchelnikov.
 */
public class FluentBuilders {

    public static InFluentBuilder in(String propertyName, Collection<?> values) {
        return new InFluentBuilder(propertyName, values);
    }

    public static EqFluentBuilder eq(String propertyName, Object value) {
        return new EqFluentBuilder(propertyName, value);
    }

}
