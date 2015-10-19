package com.github.fluent.hibernate.example.spring.console;

import org.hibernate.SessionFactory;
import org.jboss.logging.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.example.spring.console.persistent.User;
import com.github.fluent.hibernate.factory.HibernateSessionFactory;

/**
 *
 * @author V.Ladynev
 */
public class SpringConsoleExample {

    private static final Logger LOG = Logger.getLogger(SpringConsoleExample.class);

    public static void main(String[] args) {
        ApplicationContext ctx = new ClassPathXmlApplicationContext(
                "classpath:hibernate-context.xml");
        try {
            HibernateSessionFactory.Builder
                    .configureFromExistingSessionFactory((SessionFactory) ctx
                            .getBean("sessionFactory"));
            new SpringConsoleExample().doSomeDatabaseStuff();
        } finally {
            HibernateSessionFactory.closeSessionFactory();
        }
    }

    private void doSomeDatabaseStuff() {
        deleteAllUsers();
        countUsers();
    }

    private void deleteAllUsers() {
        H.update("delete from User").execute();
    }

    private void countUsers() {
        int count = H.<Long> request(User.class).count();
        LOG.info("Users count: " + count);
    }

}
