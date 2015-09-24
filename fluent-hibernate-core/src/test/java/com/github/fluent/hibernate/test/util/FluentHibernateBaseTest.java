package com.github.fluent.hibernate.test.util;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.HibernateSessionFactory;

/**
 *
 * @author V.Ladynev
 */
public class FluentHibernateBaseTest {

    @BeforeClass
    public static void initSessionFactory() {
        HibernateSessionFactory.Builder.configureFromHibernateCfgXml("hibernate-common.cfg.xml")
                .createSessionFactory();
    }

    @AfterClass
    public static void closeSessionFactory() {
        HibernateSessionFactory.closeSessionFactory();
    }

    public void clearUsers() {
        H.update("delete from User").execute();
    }

}
