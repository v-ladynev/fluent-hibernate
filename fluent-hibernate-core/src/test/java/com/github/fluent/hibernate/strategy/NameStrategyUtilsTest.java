package com.github.fluent.hibernate.strategy;

import static com.github.fluent.hibernate.strategy.NamingStrategyUtils.removeVowelsFromTheRight;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 *
 * @author V.Ladynev
 */
public class NameStrategyUtilsTest {

    @Test
    public void removeVowels() {
        assertThat(removeVowelsFromTheRight("aeiouAEIOU", 10)).isEmpty();
        assertThat(removeVowelsFromTheRight("aeiouAEIOU", 5)).isEqualTo("aeiou");
        assertThat(removeVowelsFromTheRight("a1e2i3o4u5A6E7I8O9U", 10)).isEqualTo("123456789");

        boolean dontRemoveFirst = true;
        boolean dontRemoveLast = false;
        assertThat(removeVowelsFromTheRight("aeiouAEIOU", 10, dontRemoveFirst, dontRemoveLast))
                .isEqualTo("a");
        assertThat(
                removeVowelsFromTheRight("a1e2i3o4u5A6E7I8O9U", 10, dontRemoveFirst, dontRemoveLast))
                .isEqualTo("a123456789");

        dontRemoveFirst = true;
        dontRemoveLast = true;
        assertThat(removeVowelsFromTheRight("aeiouAEIOU", 10, dontRemoveFirst, dontRemoveLast))
                .isEqualTo("aU");
        assertThat(
                removeVowelsFromTheRight("a1e2i3o4u5A6E7I8O9U", 10, dontRemoveFirst, dontRemoveLast))
                .isEqualTo("a123456789U");

        assertThat(removeVowelsFromTheRight("", 10, dontRemoveFirst, dontRemoveLast)).isEqualTo("");
        assertThat(removeVowelsFromTheRight("a", 10, dontRemoveFirst, dontRemoveLast)).isEqualTo(
                "a");
    }

}
