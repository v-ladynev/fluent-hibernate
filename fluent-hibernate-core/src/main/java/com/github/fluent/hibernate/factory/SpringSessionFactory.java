package com.github.fluent.hibernate.factory;

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
        HibernateSessionFactory.Builder.configureFromExistingSessionFactory(sessionFactory);
        sessionFactory = null;
    }

}
