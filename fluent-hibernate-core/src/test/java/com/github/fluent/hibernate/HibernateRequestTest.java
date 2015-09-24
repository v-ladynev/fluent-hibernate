package com.github.fluent.hibernate;

import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.USER_A;
import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.USER_B;

import java.util.List;

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
public class HibernateRequestTest extends FluentHibernateBaseTest {

    @Before
    public void init() {
        clearUsers();
    }

    @Test
    public void list() {
        H.saveOrUpdate(USER_A);
        H.saveOrUpdate(USER_B);
        List<User> users = FluentHibernateTestData.createUserRequest().list();
        Assert.assertNotNull(users);
        Assert.assertEquals(2, users.size());
    }

    @Test
    public void first() {
        H.saveOrUpdate(USER_A);
        H.saveOrUpdate(USER_B);
        User user = FluentHibernateTestData.createRequestForUserA().first();
        Assert.assertNotNull(user);
        Assert.assertEquals(USER_A.getLogin(), user.getLogin());
    }

    @Test
    public void count() {
        H.saveOrUpdate(USER_A);
        H.saveOrUpdate(USER_B);
        int count = H.request(User.class).count();
        Assert.assertEquals(2, count);
    }

}
