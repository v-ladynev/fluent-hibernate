package com.github.fluent.hibernate.strategy;

import org.junit.Test;

/**
 *
 * @author V.Ladynev
 */
public class NameShorterTest {

    @Test
    public void tableName() {
        System.out.println(create(23).tableName("merchants_friends_customers"));
    }

    private Chuck create(int maxLength) {
        return new Chuck(maxLength);
    }

}
