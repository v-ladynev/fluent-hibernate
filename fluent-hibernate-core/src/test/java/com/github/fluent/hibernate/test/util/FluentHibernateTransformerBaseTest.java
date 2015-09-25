package com.github.fluent.hibernate.test.util;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.github.fluent.hibernate.HibernateSessionFactory;

/**
 *
 * @author V.Ladynev
 */
public class FluentHibernateTransformerBaseTest {

    @BeforeClass
    public static void initSessionFactory() {
        HibernateSessionFactory.Builder.configureFromHibernateCfgXml(
                "hibernate-transformer.cfg.xml").createSessionFactory();
    }

    @AfterClass
    public static void closeSessionFactory() {
        HibernateSessionFactory.closeSessionFactory();
    }

}
