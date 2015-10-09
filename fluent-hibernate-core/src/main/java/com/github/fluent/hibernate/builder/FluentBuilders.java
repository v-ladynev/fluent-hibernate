package com.github.fluent.hibernate.builder;

import java.util.Collection;

/**
 * Created by alexey.pchelnikov.
 */
public class FluentBuilders {

    public static InBuilder in(String propertyName, Collection<?> values) {
        return new InBuilder(propertyName, values);
    }

    public static EqBuilder eq(String propertyName, Object value) {
        return new EqBuilder(propertyName, value);
    }

}
