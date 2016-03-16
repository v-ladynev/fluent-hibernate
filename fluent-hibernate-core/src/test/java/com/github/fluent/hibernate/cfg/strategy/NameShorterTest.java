package com.github.fluent.hibernate.cfg.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.github.fluent.hibernate.cfg.strategy.NameShorter;

/**
 *
 * @author V.Ladynev
 */
public class NameShorterTest {

    @Test
    public void tableName() {
        boolean dontTouchFirst = false;
        assertThat(create(23, dontTouchFirst).tableName("merchants_friends_customers")).isEqualTo(
                "merchnts_friends_cstmrs");
        assertThat(create(10, dontTouchFirst).tableName("merchants_friends")).isEqualTo(
                "mrchnts_friends");

        dontTouchFirst = true;
        assertThat(create(10, dontTouchFirst).tableName("merchants_friends")).isEqualTo(
                "merchants_friends");
    }

    private NameShorter create(int maxLength, boolean dontTouchFirst) {
        return new NameShorter(maxLength, dontTouchFirst);
    }

}
