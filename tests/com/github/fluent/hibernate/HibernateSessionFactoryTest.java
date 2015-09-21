package com.github.fluent.hibernate;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author V.Ladynev
 */
public class HibernateSessionFactoryTest {

    @BeforeClass
    public static void init() {
        constructConfiguration();
    }

    @Test
    public void createSessionFactory() {

    }

    private static Configuration constructConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setProperty(Environment.HBM2DDL_AUTO, "create-drop");
        configuration.setNamingStrategy(ImprovedNamingStrategy.INSTANCE);
        return configuration;
    }

}
