package com.github.fluent.hibernate.internal.util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

/**
 * Utils for this library. For internal use only.
 *
 * @author V.Ladynev
 */
public final class InternalUtils {

    private InternalUtils() {

    }

    public static int hashCode(Object... objects) {
        return Arrays.hashCode(objects);
    }

    public static boolean equal(Object a, Object b) {
        return a == b || a != null && a.equals(b);
    }

    public static Object newInstance(Class<?> classToInstantiate) {
        try {
            return classToInstantiate.newInstance();
        } catch (Exception ex) {
            throw new RuntimeException(String.format("Could not instantiate result class: %s",
                    classToInstantiate.getName()), ex);
        }
    }

    public static void closeQuietly(InputStream stream) {
        try {
            stream.close();
        } catch (Exception ex) {

        }
    }

    public static RuntimeException toRuntimeException(Throwable throwable) {
        return throwable instanceof RuntimeException ? (RuntimeException) throwable
                : new RuntimeException(throwable);
    }

    public static class StringUtils {

        public static final String EMPTY = "";

        private StringUtils() {

        }

        public static String correctEmpty(final String value) {
            return correctEmpty(value, EMPTY);
        }

        public static String correctEmpty(final String value, final String valueForEmpty) {
            return isEmpty(value) ? valueForEmpty : value;
        }

        public static boolean isEmpty(final String value) {
            if (value == null) {
                return true;
            }

            int len = value.length();
            if (len == 0) {
                return true;
            }

            if (value.charAt(0) > ' ') {
                return false;
            }

            for (int i = 1; i < len; i++) {
                if (value.charAt(i) > ' ') {
                    return false;
                }
            }

            return true;
        }

    }

    public static final class CollectionUtils {

        private CollectionUtils() {

        }

        public static <T> T first(List<T> items) {
            return isEmpty(items) ? null : items.get(0);
        }

        public static boolean isEmpty(Collection<?> collection) {
            return collection == null || collection.isEmpty();
        }

        public static boolean isEmpty(Object[] array) {
            return array == null || array.length == 0;
        }

        public static <E> ArrayList<E> newArrayList() {
            return new ArrayList<E>();
        }

        public static <K, V> HashMap<K, V> newHashMap() {
            return new HashMap<K, V>();
        }

    }

    public static final class HibernateUtils {

        private HibernateUtils() {

        }

        public static void rollback(Transaction txn) {
            if (txn != null) {
                txn.rollback();
            }
        }

        public static void close(Session session) {
            if (session != null && session.isOpen()) {
                session.close();
            }
        }

        public static void close(StatelessSession session) {
            if (session != null) {
                session.close();
            }
        }

    }

}
