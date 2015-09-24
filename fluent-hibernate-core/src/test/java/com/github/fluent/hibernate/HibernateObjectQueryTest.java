package com.github.fluent.hibernate;

import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.userA;
import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.userB;
import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.usersAB;

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
    public void save() {
        H.save(userA());
        int count = FluentHibernateTestData.createRequestForUserA().count();
        Assert.assertEquals(1, count);
    }

    @Test
    public void saveOrUpdate() {
        User user = userA();
        H.saveOrUpdate(user);
        int countNew = FluentHibernateTestData.createRequestForUserA().count();
        Assert.assertEquals(1, countNew);
        H.saveOrUpdate(user);
        int countUpdated = FluentHibernateTestData.createRequestForUserA().count();
        Assert.assertEquals(1, countUpdated);
    }

    @Test
    public void saveOrUpdateAll() {
        H.saveOrUpdateAll(usersAB());
        int count = FluentHibernateTestData.createUserRequest().count();
        Assert.assertEquals(2, count);
    }

    @Test
    public void saveAll() {
        H.saveAll(Arrays.asList(userA(), userB()));
        int count = FluentHibernateTestData.createUserRequest().count();
        Assert.assertEquals(2, count);
    }

    @Test
    public void delete() {
        H.saveOrUpdate(userA());
        User user = FluentHibernateTestData.createRequestForUserA().first();
        Assert.assertNotNull(user);
        H.delete(user);
        int count = FluentHibernateTestData.createRequestForUserA().count();
        Assert.assertEquals(0, count);
    }

}
