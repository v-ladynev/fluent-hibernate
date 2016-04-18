package com.github.fluent.hibernate.cfg.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 *
 * @author V.Ladynev
 */
public class NameShorterTest {

    @Test
    public void tableName() {
        boolean dontTouchFirst = false;
        assertThat(create(21, dontTouchFirst).tableName("merchants_friends_customers"))
                .isEqualTo("merchnts_frnds_cstmrs");
        assertThat(create(8, dontTouchFirst).tableName("merchants_friends"))
                .isEqualTo("mrchnts_frnds");

        dontTouchFirst = true;
        assertThat(create(10, dontTouchFirst).tableName("merchants_friends"))
                .isEqualTo("merchants_frnds");
    }

    private NameShorter create(int maxLength, boolean dontTouchFirst) {
        return new NameShorter(maxLength, dontTouchFirst);
    }

}
