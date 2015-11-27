package com.github.fluent.hibernate.request;

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
public class HibernateObjectQueryTest extends RootEnvironmentBaseTest {

    @Test
    public void save() {
        Root root = rootA();
        assertThat(H.save(root)).isSameAs(root);

        int count = createRequestForRootA().count();
        assertThat(count).isEqualTo(1);
    }

    @Test
    public void saveOrUpdate() {
        Root root = rootA();
        assertThat(H.saveOrUpdate(root)).isSameAs(root);

        int countNew = createRequestForRootA().count();
        assertThat(countNew).isEqualTo(1);

        H.saveOrUpdate(root);
        int countUpdated = createRequestForRootA().count();
        assertThat(countUpdated).isEqualTo(1);
    }

    @Test
    public void saveOrUpdateAll() {
        List<Root> roots = rootsAB();
        assertThat(H.saveOrUpdateAll(roots)).containsExactlyElementsOf(roots);

        int count = createRootRequest().count();
        assertThat(count).isEqualTo(2);
    }

    @Test
    public void saveAll() {
        List<Root> roots = rootsAB();
        assertThat(H.saveAll(roots)).containsExactlyElementsOf(roots);

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

    @Test
    public void deleteById() {
        H.saveOrUpdate(rootA());
        Root root = createRequestForRootA().first();
        assertThat(root).isNotNull();
        H.deleteById(Root.class, root.getPid());
        int count = createRequestForRootA().count();
        assertThat(count).isEqualTo(0);
    }

    @Test
    public void deleteAll() {
        H.saveAll(rootsAB());
        List<Root> list = createRootRequest().list();
        assertThat(list).hasSize(2);
        H.deleteAll(list);
        int count = createRootRequest().count();
        assertThat(count).isEqualTo(0);
    }

}
