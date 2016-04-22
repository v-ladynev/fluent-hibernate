package com.github.fluent.hibernate.test.util;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.github.fluent.hibernate.factory.HibernateSessionFactory;

/**
 *
 * @author V.Ladynev
 */
public class FluentHibernateBaseTest {

    @BeforeClass
    public static void initSessionFactory() {
        HibernateSessionFactory.Builder.configureFromDefaultHibernateCfgXml()
                .createSessionFactory();
    }

    @AfterClass
    public static void closeSessionFactory() {
        HibernateSessionFactory.closeSessionFactory();
    }

}
