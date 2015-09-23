package com.github.fluent.hibernate;

import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author V.Ladynev
 */
public class HibernateSessionFactoryTest {

    @BeforeClass
    public static void init() {
        HibernateSessionFactory.Builder.configureFromDefaultHibernateCfgXml()
                .createSessionFactory();
    }

    @AfterClass
    public static void tearDown() {
        HibernateSessionFactory.closeSessionFactory();
    }

    @Test
    public void createSessionFactory() {
        HibernateSessionFactory.doInTransaction(new IRequest<Void>() {
            @Override
            public Void doInTransaction(Session session) {
                Assert.assertTrue(session.isOpen());
                return null;
            }
        });
    }

}
