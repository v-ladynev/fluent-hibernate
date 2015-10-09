package com.github.fluent.hibernate.request;

import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.ROOT_NAME_A;
import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.createRequestForRootA;
import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.createRootRequest;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.test.persistent.Root;
import com.github.fluent.hibernate.test.util.RootEnvironmentBaseTest;

/**
 *
 * @author V.Ladynev
 */
public class HibernateRequestTest extends RootEnvironmentBaseTest
{

    @Test
    public void list() {
        H.saveOrUpdateAll(rootsAB());
        List<Root> roots = createRootRequest().list();
        Assert.assertNotNull(roots);
        Assert.assertEquals(2, roots.size());
    }

    @Test
    public void first() {
        H.saveOrUpdateAll(rootsAB());
        Root root = createRequestForRootA().first();
        Assert.assertNotNull(root);
        Assert.assertEquals(ROOT_NAME_A, root.getRootName());
    }

    @Test
    public void count() {
        H.saveOrUpdateAll(rootsAB());
        int count = H.request(Root.class).count();
        Assert.assertEquals(2, count);
    }

}
