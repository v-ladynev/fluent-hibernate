package com.github.fluent.hibernate.internal.transformer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.hibernate.HibernateException;
import org.hibernate.PropertyAccessException;

/**
 * @author DoubleF1re
 */
/* package */ final class Setter {
    @SuppressWarnings("rawtypes")
    private final Class clazz;
    private final transient Method[] getMethods;
    private final transient Method[] setMethods;
    private final transient Method method;
    private final String propertyName;

    @SuppressWarnings("rawtypes")
    /* package */ Setter(Class clazz, Method[] getMethods, Method[] setMethods, Method method,
            String propertyName) {
        this.clazz = clazz;
        this.method = method;
        this.propertyName = propertyName;
        this.getMethods = getMethods;
        this.setMethods = setMethods;
    }

    @SuppressWarnings("rawtypes")
    public void set(Object target, Object value)
            throws HibernateException {
        try {
            Object tmpTarget = target;
            for (int i = 0; i < getMethods.length; i++) {
                Object tmpTarget2 = getMethods[i].invoke(tmpTarget, new Object[] {});
                if (tmpTarget2 == null) {
                    Class tmpClass = getMethods[i].getReturnType();
                    try {
                        tmpTarget2 = tmpClass.newInstance();
                    } catch (InstantiationException e) {
                        throw new HibernateException(String.format(
                                "Can't instanciate intermediate bean %s", tmpTarget.toString()));
                    }
                    setMethods[i].invoke(tmpTarget, new Object[] { tmpTarget2 });
                }
                tmpTarget = tmpTarget2;
            }
            method.invoke(tmpTarget, new Object[] { value });
        } catch (NullPointerException npe) {
            if (value == null && method.getParameterTypes()[0].isPrimitive()) {
                throw new PropertyAccessException(npe,
                        "Null value was assigned to a property of primitive type", true, clazz,
                        propertyName);
            }
            throw new PropertyAccessException(npe, "NullPointerException occurred while calling",
                    true, clazz, propertyName);
        } catch (InvocationTargetException ite) {
            throw new PropertyAccessException(ite, "Exception occurred inside", true, clazz,
                    propertyName);
        } catch (IllegalAccessException iae) {
            throw new PropertyAccessException(iae, "IllegalAccessException occurred while calling",
                    true, clazz, propertyName);
        } catch (IllegalArgumentException iae) {
            if (value == null && method.getParameterTypes()[0].isPrimitive()) {
                throw new PropertyAccessException(iae,
                        "Null value was assigned to a property of primitive type", true, clazz,
                        propertyName);
            }
            String errorMessage = String.format("Expected type: %s, actual value: %s",
                    method.getParameterTypes()[0].getName(),
                    value == null ? null : value.getClass().getName());
            throw new PropertyAccessException(iae, errorMessage, true, clazz, propertyName);
        } catch (Exception e) {
            String errorMessage = String.format(
                    "Setter information: expected type: %s, actual type: %s",
                    method.getParameterTypes()[0].getName(),
                    value == null ? null : value.getClass().getName());
            throw new PropertyAccessException(e, errorMessage, true, clazz, propertyName);
        }
    }

    public Method getMethod() {
        return method;
    }

    public String getMethodName() {
        return method.getName();
    }

    @Override
    public String toString() {
        return "BasicSetter(" + clazz.getName() + '.' + propertyName + ')';
    }
}
