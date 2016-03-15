package com.github.fluent.hibernate.cfg.scanner;

import java.util.List;

import com.github.fluent.hibernate.internal.util.InternalUtils;

/**
 *
 * @author V.Ladynev
 */
final class ClassLoaderUtils {

    private ClassLoaderUtils() {

    }

    public static Class<?> classForName(String className, ClassLoader loader) {
        try {
            return Class.forName(className, true, loader);
        } catch (ClassNotFoundException ex) {
            throw InternalUtils.toRuntimeException(ex);
        }
    }

    public static List<ClassLoader> defaultClassLoaders() {
        List<ClassLoader> result = InternalUtils.CollectionUtils.newArrayList();

        ClassLoader contextClassLoader = contextClassLoader();
        ClassLoader staticClassLoader = staticClassLoader();

        add(result, contextClassLoader);
        if (contextClassLoader != staticClassLoader) {
            add(result, staticClassLoader);
        }

        return result;
    }

    private static void add(List<ClassLoader> result, ClassLoader loader) {
        if (loader != null) {
            result.add(loader);
        }
    }

    private static ClassLoader contextClassLoader() {
        return Thread.currentThread().getContextClassLoader();
    }

    private static ClassLoader staticClassLoader() {
        return ResourceUtils.class.getClassLoader();
    }

}
