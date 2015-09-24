package com.github.fluent.hibernate;

import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.USERS_AB;
import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.USER_A;
import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.USER_B;

import java.util.Arrays;

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
        int count = FluentHibernateTestData.createRequestForUserA().count();
        Assert.assertEquals(1, count);
    }

    @Test
    public void saveOrUpdateAll() {
        H.saveOrUpdateAll(USERS_AB);
        int count = FluentHibernateTestData.createUserRequest().count();
        Assert.assertEquals(2, count);
    }

    @Test
    public void saveAll() {
        H.saveAll(Arrays.asList(USER_A, USER_B));
        int count = FluentHibernateTestData.createUserRequest().count();
        Assert.assertEquals(2, count);
    }

    @Test
    public void delete() {
        H.saveOrUpdate(USER_A);
        User user = FluentHibernateTestData.createRequestForUserA().first();
        Assert.assertNotNull(user);
        H.delete(user);
        int count = FluentHibernateTestData.createRequestForUserA().count();
        Assert.assertEquals(0, count);
    }

}
