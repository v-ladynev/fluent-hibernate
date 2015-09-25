package com.github.fluent.hibernate;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;

import com.github.fluent.hibernate.test.persistent.SimplyPersistent;

/**
 *
 * @author V.Ladynev
 */
public class HibernateSessionFactoryTest {

    @After
    public void afterEachTest() {
        HibernateSessionFactory.closeSessionFactory();
    }

    // @Test
    public void configureFromDefaultHibernateCfgXml() {
        HibernateSessionFactory.Builder.configureFromDefaultHibernateCfgXml()
        .createSessionFactory();
        assertSession();
    }

    private void assertSession() {
        HibernateSessionFactory.doInTransaction(new IRequest<Void>() {
            @Override
            public Void doInTransaction(Session session) {
                Assert.assertTrue(session.isOpen());
                return null;
            }
        });
    }

    @Test
    public void configureWithoutHibernateCfgXml() {
        HibernateSessionFactory.Builder.configureWithoutHibernateCfgXml().userName("user")
                .password("").connectionUrl("jdbc:h2:mem:di;MODE=ORACLE")
                .annotatedClasses(SimplyPersistent.class).createSessionFactory();

        assertSession();

        H.<SimplyPersistent> save(SimplyPersistent.createWithDefaultName());
        SimplyPersistent simplyPersistnet = H.<SimplyPersistent> request(SimplyPersistent.class)
                .first();
        Assert.assertNotNull(simplyPersistnet);
        Assert.assertEquals(SimplyPersistent.DEFAULT_NAME, simplyPersistnet.getName());
    }

}
