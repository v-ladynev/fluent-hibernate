package com.github.fluent.hibernate.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

/**
 *
 * @author V.Ladynev
 */
public class NameShorterTest {

    @Test
    public void tableName() {
        assertThat(create(23).tableName("merchants_friends_customers")).isEqualTo(
                "merchants_frinds_cstmrs");
    }

    private Chuck create(int maxLength) {
        return new Chuck(maxLength);
    }

}
