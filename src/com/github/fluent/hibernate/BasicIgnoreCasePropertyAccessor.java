package com.github.fluent.hibernate;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.PropertyAccessException;
import org.hibernate.PropertyNotFoundException;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.util.ReflectHelper;
import org.hibernate.property.Getter;
import org.hibernate.property.PropertyAccessor;
import org.hibernate.property.Setter;

/**
 * Accesses property values via a get/set pair, which may be nonpublic. The default (and recommended
 * strategy). The alias may be different cas that the method declaration.
 *
 * @author DoubleF1re
 * @author V.Ladynev
 */
public class BasicIgnoreCasePropertyAccessor implements PropertyAccessor {

    @Override
    @SuppressWarnings("rawtypes")
    public Setter getSetter(Class theClass, String propertyName) {
        return createSetter(theClass, propertyName);
    }

    @SuppressWarnings("rawtypes")
    private static Setter createSetter(Class theClass, String propertyName) {
        BasicSetter result = getSetterOrNull(theClass, propertyName);
        if (result == null) {
            throw new PropertyNotFoundException(String.format(
                    "Could not find a setter for property %s in class %s", propertyName,
                    theClass.getName()));
        }
        return result;
    }

    @SuppressWarnings("rawtypes")
    private static BasicSetter getSetterOrNull(Class theClass, String propertyName) {
        if (theClass == Object.class || theClass == null) {
            return null;
        }

        String splitter = "_";
        if (propertyName.indexOf('.') >= 0) {
            splitter = "\\.";
        }

        String[] pNames = propertyName.split(splitter);

        Method[] getMethods = new Method[pNames.length - 1];
        Method[] setMethods = new Method[pNames.length - 1];
        Class tmpClass = theClass;
        for (int i = 0; i < getMethods.length; i++) {
            getMethods[i] = getterMethod(tmpClass, pNames[i]);
            setMethods[i] = setterMethod(tmpClass, pNames[i]);
            if (getMethods[i] == null) {
                throw new HibernateException(String.format(
                        "intermediate setter property not found : %s", pNames[i]));
            }
            tmpClass = getMethods[i].getReturnType();
        }

        Method method = setterMethod(tmpClass, pNames[pNames.length - 1]);
        if (method != null) {
            if (!ReflectHelper.isPublic(theClass, method)) {
                method.setAccessible(true);
            }
            return new BasicSetter(theClass, getMethods, setMethods, method, propertyName);
        }
        BasicSetter setter = getSetterOrNull(theClass.getSuperclass(), propertyName);
        if (setter == null) {
            Class[] interfaces = theClass.getInterfaces();
            for (int i = 0; setter == null && i < interfaces.length; i++) {
                setter = getSetterOrNull(interfaces[i], propertyName);
            }
        }
        return setter;

    }

