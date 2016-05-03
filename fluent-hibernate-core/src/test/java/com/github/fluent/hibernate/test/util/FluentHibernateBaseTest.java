package com.github.fluent.hibernate.test.util;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.github.fluent.hibernate.cfg.Fluent;
import com.github.fluent.hibernate.cfg.FluentFactoryBuilder;

/**
 *
 * @author V.Ladynev
 */
public class FluentHibernateBaseTest {

    @BeforeClass
    public static void initSessionFactory() {
        FluentFactoryBuilder.configureFromDefaultHibernateCfgXml().build();
    }

    @AfterClass
    public static void closeSessionFactory() {
        Fluent.factory().close();
    }

}
