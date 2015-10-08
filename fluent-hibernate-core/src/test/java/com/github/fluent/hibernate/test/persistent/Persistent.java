package com.github.fluent.hibernate.test.persistent;

import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;

import org.hibernate.Hibernate;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;

/**
 *
 * @param <ID>
 *            id type
 * @author V.Ladynev
 */
@MappedSuperclass
public abstract class Persistent<ID> implements IPersistent<ID> {

    public static final String PID = "pid";

    private static final String GET_PID_METHOD = "getPid";

    private static final long serialVersionUID = -2619790764758996431L;

    protected ID pid;

    public void setPid(final ID pid) {
        this.pid = pid;
    }

    @Transient
    public boolean isExists() {
        return getPid(this) != null;
    }

    @Transient
    public String getPidAsString() {
        return getPidAsString(this);
    }

    @Transient
    public static String getPidAsString(Persistent<?> persistent) {
        return toString(getPid(persistent));
    }

    /**
     * Возвращает идентификатор переданного персистентного объекта. Работает для обычных и
     * lazy-объектов. Всегда следует использовать этот метод для получения идентификатора, так как
     * поле pid для lazy объектов равно null. А метод getPid() для lazy объектов может выбрасывать
     * LazyInitializationException, если вызывать этот метод через объект, объявленный как генерик.
     *
     * @param persistent
     *            персистентный объект, идентификатор которого надо получить, или null
     * @return идентификатор, или null
     */
    @SuppressWarnings("unchecked")
    @Transient
    public static <T> T getPid(Persistent<?> persistent) {
        if (persistent == null) {
            return null;
        }

        if (!(persistent instanceof HibernateProxy) || Hibernate.isInitialized(persistent)) {
            return (T) persistent.getPid();
        }

        LazyInitializer initializer = ((HibernateProxy) persistent).getHibernateLazyInitializer();
        return (T) initializer.getIdentifier();
    }

    /**
     * Извлекает идентификатор правильного типа из строки и устанавливает в персистентном объекте.
     *
     * Для корректной работы этого метода в персистентном объекте должен быть перекрыт метод getPid
     * без использования generics (т.е. у него должен быть указан конкретный класс возвращаемого
     * идентификатора Long/Integer/String, а не ID).
     *
     * @param pidString
     *            строка с идентификатором
     */
    public void setPidAsString(final String pidString) {
        setPidAsString(this, pidString);
    }

    /**
     * Извлекает идентификатор правильного типа из строки и устанавливает в персистентном объекте.
     *
     * Для корректной работы этого метода в персистентном объекте должен быть перекрыт метод getPid
     * без использования generics (т.е. у него должен быть указан конкретный класс возвращаемого
     * идентификатора Long/Integer/String, а не ID).
     *
     * @param persistent
     *            персистентный объект
     * @param pidString
     *            строка с идентификатором
     */
    @SuppressWarnings("unchecked")
    public static <ID> void setPidAsString(Persistent<ID> persistent, String pidString) {
        Class<?> clazz = persistent.getPidClass();

        if (clazz == null) {
            return;
        }

        if (Long.class.equals(clazz)) {
            persistent.pid = (ID) toLong(pidString, null);
        } else if (Integer.class.equals(clazz)) {
            persistent.pid = (ID) toInteger(pidString, null);
        } else if (String.class.equals(clazz)) {
            persistent.pid = (ID) pidString;
        } else {
            throw new IllegalArgumentException("Can't set pid '" + pidString + "' with class '"
                    + clazz + "' for persistent object '" + persistent + "' with class '"
                    + persistent.getClass() + "'");
        }
    }

    /**
     * Этим методом нельзя пользоваться для lazy объектов. Для lazy объектов использовать
     * статическую версию метода.
     *
     * @return true если у объекта неправильный идентификатор (null или отрицательное число)
     */
    @Transient
    public boolean isPidNotValid() {
        return !isPidValid(this);
    }

    /**
     * Этим методом нельзя пользоваться для lazy объектов. Для lazy объектов использовать
     * статическую версию метода.
     *
     * @return true если у объекта правильный идентификатор (не null, неотрицательное число для
     *         целочисленных)
     */
    @Transient
    public boolean isPidValid() {
        return isPidValid(this);
    }

    /**
     * @param persistent
     *            объект
     * @return true если у объекта правильный идентификатор (не null, неотрицательное число для
     *         целочисленных)
     */
    @Transient
    public static boolean isPidValid(Persistent<?> persistent) {
        Object pid = getPid(persistent);
        if (pid == null) {
            return false;
        }

        if (pid instanceof Number) {
            return isPidValid(((Number) pid).longValue());
        }

        if (pid instanceof String) {
            return isPidValid((String) pid);
        }

        return true;
    }

    @Transient
    public static boolean isPidValid(Long pid) {
        return pid != null && pid >= 0;
    }

    @Transient
    public static boolean isPidValid(String pid) {
        if (isEmpty(pid)) {
            return false;
        }
        // для строковых pid может быть значение "-1"
        Long longPid = toLong(pid, null);
        return longPid == null || isPidValid(longPid);
    }

    @Transient
    public static Class<?> getPidClass(Class<?> clazz) {
        return getGetterReturnType(clazz, GET_PID_METHOD);
    }

    @Transient
    private Class<?> getPidClass() {
        return getGetterReturnType(getClass(), GET_PID_METHOD);
    }

    private static Class<?> getGetterReturnType(final Class<?> clazz, final String getterName) {
        try {
            return clazz.getMethod(getterName, (Class<?>[]) null).getReturnType();
        } catch (SecurityException e) {
            return null;
        } catch (NoSuchMethodException e) {
            return null;
        }
    }

    private static String toString(final Object object) {
        return object == null ? null : object.toString();
    }

    private static Integer toInteger(final String str, final Integer defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Integer.valueOf(str.trim());
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    private static Long toLong(final String str, final Long defaultValue) {
        if (str == null) {
            return defaultValue;
        }
        try {
            return Long.valueOf(str.trim());
        } catch (NumberFormatException nfe) {
            return defaultValue;
        }
    }

    private static boolean isEmpty(final String value) {
        return value == null || value.trim().length() == 0;
    }

}
