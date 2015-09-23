package com.github.fluent.hibernate;

import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Test;

import com.github.fluent.hibernate.test.util.FluentHibernateBaseTest;

/**
 *
 * @author V.Ladynev
 */
public class HibernateSessionFactoryTest extends FluentHibernateBaseTest {

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
