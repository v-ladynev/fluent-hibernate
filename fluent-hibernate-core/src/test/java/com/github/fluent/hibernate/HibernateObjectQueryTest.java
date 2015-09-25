package com.github.fluent.hibernate;

import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.createRequestForRootA;
import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.createRootRequest;

import org.junit.Assert;
import org.junit.Test;

import com.github.fluent.hibernate.test.persistent.Root;
import com.github.fluent.hibernate.test.util.FluentHibernateBaseTest;

/**
 *
 * @author V.Ladynev
 */
public class HibernateObjectQueryTest extends FluentHibernateBaseTest {

    @Test
    public void save() {
        H.save(rootA());
        int count = createRequestForRootA().count();
        Assert.assertEquals(1, count);
    }

    @Test
    public void saveOrUpdate() {
        Root root = rootA();
        H.saveOrUpdate(root);
        int countNew = createRequestForRootA().count();
        Assert.assertEquals(1, countNew);
        H.saveOrUpdate(root);
        int countUpdated = createRequestForRootA().count();
        Assert.assertEquals(1, countUpdated);
    }

    @Test
    public void saveOrUpdateAll() {
        H.saveOrUpdateAll(rootsAB());
        int count = createRootRequest().count();
        Assert.assertEquals(2, count);
    }

    @Test
    public void saveAll() {
        H.saveAll(rootsAB());
        int count = createRootRequest().count();
        Assert.assertEquals(2, count);
    }

    @Test
    public void delete() {
        H.saveOrUpdate(rootA());
        Root root = createRequestForRootA().first();
        Assert.assertNotNull(root);
        H.delete(root);
        int count = createRequestForRootA().count();
        Assert.assertEquals(0, count);
    }

}
