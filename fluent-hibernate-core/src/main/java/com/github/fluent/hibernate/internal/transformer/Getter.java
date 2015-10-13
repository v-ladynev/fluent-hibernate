package com.github.fluent.hibernate.internal.transformer;

import java.lang.reflect.Method;

import org.hibernate.HibernateException;

/**
 * @author DoubleF1re
 */
/* package */ final class Getter {

    private final Class<?> clazz;
    private final transient Method method;
    private final String propertyName;

    /* package */ Getter(Class<?> clazz, Method method, String propertyName) {
        this.clazz = clazz;
        this.method = method;
        this.propertyName = propertyName;
    }

    public Object get(Object target) throws HibernateException {
        try {
            return method.invoke(target, (Object[]) null);
        } catch (Exception e) {
            throw new HibernateException(
                    String.format("Exception throwns by getter. Class[%s], propertyName[%s]", clazz,
                            propertyName),
                    e);
        }
    }

    public Class<?> getReturnType() {
        return method.getReturnType();
    }
}
