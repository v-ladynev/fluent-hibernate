package com.github.fluent.hibernate.cfg;

import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;

import com.github.fluent.hibernate.cfg.strategy.StrategyOptions;

/**
 * Fluent API for a Hibernate session factory configuration and build. The simplest way to create a
 * session factory:
 *
 * <code>
 * HibernateSessionFactory.Builder.configureFromDefaultHibernateCfgXml().createSessionFactory();
 * </code>
 *
 * @author V.Ladynev
 */
public class FluentFactoryBuilder {

    private final ConfigurationBuilder configurationBuilder = new ConfigurationBuilder();

    public static FluentFactoryBuilder configureFromDefaultHibernateCfgXml() {
        return configureFromHibernateCfgXml(null);
    }

    public static FluentFactoryBuilder configureFromHibernateCfgXml(String hibernateCfgXml) {
        FluentFactoryBuilder result = new FluentFactoryBuilder();
        result.configurationBuilder.configure(hibernateCfgXml);
        return result;
    }

    public static FluentFactoryBuilder configureWithoutHibernateCfgXml() {
        return new FluentFactoryBuilder();
    }

    public FluentFactoryBuilder database(DatabaseOptions options) {
        configurationBuilder.addDatabaseOptions(options);
        return this;
    }

    public FluentFactoryBuilder annotatedClasses(Class<?>... annotatedClasses) {
        configurationBuilder.addAnnotatedClasses(annotatedClasses);
        return this;
    }

    public FluentFactoryBuilder packagesToScan(String... packagesToScan) {
        configurationBuilder.addPackagesToScan(packagesToScan);
        return this;
    }

    public static void configureFromExistingSessionFactory(SessionFactory sessionFactory) {
        HibernateSessionFactory.setExistingSessionFactory(sessionFactory);
    }

    public FluentFactoryBuilder addHibernatePropertiesFromFile(File pathToPropertiesFile) {
        configurationBuilder.addPropertiesFromFile(pathToPropertiesFile);
        return this;
    }

    public FluentFactoryBuilder addHibernatePropertiesFromClassPathResource(
            String classPathResourceName) {
        configurationBuilder.addPropertiesFromClassPath(classPathResourceName);
        return this;
    }

    public FluentFactoryBuilder useNamingStrategy() {
        configurationBuilder.useNamingStrategy();
        return this;
    }

    public FluentFactoryBuilder useNamingStrategy(StrategyOptions options) {
        configurationBuilder.useNamingStrategy(options);
        return this;
    }

    public FluentFactoryBuilder useNamingStrategy(ImplicitNamingStrategy startegy) {
        configurationBuilder.useNamingStrategy(startegy);
        return this;
    }

    public void build() {
        HibernateSessionFactory
                .setExistingSessionFactory(configurationBuilder.buildSessionFactory());
    }

    /**
     * Close a Hibernate session factory.
     */
    public void close() {
        HibernateSessionFactory.closeSessionFactory();
    }

}
