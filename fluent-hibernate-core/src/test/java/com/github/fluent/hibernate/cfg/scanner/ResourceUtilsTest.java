package com.github.fluent.hibernate.cfg.scanner;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 *
 * @author V.Ladynev
 */
public class ResourceUtilsTest {

    @Test
    public void hasClassExtension() {
        assertThat(ResourceUtils.hasClassExtension("SomeClass.class")).isTrue();
        assertThat(ResourceUtils.hasClassExtension("some_path")).isFalse();
    }

}
