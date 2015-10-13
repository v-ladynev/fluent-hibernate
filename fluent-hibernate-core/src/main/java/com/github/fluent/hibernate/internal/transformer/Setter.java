package com.github.fluent.hibernate.internal.transformer;

import java.lang.reflect.Method;

import org.hibernate.HibernateException;
import org.hibernate.PropertyAccessException;

/**
 * @author DoubleF1re
 */
/* package */ final class Setter {

    private final Class<?> clazz;
    private final transient Method[] getMethods;
    private final transient Method[] setMethods;
    private final transient Method method;
    private final String propertyName;

    /* package */ Setter(Class<?> clazz, Method[] getMethods, Method[] setMethods, Method method,
            String propertyName) {
        this.clazz = clazz;
        this.method = method;
        this.propertyName = propertyName;
        this.getMethods = getMethods;
        this.setMethods = setMethods;
    }

    public void set(Object target, Object value) throws HibernateException {
        try {
            invokeSet(target, value);
        } catch (Exception e) {
            checkForPrimitive(value);
            String errorMessage = String.format(
                    "Setter information: expected type: %s, actual type: %s",
                    method.getParameterTypes()[0].getName(),
                    value == null ? null : value.getClass().getName());
            throw new PropertyAccessException(e, errorMessage, true, clazz, propertyName);
        }
    }

    private void invokeSet(Object target, Object value) throws Exception {
        Object tmpTarget = target;
        for (int i = 0; i < getMethods.length; i++) {
            Object tmpTarget2 = getMethods[i].invoke(tmpTarget, new Object[] {});
            if (tmpTarget2 == null) {
                tmpTarget2 = instantiate(getMethods[i].getReturnType());
                setMethods[i].invoke(tmpTarget, new Object[] { tmpTarget2 });
            }
            tmpTarget = tmpTarget2;
        }
        method.invoke(tmpTarget, new Object[] { value });
    }

    private void checkForPrimitive(Object value) {
        if (value == null && method.getParameterTypes()[0].isPrimitive()) {
            throw new HibernateException(String.format(
                    "Value is null, but property type is primitive. Class[%s], propertyName[%s]",
                    clazz, propertyName));
        }
    }

    private Object instantiate(Class<?> clazz) throws IllegalAccessException {
        try {
            return clazz.newInstance();
        } catch (InstantiationException e) {
            throw new HibernateException(
                    String.format("Can't instanciate intermediate bean %s", clazz.toString()));
        }
    }
}