package com.github.fluent.hibernate.internal.transformer;

import java.lang.reflect.Method;

/**
 *
 * @author DoubleF1re
 */
/* package */final class Getter {

    private final Method method;

    public Getter(Method method) {
        this.method = method;
    }

    public Class<?> getReturnType() {
        return method.getReturnType();
    }

}
