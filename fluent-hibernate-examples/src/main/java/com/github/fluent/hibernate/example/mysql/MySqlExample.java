package com.github.fluent.hibernate.example.mysql;

import org.jboss.logging.Logger;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.HibernateSessionFactory;
import com.github.fluent.hibernate.example.mysql.persistent.User;

/**
 *
 * @author V.Ladynev
 */
public class MySqlExample {

    private static final String USER_LOGIN_A = "loginA";

    private static final Logger LOG = Logger.getLogger(MySqlExample.class);

    public static void main(String[] args) {
        try {
            HibernateSessionFactory.Builder.configureFromDefaultHibernateCfgXml()
            .createSessionFactory();
            new MySqlExample().doSomeDatabaseStuff();
        } finally {
            HibernateSessionFactory.closeSessionFactory();
        }
    }

    private void doSomeDatabaseStuff() {
        deleteAllUsers();
        insertUsers();
        countUsers();
        User user = findUser(USER_LOGIN_A);
        LOG.info("User A: " + user);
    }

    private User findUser(String login) {
        return H.<User> request(User.class).eq(User.LOGIN, login).first();
    }

    private void deleteAllUsers() {
        H.update("delete from User").execute();
    }

    private void insertUsers() {
        H.save(userA());
        H.save(userB());
    }

    private void countUsers() {
        int count = H.<Long> request(User.class).count();
        LOG.info("Users count: " + count);
    }

    public static User userA() {
        return User.create(USER_LOGIN_A, "A user", 20);
    }

    public static User userB() {
        return User.create("loginB", "B user", 30);
    }

}
