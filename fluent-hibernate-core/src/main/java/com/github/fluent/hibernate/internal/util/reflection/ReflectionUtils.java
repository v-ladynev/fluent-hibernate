package com.github.fluent.hibernate.internal.util.reflection;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Method;

import com.github.fluent.hibernate.internal.util.InternalUtils;

/**
 *
 * @author V.Ladynev
 */
public final class ReflectionUtils {

    private ReflectionUtils() {

    }

    public static void makePublic(AccessibleObject accessibleObject) {
        if (accessibleObject != null) {
            accessibleObject.setAccessible(true);
        }
    }

    /**
     * Try to find a getter method by a property name. Check parent classes and interfaces.
     *
     * @param rootClass
     *            a root class form which begin to find a getter
     * @param propertyName
     *            a property name
     * @return getter method or null, if such getter is not exist
     */
    public static Method findGetterMethod(Class<?> rootClass, String propertyName) {
        Class<?> checkClass = rootClass;
        Method result = null;

        // check containerClass, and then its super types (if any)
        while (result == null && checkClass != null) {
            if (checkClass.equals(Object.class)) {
                break;
            }

            result = getClassGetter(checkClass, propertyName);
            checkClass = checkClass.getSuperclass();
        }

        // if no getter found yet, check all implemented interfaces
        if (result == null) {
            for (Class<?> theInterface : rootClass.getInterfaces()) {
                result = getClassGetter(theInterface, propertyName);
                if (result != null) {
                    break;
                }
            }
        }

        makePublic(result);

        return result;
    }

    /**
     * Try to find a class getter method by a property name. Don't check parent classes or
     * interfaces.
     *
     * @param classToCheck
     *            a class in which find a getter
     * @param propertyName
     *            a property name
     * @return getter method or null, if such getter is not exist
     */
    public static Method getClassGetter(Class<?> classToCheck, String propertyName) {
        PropertyDescriptor[] descriptors = getPropertyDescriptors(classToCheck);

        for (PropertyDescriptor descriptor : descriptors) {
            if (isReadPropertyMethod(descriptor, propertyName)) {
                return descriptor.getReadMethod();
            }
        }

        return null;
    }

    private static boolean isReadPropertyMethod(PropertyDescriptor descriptor,
            String propertyName) {
        Method method = descriptor.getReadMethod();
        return method != null && method.getParameterTypes().length == 0
                && descriptor.getName().equalsIgnoreCase(propertyName);
    }

    private static PropertyDescriptor[] getPropertyDescriptors(Class<?> beanClass) {
        try {
            return Introspector.getBeanInfo(beanClass).getPropertyDescriptors();
        } catch (IntrospectionException ex) {
            throw InternalUtils.toRuntimeException(ex);
        }
    }

}
