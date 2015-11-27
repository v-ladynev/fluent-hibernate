package com.github.fluent.hibernate.factory;

import static org.assertj.core.api.Assertions.assertThat;

import org.hibernate.Session;
import org.junit.After;
import org.junit.Test;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.IRequest;
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

    @Test
    public void configureFromDefaultHibernateCfgXml() {
        HibernateSessionFactory.Builder.configureFromDefaultHibernateCfgXml()
        .createSessionFactory();
        assertSession();
        dealWithSimplyPersistent();
    }

    @Test
    public void configureWithoutHibernateCfgXml() {
        HibernateSessionFactory.Builder.configureWithoutHibernateCfgXml().userName("user")
        .password("").connectionUrl("jdbc:h2:mem:di;MODE=ORACLE")
        .annotatedClasses(SimplyPersistent.class).createSessionFactory();

        assertSession();
        dealWithSimplyPersistent();
    }

    private void assertSession() {
        HibernateSessionFactory.doInTransaction(new IRequest<Void>() {
            @Override
            public Void doInTransaction(Session session) {
                assertThat(session.isOpen()).isTrue();
                return null;
            }
        });
    }

    private void dealWithSimplyPersistent() {
        H.<SimplyPersistent> save(SimplyPersistent.createWithDefaultName());
        SimplyPersistent simplyPersistnet = H.<SimplyPersistent> request(SimplyPersistent.class)
                .first();
        assertThat(simplyPersistnet).isNotNull();
        assertThat(simplyPersistnet.getName()).isEqualTo(SimplyPersistent.DEFAULT_NAME);
    }

}
