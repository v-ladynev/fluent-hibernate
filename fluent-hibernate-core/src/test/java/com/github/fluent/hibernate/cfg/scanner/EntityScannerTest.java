package com.github.fluent.hibernate.cfg.scanner;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

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

    private static final Class<?>[] ENTITY_CLASSES = new Class<?>[] { FirstRootEntity.class,
        FirstRootEntity.NestedEntity.class, SecondRootEntity.class, FirstSubpackageEntity.class };

    private static final Class<?>[] OTHER_ENTITY_CLASSES = new Class<?>[] { OtherRootEntity.class,
            OtherRootEntity.OtherNestedEntity.class };

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

}
