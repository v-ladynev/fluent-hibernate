package com.github.fluent.hibernate.cfg;

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
        Fluent.factory().close();
    }

    @Test
    public void configureFromDefaultHibernateCfgXml() {
        Fluent.factory().build();
        assertSession();
        dealWithSimplyPersistent();
    }

    @Test
    public void configureWithoutHibernateCfgXml() {
        // TODO hibernate.properties has the same
        Fluent.factory().dontUseHibernateCfgXml().database(DatabaseOptions.H2_ORACLE_MODE)
                .annotatedClasses(SimplyPersistent.class).build();

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
