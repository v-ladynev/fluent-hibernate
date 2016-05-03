package com.github.fluent.hibernate.cfg;

import org.hibernate.SessionFactory;

/**
 *
 * @author V.Ladynev
 */
public class SpringSessionFactory {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void init() {
        FluentFactoryBuilder.configureFromExistingSessionFactory(sessionFactory);
        sessionFactory = null;
    }

}
