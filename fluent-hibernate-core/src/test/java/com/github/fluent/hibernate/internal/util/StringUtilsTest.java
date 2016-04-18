/**
 * $HeadURL$
 * $Revision$
 * $Date$
 *
 * Copyright (c) Isida-Informatica, Ltd. All Rights Reserved.
 */
package com.github.fluent.hibernate.internal.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.github.fluent.hibernate.internal.util.InternalUtils.StringUtils;

/**
 *
 * @author V.Ladynev
 * @version $Id$
 */
public class StringUtilsTest {

    @Test
    public void splitBySpace() {
        assertThat(StringUtils.splitBySpace(null)).hasSize(0);
        assertThat(StringUtils.splitBySpace("")).hasSize(0);
        assertThat(StringUtils.splitBySpace("  ")).hasSize(0);
        assertThat(StringUtils.splitBySpace("a")).containsExactly("a");
        assertThat(StringUtils.splitBySpace("a  b")).containsExactly("a", "b");
        assertThat(StringUtils.splitBySpace(" a  b ")).containsExactly("a", "b");
    }

    @Test
    public void join() {
        assertThat(StringUtils.join("a", "b", "_")).isEqualTo("a_b");
        assertThat(StringUtils.join(null, "b", "_")).isEqualTo("b");
        assertThat(StringUtils.join("a", null, "_")).isEqualTo("a");
    }

}
