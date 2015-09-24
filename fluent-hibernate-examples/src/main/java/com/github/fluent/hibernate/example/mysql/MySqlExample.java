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

    private static final User USER_A = createUser("loginA", "A user", 20);

    private static final User USER_B = createUser("loginB", "B user", 30);

    private static final Logger LOG = Logger.getLogger(MySqlExample.class);

    public static void main(String[] args) {
        try {
            HibernateSessionFactory.Builder.configureFromDefaultHibernateCfgXml()
            .createSessionFactory();
            new MySqlExample().doSomeDatabaseStuff();
        } catch (Throwable th) {
            th.printStackTrace();
        } finally {
            HibernateSessionFactory.closeSessionFactory();
        }
    }

    private void doSomeDatabaseStuff() {
        deleteAllUsers();
        insertUsers();
        countUsers();
        User user = findUser(USER_A.getLogin());
        LOG.info("User: " + user);
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
        return USER_A.cloneUser();
    }

    public static User userB() {
        return USER_B.cloneUser();
    }

    private static User createUser(String login, String name, int age) {
        User result = new User();
        result.setLogin(login);
        result.setName(name);
        result.setAge(age);
        return result;
    }

}
