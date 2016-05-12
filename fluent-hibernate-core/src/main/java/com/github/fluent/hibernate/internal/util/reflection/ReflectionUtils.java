package com.github.fluent.hibernate.internal.util.reflection;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.annotation.Annotation;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.github.fluent.hibernate.internal.util.InternalUtils;
import com.github.fluent.hibernate.internal.util.InternalUtils.StringUtils;

/**
 *
 * @author V.Ladynev
 */
public final class ReflectionUtils {

    public static final Class<?>[] NO_PARAM_SIGNATURE = new Class<?>[0];

    public static final Class<?>[] SINGLE_OBJECT_PARAM_SIGNATURE = new Class[] { Object.class };

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
     * @return the getter method or null, if such getter is not exist
     */
    public static Method getClassGetter(Class<?> classToCheck, String propertyName) {
        PropertyDescriptor[] descriptors = getPropertyDescriptors(classToCheck);

        for (PropertyDescriptor descriptor : descriptors) {
            if (isGetter(descriptor, propertyName)) {
                return descriptor.getReadMethod();
            }
        }

        return null;
    }

    private static boolean isGetter(PropertyDescriptor descriptor, String propertyName) {
        Method method = descriptor.getReadMethod();
        return method != null && method.getParameterTypes().length == 0
                && descriptor.getName().equalsIgnoreCase(propertyName);
    }

    /**
     * Try to find a class setter method by a property name. Don't check parent classes or
     * interfaces.
     *
     * @param classToCheck
     *            a class in which find a setter
     * @param propertyName
     *            a property name
     * @param getterMethod
     *            a getter method for getting a type of a property
     *
     * @return the setter method or null, if such setter is not exist
     */
    public static Method getClassSetter(Class<?> classToCheck, String propertyName,
            Method getterMethod) {
        return getClassSetter(classToCheck, propertyName,
                getterMethod == null ? null : getterMethod.getReturnType());
    }

    /**
     * Try to find a class setter method by a property name. Don't check parent classes or
     * interfaces.
     *
     * @param classToCheck
     *            a class in which find a setter
     * @param propertyName
     *            a property name
     * @param propertyType
     *            a type of a property
     *
     * @return the setter method or null, if such setter is not exist
     */
    public static Method getClassSetter(Class<?> classToCheck, String propertyName,
            Class<?> propertyType) {
        PropertyDescriptor[] descriptors = getPropertyDescriptors(classToCheck);

        for (PropertyDescriptor descriptor : descriptors) {
            if (isSetter(descriptor, propertyName, propertyType)) {
                return descriptor.getWriteMethod();
            }

        }

        return null;
    }

    private static boolean isSetter(PropertyDescriptor descriptor, String propertyName,
            Class<?> propertyType) {
        Method method = descriptor.getWriteMethod();

        return method != null && method.getParameterTypes().length == 1
                && method.getName().startsWith("set")
                && descriptor.getName().equalsIgnoreCase(propertyName)
                && (propertyType == null || method.getParameterTypes()[0].equals(propertyType));
    }

    private static PropertyDescriptor[] getPropertyDescriptors(Class<?> beanClass) {
        try {
            return Introspector.getBeanInfo(beanClass).getPropertyDescriptors();
        } catch (IntrospectionException ex) {
            throw InternalUtils.toRuntimeException(ex);
        }
    }

    public static Field findField(Class<?> classToCheck, String propertyName) {
        if (classToCheck == null || Object.class.equals(classToCheck)) {
            return null;
        }

        try {
            return classToCheck.getDeclaredField(propertyName);
        } catch (NoSuchFieldException nsfe) {
            return findField(classToCheck.getSuperclass(), propertyName);
        }

    }

    public static String[] getPropertyParts(String property) {
        return StringUtils.splitByDot(property);
    }

    public static <T extends Annotation> T getAnnotation(Class<?> classToCheck, String propertyName,
            Class<T> annotationClass) {
        T result = getAnnotation(findGetterMethod(classToCheck, propertyName), annotationClass);
        return result == null
                ? getAnnotation(findField(classToCheck, propertyName), annotationClass) : result;
    }

    private static <T extends Annotation> T getAnnotation(AccessibleObject accessibleObject,
            Class<T> annotationClass) {
        return accessibleObject == null ? null : accessibleObject.getAnnotation(annotationClass);
    }

    public static Method extractMethod(Class<?> clazz, String methodName)
            throws NoSuchMethodException {
        return clazz.getMethod(methodName, NO_PARAM_SIGNATURE);
    }

    public static <T> T invoke(Object thisObject, Method method)
            throws IllegalAccessException, InvocationTargetException {
        return (T) method.invoke(thisObject, NO_PARAM_SIGNATURE);
    }

    public static Method extractMethod(Class<?> clazz, String methodName, Class<?> parameterType)
            throws NoSuchMethodException {
        return clazz.getMethod(methodName, parameterType);
    }

    public static <T> T invoke(Object thisObject, Method method, Object parameter)
            throws IllegalAccessException, InvocationTargetException {
        return (T) method.invoke(thisObject, parameter);
    }

}
