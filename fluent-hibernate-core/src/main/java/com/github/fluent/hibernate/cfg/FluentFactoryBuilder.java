package com.github.fluent.hibernate.cfg;

import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;

import com.github.fluent.hibernate.cfg.strategy.StrategyOptions;

/**
 * Fluent API for a Hibernate session factory configuration and build. The simplest way to create a
 * session factory:
 *
 * <code>Fluent.factory().build();</code> <br>
 * Don't forget to destroy it with
 *
 * <code>Fluent.factory().close();</code>
 *
 * @author V.Ladynev
 */
public class FluentFactoryBuilder {

    private final IConfigurationBuilder configurationBuilder = new Hibernate5ConfigurationBuilder();

    private boolean useHibernateCfgXml = true;

    private String hibernateCfgXmlPath;

    private Class<?>[] annotatedClasses;

    private String[] packagesToScan;

    private StrategyOptions options;

    private ImplicitNamingStrategy strategy;

    /**
     * Specify a path to the xml configuration (like hibernate.cfg.xml). This method should be used
     * only for a non standard path.
     *
     * @param hibernateCfgXmlPath
     *            a path to the xml configuration. For an example, "config/hibernate.cfg.xml".
     */
    public FluentFactoryBuilder hibernateCfgXml(String hibernateCfgXmlPath) {
        this.hibernateCfgXmlPath = hibernateCfgXmlPath;
        return this;
    }

    /**
     * Specify don't use the xml confriguration (like hibernate.cfg.xml).
     *
     */
    public FluentFactoryBuilder dontUseHibernateCfgXml() {
        this.useHibernateCfgXml = false;
        return this;
    }

    public FluentFactoryBuilder hibernateProperties(HibernateProperties options) {
        configurationBuilder.addHibernateProperties(options);
        return this;
    }

    public FluentFactoryBuilder hibernatePropertiesFromFile(File propertiesFilePath) {
        configurationBuilder.addPropertiesFromFile(propertiesFilePath);
        return this;
    }

    public FluentFactoryBuilder hibernatePropertiesFromClassPathResource(
            String classPathResourcePath) {
        configurationBuilder.addPropertiesFromClassPath(classPathResourcePath);
        return this;
    }

    public FluentFactoryBuilder annotatedClasses(Class<?>... annotatedClasses) {
        this.annotatedClasses = annotatedClasses;
        return this;
    }

    public FluentFactoryBuilder scanPackages(String... packagesToScan) {
        this.packagesToScan = packagesToScan;
        return this;
    }

    /**
     * Use the default Hibernate5NamingStrategy.
     */
    public FluentFactoryBuilder useNamingStrategy() {
        useNamingStrategy(new StrategyOptions());
        return this;
    }

    /**
     * Use the default Hibernate5NamingStrategy with options.
     *
     * @param options
     *            options, to specify a strategy behaviour
     */
    public FluentFactoryBuilder useNamingStrategy(StrategyOptions options) {
        this.options = options;
        return this;
    }

    /**
     * TODO Consider passing an Object to support Hibernate 4.
     *
     * Use an implicit naming strategy.
     *
     * @param strategy
     *            an implicit naming strategy
     */
    public FluentFactoryBuilder useNamingStrategy(ImplicitNamingStrategy strategy) {
        this.strategy = strategy;
        return this;
    }

    public static void configureFromExistingSessionFactory(SessionFactory sessionFactory) {
        HibernateSessionFactory.setExistingSessionFactory(sessionFactory);
    }

    /**
     * Build a Hibernate session factory.
     */
    public void build() {
        if (useHibernateCfgXml) {
            configurationBuilder.configure(hibernateCfgXmlPath);
        }

        if (annotatedClasses != null) {
            configurationBuilder.addAnnotatedClasses(annotatedClasses);
        }

        if (packagesToScan != null) {
            configurationBuilder.addPackagesToScan(packagesToScan);
        }

        if (options != null) {
            configurationBuilder.useNamingStrategy(options);
        }

        if (strategy != null) {
            configurationBuilder.useNamingStrategy(strategy);
        }

        configureFromExistingSessionFactory(configurationBuilder.buildSessionFactory());
    }

    /**
     * Close a Hibernate session factory.
     */
    public void close() {
        HibernateSessionFactory.closeSessionFactory();
    }

}
