package com.github.fluent.hibernate;

import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.userA;
import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.userB;

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
        H.saveOrUpdate(userA());
        H.saveOrUpdate(userB());
        List<User> users = FluentHibernateTestData.createUserRequest().list();
        Assert.assertNotNull(users);
        Assert.assertEquals(2, users.size());
    }

    @Test
    public void first() {
        H.saveOrUpdate(userA());
        H.saveOrUpdate(userB());
        User user = FluentHibernateTestData.createRequestForUserA().first();
        Assert.assertNotNull(user);
        Assert.assertEquals(userA().getLogin(), user.getLogin());
    }

    @Test
    public void count() {
        H.saveOrUpdate(userA());
        H.saveOrUpdate(userB());
        int count = H.request(User.class).count();
        Assert.assertEquals(2, count);
    }

}
