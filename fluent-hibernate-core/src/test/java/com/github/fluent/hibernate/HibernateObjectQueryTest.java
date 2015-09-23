package com.github.fluent.hibernate;

import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.USER_A;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.fluent.hibernate.test.persistent.User;
import com.github.fluent.hibernate.test.util.FluentHibernateBaseTest;
import com.github.fluent.hibernate.test.util.FluentHibernateTestData;

/**
 *
 * @author V.Ladynev
 */
public class HibernateObjectQueryTest extends FluentHibernateBaseTest {

    @Before
    public void init() {
        clearUsers();
    }

    @Test
    public void saveOrUpdate() {
        H.saveOrUpdate(USER_A);
        int count = FluentHibernateTestData.cerateRequestForUserA().count();
        Assert.assertEquals(1, count);
    }

    @Test
    public void delete() {
        H.saveOrUpdate(USER_A);
        User user = FluentHibernateTestData.cerateRequestForUserA().first();
        Assert.assertNotNull(user);
        H.delete(user);
        int count = FluentHibernateTestData.cerateRequestForUserA().count();
        Assert.assertEquals(0, count);
    }

}
