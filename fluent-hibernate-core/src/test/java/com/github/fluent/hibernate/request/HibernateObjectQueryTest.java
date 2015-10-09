package com.github.fluent.hibernate.request;

import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.createRequestForRootA;
import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.createRootRequest;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.test.persistent.Root;
import com.github.fluent.hibernate.test.util.RootEnvironmentBaseTest;

/**
 *
 * @author V.Ladynev
 */
public class HibernateObjectQueryTest extends RootEnvironmentBaseTest {

    @Test
    public void save() {
        H.save(rootA());
        int count = createRequestForRootA().count();
        assertThat(count).isEqualTo(1);
    }

    @Test
    public void saveOrUpdate() {
        Root root = rootA();
        H.saveOrUpdate(root);
        int countNew = createRequestForRootA().count();
        assertThat(countNew).isEqualTo(1);

        H.saveOrUpdate(root);
        int countUpdated = createRequestForRootA().count();
        assertThat(countUpdated).isEqualTo(1);
    }

    @Test
    public void saveOrUpdateAll() {
        H.saveOrUpdateAll(rootsAB());
        int count = createRootRequest().count();
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void saveAll() {
        H.saveAll(rootsAB());
        int count = createRootRequest().count();
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void delete() {
        H.saveOrUpdate(rootA());
        Root root = createRequestForRootA().first();
        assertThat(root).isNotNull();
        H.delete(root);
        int count = createRequestForRootA().count();
        assertThat(count).isEqualTo(0);
    }

}
