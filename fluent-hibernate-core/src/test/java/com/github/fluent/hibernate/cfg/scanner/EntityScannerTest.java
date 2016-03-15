package com.github.fluent.hibernate.cfg.scanner;

import static org.assertj.core.api.Assertions.assertThat;

import java.lang.annotation.Annotation;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.persistence.Entity;

import org.junit.Test;

import com.github.fluent.hibernate.cfg.scanner.jar.persistent.FirstRootEntityJar;
import com.github.fluent.hibernate.cfg.scanner.jar.persistent.NotEntityJar;
import com.github.fluent.hibernate.cfg.scanner.jar.persistent.SecondRootEntityJar;
import com.github.fluent.hibernate.cfg.scanner.jar.persistent.subpackage.FirstSubpackageEntityJar;
import com.github.fluent.hibernate.cfg.scanner.other.persistent.OtherRootEntity;
import com.github.fluent.hibernate.cfg.scanner.persistent.FirstRootEntity;
import com.github.fluent.hibernate.cfg.scanner.persistent.NotEntity;
import com.github.fluent.hibernate.cfg.scanner.persistent.SecondRootEntity;
import com.github.fluent.hibernate.cfg.scanner.persistent.subpackage.FirstSubpackageEntity;

/**
 *
 * @author V.Ladynev
 */
public class EntityScannerTest {

    private static final String PERSISTENT_PACKAGE = "com.github.fluent.hibernate.cfg.scanner.persistent";

    private static final String PERSISTENT_SUBPACKAGE = "com.github.fluent.hibernate.cfg.scanner.persistent.subpackage";

    private static final String OTHER_PERSISTENT_PACKAGE = "com.github.fluent.hibernate.cfg.scanner.other.persistent";

    private static final String JAR_PACKAGE = "com.github.fluent.hibernate.cfg.scanner.jar.persistent";

    private static final Class<?>[] ENTITY_CLASSES = new Class<?>[] { FirstRootEntity.class,
            FirstRootEntity.NestedEntity.class, SecondRootEntity.class, FirstSubpackageEntity.class };

    private static final Class<?>[] OTHER_ENTITY_CLASSES = new Class<?>[] { OtherRootEntity.class,
        OtherRootEntity.OtherNestedEntity.class };

    private static final Class<?>[] JAR_ENTITY_CLASSES = new Class<?>[] { FirstRootEntityJar.class,
        FirstRootEntityJar.NestedEntityJar.class, SecondRootEntityJar.class,
        FirstSubpackageEntityJar.class };

    @Test
    public void scanOnePackage() {
        List<Class<?>> classes = EntityScanner.scanPackages(OTHER_PERSISTENT_PACKAGE);
        assertThat(classes).containsOnlyOnce(OTHER_ENTITY_CLASSES).doesNotContain(ENTITY_CLASSES)
        .doesNotContain(NotEntity.class);
    }

    @Test
    public void scanAllPackages() {
        List<Class<?>> classes = EntityScanner.scanPackages(PERSISTENT_PACKAGE,
                OTHER_PERSISTENT_PACKAGE);
        assertThat(classes).containsOnlyOnce(ENTITY_CLASSES).containsOnlyOnce(OTHER_ENTITY_CLASSES)
                .doesNotContain(NotEntity.class);
    }

    @Test
    public void scanOverlappedPackages() {
        List<Class<?>> classes = EntityScanner.scanPackages(PERSISTENT_PACKAGE,
                PERSISTENT_SUBPACKAGE);
        assertThat(classes).containsOnlyOnce(ENTITY_CLASSES).doesNotContain(NotEntity.class);
    }

    @Test
    public void scanInJar() throws Exception {
        URLClassLoader loader = createDynJarClassLoader(ScannerTestUtils.writeTestJar());

        Class<? extends Annotation> entityAnnotation = (Class<? extends Annotation>) loader
                .loadClass(Entity.class.getName());
        assertThat(entityAnnotation).isNotNull();

        List<Class<?>> classes = EntityScanner.scanPackages(new String[] { JAR_PACKAGE },
                Arrays.<ClassLoader> asList(loader), entityAnnotation);

        assertThat(classes).contains(reload(loader, JAR_ENTITY_CLASSES)).doesNotContain(
                NotEntityJar.class);
    }

    private static URLClassLoader createDynJarClassLoader(URL jarFile) throws Exception {
        URL jpaJar = ScannerTestUtils.urlForJar("hibernate-jpa-2.1-api-1.0.0.Final.jar");
        assertThat(jpaJar).isNotNull();

        ClassLoader parent = null;
        return ScannerTestUtils.createClassLoader(parent, jarFile.toURI().toURL(), jpaJar);
    }

    private static Class<?>[] reload(ClassLoader loader, Class<?>... classes) throws Exception {
        ArrayList<Class<?>> result = new ArrayList<Class<?>>(classes.length);
        for (Class<?> clazz : classes) {
            result.add(loader.loadClass(clazz.getName()));
        }

        return result.toArray(new Class<?>[result.size()]);
    }

}
