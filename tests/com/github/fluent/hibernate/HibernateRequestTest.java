package com.github.fluent.hibernate;

import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.USER_A;
import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.USER_B;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.github.fluent.hibernate.test.persistent.User;
import com.github.fluent.hibernate.test.util.FluentHibernateBaseTest;

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
        List<User> users = H.<User> request(User.class).list();
        Assert.assertNotNull(users);
        Assert.assertEquals(2, users.size());
    }

}
