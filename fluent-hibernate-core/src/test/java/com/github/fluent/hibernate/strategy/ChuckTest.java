package com.github.fluent.hibernate.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 *
 * @author V.Ladynev
 */
public class ChuckTest {

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

    private Chuck create(int maxLength, boolean dontTouchFirst) {
        return new Chuck(maxLength, dontTouchFirst);
    }

}
