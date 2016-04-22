package com.github.fluent.hibernate.internal.util.reflection;

import java.lang.reflect.Method;

import org.hibernate.HibernateException;
import org.hibernate.PropertyNotFoundException;

/**
 * Accesses setter of a nested property by the reflection.
 *
 * @author DoubleF1re
 * @author V.Ladynev
 */
public class NestedSetterAccessor {

    private static final String SPLITTER_REG_EX = "\\.";

    public static NestedSetter createSetter(Class<?> theClass, String propertyName) {
        NestedSetter result = getSetterOrNull(theClass, propertyName);
        if (result == null) {
            throw new PropertyNotFoundException(
                    String.format("Could not find a setter for property `%s` in the class `%s`",
                            propertyName, theClass.getName()));
        }
        return result;
    }

    private static NestedSetter getSetterOrNull(Class<?> theClass, String propertyName) {
        if (theClass == Object.class || theClass == null || propertyName == null) {
            return null;
        }

        String[] propertyParts = propertyName.split(SPLITTER_REG_EX);

        int nestedCount = propertyParts.length;

        Method[] getMethods = new Method[nestedCount - 1];
        Method[] setMethods = new Method[nestedCount - 1];
        Class<?> tmpClass = theClass;
        for (int i = 0; i < nestedCount - 1; i++) {
            Method getter = ReflectionUtils.getClassGetter(tmpClass, propertyParts[i]);

            if (getter == null) {
                throw new HibernateException(String.format(
                        "Intermediate getter property not found `%s`, "
                                + "for property `%s`, for the class `%s`",
                        propertyParts[i], propertyName, theClass.getName()));
            }

            getMethods[i] = getter;
            setMethods[i] = ReflectionUtils.getClassSetter(tmpClass, propertyParts[i], getter);

            tmpClass = getMethods[i].getReturnType();
        }

        Method method = setterMethod(tmpClass, propertyParts[nestedCount - 1]);
        if (method != null) {
            ReflectionUtils.makePublic(method);
            return new NestedSetter(theClass, getMethods, setMethods, method, propertyName);
        }

        NestedSetter setter = getSetterOrNull(theClass.getSuperclass(), propertyName);
        if (setter == null) {
            Class<?>[] interfaces = theClass.getInterfaces();
            for (int i = 0; setter == null && i < interfaces.length; i++) {
                setter = getSetterOrNull(interfaces[i], propertyName);
            }
        }
        return setter;

    }

    private static Method setterMethod(Class<?> theClass, String propertyName) {
        Method getter = ReflectionUtils.getClassGetter(theClass, propertyName);
        return ReflectionUtils.getClassSetter(theClass, propertyName, getter);
    }

}
