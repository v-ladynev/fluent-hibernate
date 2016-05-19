package com.github.fluent.hibernate.cfg.scanner;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;

import org.hibernate.cfg.Configuration;

import com.github.fluent.hibernate.internal.util.InternalUtils;
import com.github.fluent.hibernate.internal.util.InternalUtils.Asserts;
import com.github.fluent.hibernate.internal.util.InternalUtils.ClassUtils;
import com.github.fluent.hibernate.internal.util.InternalUtils.CollectionUtils;

/**
 *
 * @author V.Ladynev
 */
public final class EntityScanner {

    private AnnotationChecker checker;

    private List<ClassLoader> loaders;

    private final String[] packagesToScan;

    private List<Class<?>> result = CollectionUtils.newArrayList();

    private EntityScanner(String[] packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    /**
     * Scan all the class path for the @Entity annotation. It scans JRE libraries too, so this
     * method recommended to use only for the test purposes.
     *
     * @return EntityScanner for fluent calls
     */
    public static EntityScanner scanAllPackages() {
        return scanPackages(null, null, Entity.class);
    }

    /**
     * Scan packages for the @Entity annotation.
     *
     * @param packages
     *            one or more Java package names
     *
     * @return EntityScanner for fluent calls
     */
    public static EntityScanner scanPackages(String... packages) {
        Asserts.isTrue(!CollectionUtils.isEmptyEllipsis(packages),
                "You should to specify almost one package to scan.");
        return scanPackages(packages, null, Entity.class);
    }

    private static EntityScanner scanPackages(String[] packages, List<ClassLoader> loaders,
            Class<? extends Annotation> annotation) {
        try {
            return scanPackagesInternal(packages, null, Entity.class);
        } catch (Exception ex) {
            throw InternalUtils.toRuntimeException(ex);
        }
    }

    static EntityScanner scanPackagesInternal(String[] packages, List<ClassLoader> loaders,
            Class<? extends Annotation> annotation) throws Exception {
        EntityScanner scanner = new EntityScanner(packages);
        scanner.loaders = loaders;
        scanner.scan(annotation);
        return scanner;
    }

    private void scan(Class<? extends Annotation> annotation) throws Exception {
        checker = new AnnotationChecker(annotation);

        ClasspathScanner scanner = new ClasspathScanner(new ClasspathScanner.IResourceAcceptor() {
            @Override
            public void accept(String resource, ClassLoader loader) throws Exception {
                addClassToResult(resource, loader);
            }
        });

        if (packagesToScan == null) {
            scanner.allPackagesToScan();
        } else {
            scanner.setPackagesToScan(Arrays.asList(packagesToScan));
        }

        scanner.setLoaders(loaders);

        scanner.scan();
    }

    private void addClassToResult(String resource, ClassLoader loader) throws IOException {
        if (!ResourceUtils.hasClassExtension(resource)) {
            return;
        }

        // in JDK 8 getResourceAsStream() returns null for version.rc
        if (!checker.hasAnnotation(loader.getResourceAsStream(resource))) {
            return;
        }

        Class<?> clazz = ClassUtils.classForName(ResourceUtils.getClassNameFromPath(resource),
                loader);
        result.add(clazz);
    }

    /**
     * Adds scanned persistents to the Hibernate configuration.
     *
     * @param configuration
     *            a Hibernate configuration
     *
     * @return EntityScanner for fluent calls
     */
    public EntityScanner addTo(Configuration configuration) {
        for (Class<?> annotatedClass : result) {
            configuration.addAnnotatedClass(annotatedClass);
        }

        return this;
    }

    public List<Class<?>> result() {
        return result;
    }

}
