package com.github.fluent.hibernate.internal.util.reflection;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

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

    public static NestedSetter getSetter(Class<?> theClass, String propertyName) {
        return createSetter(theClass, propertyName);
    }

    private static NestedSetter createSetter(Class<?> theClass, String propertyName) {
        NestedSetter result = getSetterOrNull(theClass, propertyName);
        if (result == null) {
            throw new PropertyNotFoundException(
                    String.format("Could not find a setter for property `%s` in the class `%s`",
                            propertyName, theClass.getName()));
        }
        return result;
    }

    /*
    private static NestedSetter getSetterOrNull(Class<?> theClass, String propertyName) {
        if (theClass == Object.class || theClass == null || propertyName == null) {
            return null;
        }
    
        String[] pNames = propertyName.split(SPLITTER_REG_EX);
    
        Method[] getMethods = new Method[pNames.length - 1];
        Method[] setMethods = new Method[pNames.length - 1];
        Class<?> tmpClass = theClass;
        for (int i = 0; i < getMethods.length; i++) {
            getMethods[i] = ReflectionUtils.getClassGetter(tmpClass, pNames[i]);
            setMethods[i] = setterMethod(tmpClass, pNames[i]);
            if (getMethods[i] == null) {
                throw new HibernateException(
                        String.format("intermediate setter property not found : %s", pNames[i]));
            }
            tmpClass = getMethods[i].getReturnType();
        }
    
        Method method = setterMethod(tmpClass, pNames[pNames.length - 1]);
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
        Method getter = ReflectionUtils.findGetterMethod(theClass, propertyName);
        Class<?> returnType = getter == null ? null : getter.getReturnType();
    
        PropertyDescriptor[] descriptors = getPropertyDescriptors(theClass);
    
        Method potentialSetter = null;
    
        for (PropertyDescriptor descriptor : descriptors) {
            potentialSetter = descriptor.getWriteMethod();
            if (potentialSetter == null) {
                continue;
            }
            if (potentialSetter.getParameterTypes().length == 1
                    && potentialSetter.getName().startsWith("set")) {
                if (descriptor.getName().equalsIgnoreCase(propertyName)) {
                    if (returnType == null
                            || potentialSetter.getParameterTypes()[0].equals(returnType)) {
                        return potentialSetter;
                    }
                }
            }
        }
        return potentialSetter;
    }
    
    private static PropertyDescriptor[] getPropertyDescriptors(Class<?> beanClass) {
        try {
            return Introspector.getBeanInfo(beanClass).getPropertyDescriptors();
        } catch (IntrospectionException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    */

    private static NestedSetter getSetterOrNull(Class<?> theClass, String propertyName) {
        if (theClass == Object.class || theClass == null) {
            return null;
        }

        String[] pNames = propertyName.split(SPLITTER_REG_EX);

        Method[] getMethods = new Method[pNames.length - 1];
        Method[] setMethods = new Method[pNames.length - 1];
        Class<?> tmpClass = theClass;
        for (int i = 0; i < getMethods.length; i++) {
            getMethods[i] = getterMethod(tmpClass, pNames[i]);
            setMethods[i] = setterMethod(tmpClass, pNames[i]);
            if (getMethods[i] == null) {
                throw new HibernateException(
                        String.format("intermediate setter property not found : %s", pNames[i]));
            }
            tmpClass = getMethods[i].getReturnType();
        }

        Method method = setterMethod(tmpClass, pNames[pNames.length - 1]);
        if (method != null) {
            makePublic(theClass, method);
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
        Getter getter = getGetterOrNull(theClass, propertyName);
        Class<?> returnType = getter == null ? null : getter.getReturnType();

        PropertyDescriptor[] descriptors = getPropertyDescriptors(theClass);

        Method potentialSetter = null;

        for (PropertyDescriptor descriptor : descriptors) {
            potentialSetter = descriptor.getWriteMethod();
            if (potentialSetter == null) {
                continue;
            }
            if (potentialSetter.getParameterTypes().length == 1
                    && potentialSetter.getName().startsWith("set")) {
                if (descriptor.getName().equalsIgnoreCase(propertyName)) {
                    if (returnType == null
                            || potentialSetter.getParameterTypes()[0].equals(returnType)) {
                        return potentialSetter;
                    }
                }
            }
        }
        return potentialSetter;
    }

    private static Getter getGetterOrNull(Class<?> theClass, String propertyName) {
        if (theClass == Object.class || theClass == null) {
            return null;
        }

        Method method = getterMethod(theClass, propertyName);

        if (method != null) {
            makePublic(theClass, method);
            return new Getter(method);
        }

        Getter getter = getGetterOrNull(theClass.getSuperclass(), propertyName);
        if (getter == null) {
            Class<?>[] interfaces = theClass.getInterfaces();
            for (int i = 0; getter == null && i < interfaces.length; i++) {
                getter = getGetterOrNull(interfaces[i], propertyName);
            }
        }
        return getter;
    }

    private static void makePublic(Class<?> clazz, Method method) {
        if (!isPublic(clazz, method)) {
            method.setAccessible(true);
        }
    }

    private static boolean isPublic(Class<?> clazz, Member member) {
        return Modifier.isPublic(member.getModifiers()) && Modifier.isPublic(clazz.getModifiers());
    }

    private static Method getterMethod(Class<?> theClass, String propertyName) {
        PropertyDescriptor[] descriptors = getPropertyDescriptors(theClass);

        for (PropertyDescriptor descriptor : descriptors) {
            Method readMethod = descriptor.getReadMethod();
            if (readMethod == null) {
                continue;
            }
            if (readMethod.getParameterTypes().length == 0
                    && descriptor.getName().equalsIgnoreCase(propertyName)) {
                return readMethod;
            }
        }

        return null;
    }

    private static PropertyDescriptor[] getPropertyDescriptors(Class<?> beanClass) {
        try {
            return Introspector.getBeanInfo(beanClass).getPropertyDescriptors();
        } catch (IntrospectionException ex) {
            throw new RuntimeException(ex);
        }
    }

}
