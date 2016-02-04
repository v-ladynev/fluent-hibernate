package com.github.fluent.hibernate.strategy;

import org.junit.Test;

/**
 *
 * @author V.Ladynev
 */
public class NameShorterTest {

    @Test
    public void tableName() {
        String name = create(23).tableName("merchants_friends_customers");
        System.out.println(name);
        System.out.println(name.length());
    }

    private Chuck create(int maxLength) {
        return new Chuck(maxLength);
    }

}
