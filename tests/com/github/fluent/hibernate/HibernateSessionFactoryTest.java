package com.github.fluent.hibernate;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
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
        // configuration.setNamingStrategy(ImprovedNamingStrategy.INSTANCE);
        /*
        cfg=new Configuration();
        Properties p = new Properties();
        p.put( Environment.DIALECT, "org.hibernate.dialect.HSQLDialect" );
        p.put( "hibernate.connection.driver_class", "org.h2.Driver" );
        p.put( "hibernate.connection.url", "jdbc:h2:mem:" );
        p.put( "hibernate.connection.username", "sa" );
        p.put( "hibernate.connection.password", "" );
        cfg.setProperties(p);
        serviceRegistry = ServiceRegistryBuilder.buildServiceRegistry( cfg.getProperties() );
         */

        return configuration;
    }

}
