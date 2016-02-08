package com.github.fluent.hibernate.strategy;

import static com.github.fluent.hibernate.strategy.NamingStrategyUtils.removeVowelsSmart;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 *
 * @author V.Ladynev
 */
public class NamingStrategyUtilsTest {

    @Test
    public void removeVowels() {
        assertThat(removeVowelsSmart("aeiouAEIOU", 10)).isEqualTo("aeiouAEIOU");
        assertThat(removeVowelsSmart("a1e2i3o4u5A6E7I8O9U", 10)).isEqualTo("a123456789U");

        assertThat(removeVowelsSmart("", 10)).isEqualTo("");
        assertThat(removeVowelsSmart("a", 10)).isEqualTo("a");
        assertThat(removeVowelsSmart("ae", 10)).isEqualTo("ae");

        assertThat(removeVowelsSmart("aeb", 10)).isEqualTo("aeb");
        assertThat(removeVowelsSmart("bae", 10)).isEqualTo("bae");
        assertThat(removeVowelsSmart("aebae", 10)).isEqualTo("aebae");
        assertThat(removeVowelsSmart("aeeb", 10)).isEqualTo("aeeb");
        assertThat(removeVowelsSmart("friends", 10)).isEqualTo("friends");
    }

}
