package com.github.fluent.hibernate.cfg;

import java.io.File;

import org.hibernate.SessionFactory;

import com.github.fluent.hibernate.cfg.strategy.StrategyOptions;
import com.github.fluent.hibernate.internal.util.InternalUtils.HibernateUtils;

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

    private final IConfigurationBuilder configurationBuilder;

    private boolean useHibernateCfgXml = true;

    private String hibernateCfgXmlPath;

    private Class<?>[] annotatedClasses;

    private String[] packagesToScan;

    private boolean hibernate4Used;

    public FluentFactoryBuilder() {
        hibernate4Used = HibernateUtils.isHibernate4Used();
        configurationBuilder = hibernate4Used ? new ConfigurationBuilderHibernate4()
                : new ConfigurationBuilderHibernate5();
    }

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
        configurationBuilder.useNamingStrategy(options);
        return this;
    }

    public FluentFactoryBuilder useNamingStrategy(Object strategy) {
        configurationBuilder.useNamingStrategy(strategy);
        return this;
    }

    public FluentFactoryBuilder h2ConfigForTests() {
        return dontUseHibernateCfgXml().hibernateProperties(HibernateProperties.forH2CreateDrop())
                .useNamingStrategy();
    }

    public FluentFactoryBuilder showSql() {
        return hibernateProperties(
                HibernateProperties.create().showSql(true).formatSql(true).useSqlComments(true));
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

        configureFromExistingSessionFactory(configurationBuilder.buildSessionFactory(),
                hibernate4Used);
    }

    /**
     * Close a Hibernate session factory.
     */
    public void close() {
        HibernateSessionFactory.closeSessionFactory();
    }

    static void configureFromExistingSessionFactory(SessionFactory sessionFactory) {
        configureFromExistingSessionFactory(sessionFactory, HibernateUtils.isHibernate4Used());
    }

    private static void configureFromExistingSessionFactory(SessionFactory sessionFactory,
            boolean hibernate4Used) {
        HibernateSessionFactory.setExistingSessionFactory(sessionFactory,
                hibernate4Used ? new SessionControlHibernate4() : new SessionControlHibernate5());
    }

}
