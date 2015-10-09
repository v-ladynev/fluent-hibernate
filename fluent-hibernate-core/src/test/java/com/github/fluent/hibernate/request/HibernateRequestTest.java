package com.github.fluent.hibernate.request;

import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.ROOT_NAME_A;
import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.createRequestForRootA;
import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.createRootRequest;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.test.persistent.Root;
import com.github.fluent.hibernate.test.util.RootEnvironmentBaseTest;

/**
 *
 * @author V.Ladynev
 */
public class HibernateRequestTest extends RootEnvironmentBaseTest {

    @Test
    public void list() {
        H.saveOrUpdateAll(rootsAB());
        List<Root> roots = createRootRequest().list();
        assertThat(roots).isNotNull().hasSize(2);
    }

    @Test
    public void first() {
        H.saveOrUpdateAll(rootsAB());
        Root root = createRequestForRootA().first();
        assertThat(root).isNotNull();
        assertThat(root.getRootName()).isEqualTo(ROOT_NAME_A);
    }

    @Test
    public void count() {
        H.saveOrUpdateAll(rootsAB());
        int count = H.request(Root.class).count();
        assertThat(count).isEqualTo(2);
    }

}
