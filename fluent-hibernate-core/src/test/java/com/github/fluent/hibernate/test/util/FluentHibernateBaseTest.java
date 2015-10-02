package com.github.fluent.hibernate.test.util;

import com.github.fluent.hibernate.HibernateSessionFactory;
import org.junit.AfterClass;
import org.junit.BeforeClass;

/**
 *
 * @author V.Ladynev
 */
public class FluentHibernateBaseTest
{

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
