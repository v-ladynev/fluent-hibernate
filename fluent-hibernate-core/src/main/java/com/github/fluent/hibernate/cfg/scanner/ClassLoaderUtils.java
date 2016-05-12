package com.github.fluent.hibernate.cfg.scanner;

import java.util.List;

import com.github.fluent.hibernate.internal.util.InternalUtils;
import com.github.fluent.hibernate.internal.util.InternalUtils.ClassUtils;

/**
 *
 * @author V.Ladynev
 */
final class ClassLoaderUtils {

    private ClassLoaderUtils() {

    }

    public static List<ClassLoader> defaultClassLoaders() {
        List<ClassLoader> result = InternalUtils.CollectionUtils.newArrayList();

        ClassLoader contextClassLoader = ClassUtils.contextClassLoader();
        ClassLoader staticClassLoader = ClassUtils.staticClassLoader();

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

}
