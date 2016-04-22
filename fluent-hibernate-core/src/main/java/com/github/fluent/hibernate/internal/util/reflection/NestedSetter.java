package com.github.fluent.hibernate.internal.util.reflection;

import java.lang.reflect.Method;

import org.hibernate.HibernateException;
import org.hibernate.PropertyAccessException;

import com.github.fluent.hibernate.internal.util.InternalUtils;

/**
 * A nested setter.
 *
 * @author DoubleF1re
 */
public final class NestedSetter {

    private final Class<?> clazz;
    private final transient Method[] getMethods;
    private final transient Method[] setMethods;
    private final transient Method method;
    private final String propertyName;

    public NestedSetter(Class<?> clazz, Method[] getMethods, Method[] setMethods, Method method,
            String propertyName) {
        this.clazz = clazz;
        this.method = method;
        this.propertyName = propertyName;
        this.getMethods = getMethods;
        this.setMethods = setMethods;
    }

    public void set(Object target, Object value) {
        try {
            invokeSet(target, value);
        } catch (Exception ex) {
            checkForPrimitive(value);
            String errorMessage = String.format(
                    "Setter information: expected type: %s, actual type: %s",
                    method.getParameterTypes()[0].getName(),
                    value == null ? null : value.getClass().getName());
            throw new PropertyAccessException(ex, errorMessage, true, clazz, propertyName);
        }
    }

    private void invokeSet(Object target, Object value) throws Exception {
        Object tmpTarget = target;
        for (int i = 0; i < getMethods.length; i++) {
            Object tmpTarget2 = getMethods[i].invoke(tmpTarget, new Object[] {});
            if (tmpTarget2 == null) {
                tmpTarget2 = InternalUtils.newInstance(getMethods[i].getReturnType());
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

}
