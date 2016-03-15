package com.github.fluent.hibernate.cfg.scanner;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;

import com.github.fluent.hibernate.internal.util.InternalUtils;

/**
 *
 * @author V.Ladynev
 */
public final class EntityScanner {

    private AnnotationChecker checker;

    private List<ClassLoader> loaders;

    private final List<String> packagesToScan;

    private EntityScanner(String[] packagesToScan) {
        this.packagesToScan = packagesToScan == null ? Collections.<String> emptyList() : Arrays
                .asList(InternalUtils.CollectionUtils.correctOneNullToEmpty(packagesToScan));
    }

    /**
     * Scan packages for the @Entity annotation.
     *
     *
     * @param packages
     *            one or more Java package names
     *
     * @return entity classes
     */
    public static List<Class<?>> scanPackages(String... packages) {
        try {
            return scanPackages(packages, null, Entity.class);
        } catch (Exception ex) {
            throw InternalUtils.toRuntimeException(ex);
        }
    }

    static List<Class<?>> scanPackages(String[] packages, List<ClassLoader> loaders,
            Class<? extends Annotation> annotation) throws Exception {
        EntityScanner scanner = new EntityScanner(packages);
        scanner.loaders = loaders;
        return scanner.scan(annotation);
    }

    private List<Class<?>> scan(Class<? extends Annotation> annotation) throws Exception {
        checker = new AnnotationChecker(annotation);

        ClasspathScanner scanner = new ClasspathScanner(new ClasspathScanner.IClassAcceptor() {
            @Override
            public boolean accept(String classResource, ClassLoader loader) throws Exception {
                return checker.hasAnnotation(loader.getResourceAsStream(classResource));
            }
        });

        scanner.setPackagesToScan(packagesToScan);
        scanner.setLoaders(loaders);

        return scanner.scan();
    }

}
