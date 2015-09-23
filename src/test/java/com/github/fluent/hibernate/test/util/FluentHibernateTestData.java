package com.github.fluent.hibernate.test.util;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.HibernateRequest;
import com.github.fluent.hibernate.test.persistent.User;

/**
 *
 * @author V.Ladynev
 */
public final class FluentHibernateTestData {

    public static final User USER_A = createUser("loginA", "A user", 20);

    public static final User USER_B = createUser("loginB", "B user", 30);

    private FluentHibernateTestData() {

    }

    public static HibernateRequest<User> cerateRequestForUserA() {
        return H.<User> request(User.class).eq(User.LOGIN, USER_A.getLogin());
    }

    private static User createUser(String login, String name, int age) {
        User result = new User();
        result.setLogin(login);
        result.setName(name);
        result.setAge(age);
        return result;
    }

}
