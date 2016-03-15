package com.github.fluent.hibernate.cfg.scanner;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.InputStream;
import java.net.URLClassLoader;

import javax.persistence.Entity;

import org.junit.BeforeClass;
import org.junit.Test;

import com.github.fluent.hibernate.cfg.scanner.jar.persistent.FirstRootEntityJar;
import com.github.fluent.hibernate.cfg.scanner.jar.persistent.NotEntityJar;
import com.github.fluent.hibernate.cfg.scanner.persistent.FirstRootEntity;
import com.github.fluent.hibernate.cfg.scanner.persistent.NotEntity;

/**
 *
 * @author V.Ladynev
 */
public class AnnotationsCheckerTest {

    private static AnnotationChecker annotationChecker;

    @BeforeClass
    public static void init() {
        annotationChecker = new AnnotationChecker(Entity.class);
    }

    @Test
    public void checkInPackage() throws Exception {
        assertThat(check(FirstRootEntity.class)).isTrue();
        assertThat(check(FirstRootEntity.NestedEntity.class)).isTrue();
        assertThat(check(NotEntity.class)).isFalse();
    }

    @Test
    public void checkInJar() throws Exception {
        URLClassLoader loader = ScannerTestUtils.createClassLoader(null,
                ScannerTestUtils.writeTestJar());
        assertThat(check(FirstRootEntityJar.class, loader)).isTrue();
        assertThat(check(FirstRootEntityJar.NestedEntityJar.class, loader)).isTrue();
        assertThat(check(NotEntityJar.class, loader)).isFalse();
    }

    private static boolean check(Class<?> clazz) throws Exception {
        return annotationChecker.hasAnnotation(classAsStream(clazz));
    }

    private static InputStream classAsStream(Class<?> clazz) {
        return clazz.getResourceAsStream("/" + ResourceUtils.classAsResource(clazz));
    }

    private static boolean check(Class<?> clazz, ClassLoader loader) throws Exception {
        return annotationChecker.hasAnnotation(classAsStream(clazz, loader));
    }

    private static InputStream classAsStream(Class<?> clazz, ClassLoader loader) {
        return loader.getResourceAsStream(ResourceUtils.classAsResource(clazz));
    }

}