    @SuppressWarnings("rawtypes")
    private static Method setterMethod(Class theClass, String propertyName) {
        BasicGetter getter = getGetterOrNull(theClass, propertyName);
        Class returnType = getter == null ? null : getter.getReturnType();

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

    @Override
    @SuppressWarnings("rawtypes")
    public Getter getGetter(Class theClass, String propertyName) {
        return createGetter(theClass, propertyName);
    }

    @SuppressWarnings("rawtypes")
    public static Getter createGetter(Class theClass, String propertyName) {
        BasicGetter result = getGetterOrNull(theClass, propertyName);
        if (result == null) {
            throw new PropertyNotFoundException(String.format(
                    "Could not find a getter for %s in class %s", propertyName, theClass.getName()));
        }
        return result;

    }

    @SuppressWarnings("rawtypes")
    private static BasicGetter getGetterOrNull(Class theClass, String propertyName) {

        if (theClass == Object.class || theClass == null) {
            return null;
        }

        Method method = getterMethod(theClass, propertyName);

        if (method != null) {
            if (!ReflectHelper.isPublic(theClass, method)) {
                method.setAccessible(true);
            }
            return new BasicGetter(theClass, method, propertyName);
        }
        BasicGetter getter = getGetterOrNull(theClass.getSuperclass(), propertyName);
        if (getter == null) {
            Class[] interfaces = theClass.getInterfaces();
            for (int i = 0; getter == null && i < interfaces.length; i++) {
                getter = getGetterOrNull(interfaces[i], propertyName);
            }
        }
        return getter;
    }

    @SuppressWarnings("rawtypes")
    private static Method getterMethod(Class theClass, String propertyName) {
        PropertyDescriptor[] descriptors = getPropertyDescriptors(theClass);

        for (PropertyDescriptor descriptor : descriptors) {
            if (descriptor.getReadMethod() == null) {
                continue;
            }
            if (descriptor.getReadMethod().getParameterTypes().length == 0) {
                if (descriptor.getName().equalsIgnoreCase(propertyName)) {
                    return descriptor.getReadMethod();
                }
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

    /** Basic setter. */
    public static final class BasicSetter implements Setter {
        private static final long serialVersionUID = -3092319284832094425L;
        @SuppressWarnings("rawtypes")
        private final Class clazz;
        private final transient Method[] getMethods;
        private final transient Method[] setMethods;
        private final transient Method method;
        private final String propertyName;

        @SuppressWarnings("rawtypes")
        private BasicSetter(Class clazz, Method[] getMethods, Method[] setMethods, Method method,
                String propertyName) {
            this.clazz = clazz;
            this.method = method;
            this.propertyName = propertyName;
            this.getMethods = getMethods;
            this.setMethods = setMethods;
        }

        @Override
        @SuppressWarnings("rawtypes")
        public void set(Object target, Object value, SessionFactoryImplementor factory)
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
                throw new PropertyAccessException(npe,
                        "NullPointerException occurred while calling", true, clazz, propertyName);
            } catch (InvocationTargetException ite) {
                throw new PropertyAccessException(ite, "Exception occurred inside", true, clazz,
                        propertyName);
            } catch (IllegalAccessException iae) {
                throw new PropertyAccessException(iae,
                        "IllegalAccessException occurred while calling", true, clazz, propertyName);
            } catch (IllegalArgumentException iae) {
                if (value == null && method.getParameterTypes()[0].isPrimitive()) {
                    throw new PropertyAccessException(iae,
                            "Null value was assigned to a property of primitive type", true, clazz,
                            propertyName);
                }
                String errorMessage = String.format("Expected type: %s, actual value: %s", method
                        .getParameterTypes()[0].getName(), value == null ? null : value.getClass()
                        .getName());
                throw new PropertyAccessException(iae, errorMessage, true, clazz, propertyName);
            } catch (Exception e) {
                String errorMessage = String.format(
                        "Setter information: expected type: %s, actual type: %s", method
                        .getParameterTypes()[0].getName(), value == null ? null : value
                                .getClass().getName());
                throw new PropertyAccessException(e, errorMessage, true, clazz, propertyName);
            }
        }

        @Override
        public Method getMethod() {
            return method;
        }

        @Override
        public String getMethodName() {
            return method.getName();
        }

        Object readResolve() throws PropertyNotFoundException, IntrospectionException {
            return createSetter(clazz, propertyName);
        }

        @Override
        public String toString() {
            return "BasicSetter(" + clazz.getName() + '.' + propertyName + ')';
        }
    }

    /** Basic getter. */
    public static final class BasicGetter implements Getter {
        private static final long serialVersionUID = 2828591409798958202L;
        @SuppressWarnings("rawtypes")
        private final Class clazz;
        private final transient Method method;
        private final String propertyName;

        @SuppressWarnings("rawtypes")
        private BasicGetter(Class clazz, Method method, String propertyName) {
            this.clazz = clazz;
            this.method = method;
            this.propertyName = propertyName;
        }

        @Override
        public Object get(Object target) throws HibernateException {
            try {
                return method.invoke(target, (Object[]) null);
            } catch (InvocationTargetException ite) {
                throw new PropertyAccessException(ite, "Exception occurred inside", false, clazz,
                        propertyName);
            } catch (IllegalAccessException iae) {
                throw new PropertyAccessException(iae,
                        "IllegalAccessException occurred while calling", false, clazz, propertyName);
            } catch (IllegalArgumentException iae) {
                throw new PropertyAccessException(iae, "IllegalArgumentException occurred calling",
                        false, clazz, propertyName);
            }
        }

        @Override
        @SuppressWarnings("rawtypes")
        public Object getForInsert(Object target, Map mergeMap, SessionImplementor session) {
            return get(target);
        }

        @Override
        @SuppressWarnings("rawtypes")
        public Class getReturnType() {
            return method.getReturnType();
        }

        @Override
        public Method getMethod() {
            return method;
        }

        @Override
        public String getMethodName() {
            return method.getName();
        }

        @Override
        public Member getMember() {
            return method;
        }

        @Override
        public String toString() {
            return String.format("BasicGetter(%s.%s)", clazz.getName(), propertyName);
        }

        Object readResolve() throws PropertyNotFoundException, IntrospectionException {
            return createGetter(clazz, propertyName);
        }
    }
}
