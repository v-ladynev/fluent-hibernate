package com.github.fluent.hibernate.cfg;

import org.hibernate.SessionFactory;

/**
 * The entry point for a fluent-hibernate configuration.
 *
 * @author V.Ladynev
 */
public final class Fluent {

    private Fluent() {

    }

    /**
     * Creates a builder for a Hibernate session factory configuration and build.
     *
     * @return a builder
     */
    public static FluentFactoryBuilder factory() {
        return new FluentFactoryBuilder();
    }

    public static void configureFromExistingSessionFactory(SessionFactory sessionFactory) {
        FluentFactoryBuilder.configureFromExistingSessionFactory(sessionFactory);
    }

}
