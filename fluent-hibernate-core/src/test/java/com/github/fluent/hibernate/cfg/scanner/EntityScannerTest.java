package com.github.fluent.hibernate.cfg.scanner;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import com.github.fluent.hibernate.cfg.scanner.other.persistent.OtherRootEntity;
import com.github.fluent.hibernate.cfg.scanner.persistent.FirstRootEntity;
import com.github.fluent.hibernate.cfg.scanner.persistent.SecondRootEntity;
import com.github.fluent.hibernate.cfg.scanner.persistent.subpackage.FirstSubpackageEntity;

/**
 *
 * @author V.Ladynev
 */
public class EntityScannerTest {

    private static final String OTHER_PACKAGE = "com.github.fluent.hibernate.cfg.scanner.other.persistent";

    private static final Class<?>[] ENTITY_CLASSES = new Class<?>[] { FirstRootEntity.class,
            FirstRootEntity.NestedEntity.class, SecondRootEntity.class, FirstSubpackageEntity.class };

    private static final Class<?>[] OTHER_ENTITY_CLASSES = new Class<?>[] { OtherRootEntity.class,
        OtherRootEntity.OtherNestedEntity.class };

    // @Test
    public void scanOnePackage() {
        List<Class<?>> classes = EntityScanner.scanPackages(OTHER_PACKAGE);
        assertThat(classes).contains(OTHER_ENTITY_CLASSES).doesNotContain(ENTITY_CLASSES);
    }

}
