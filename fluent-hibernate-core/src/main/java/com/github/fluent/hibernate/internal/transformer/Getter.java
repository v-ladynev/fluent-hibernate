/**
 * $HeadURL: http://svn.isida.by:3690/svn/dev2/trunk/systems/dev2.settings/eclipse_settings/Code%20Templates.xml $
 * $Revision$
 * $Date::                             $
 *
 * Copyright (c) Isida-Informatica, Ltd. All Rights Reserved.
 */
package com.github.fluent.hibernate.internal.transformer;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.PropertyAccessException;
import org.hibernate.engine.spi.SessionImplementor;

/**
 * @author DoubleF1re
 */
public final class Getter {
    @SuppressWarnings("rawtypes")
    private final Class clazz;
    private final transient Method method;
    private final String propertyName;

    @SuppressWarnings("rawtypes")
    public Getter(Class clazz, Method method, String propertyName) {
        this.clazz = clazz;
        this.method = method;
        this.propertyName = propertyName;
    }

    public Object get(Object target) throws HibernateException {
        try {
            return method.invoke(target, (Object[]) null);
        } catch (InvocationTargetException ite) {
            throw new PropertyAccessException(ite, "Exception occurred inside", false, clazz,
                    propertyName);
        } catch (IllegalAccessException iae) {
            throw new PropertyAccessException(iae, "IllegalAccessException occurred while calling",
                    false, clazz, propertyName);
        } catch (IllegalArgumentException iae) {
            throw new PropertyAccessException(iae, "IllegalArgumentException occurred calling",
                    false, clazz, propertyName);
        }
    }

    @SuppressWarnings("rawtypes")
    public Object getForInsert(Object target, Map mergeMap, SessionImplementor session) {
        return get(target);
    }

    @SuppressWarnings("rawtypes")
    public Class getReturnType() {
        return method.getReturnType();
    }

    public Method getMethod() {
        return method;
    }

    public String getMethodName() {
        return method.getName();
    }

    public Member getMember() {
        return method;
    }

    @Override
    public String toString() {
        return String.format("BasicGetter(%s.%s)", clazz.getName(), propertyName);
    }

    /*Object readResolve() throws PropertyNotFoundException, IntrospectionException {
        return createGetter(clazz, propertyName);
    }*/
}
