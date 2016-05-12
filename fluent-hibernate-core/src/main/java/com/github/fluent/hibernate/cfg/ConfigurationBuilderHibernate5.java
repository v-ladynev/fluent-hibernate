package com.github.fluent.hibernate.cfg;

import static com.github.fluent.hibernate.internal.util.InternalUtils.Asserts.fail;
import static com.github.fluent.hibernate.internal.util.InternalUtils.Asserts.isTrue;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.PhysicalNamingStrategy;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import com.github.fluent.hibernate.cfg.scanner.EntityScanner;
import com.github.fluent.hibernate.cfg.strategy.StrategyOptions;
import com.github.fluent.hibernate.cfg.strategy.hibernate5.Hibernate5NamingStrategy;
import com.github.fluent.hibernate.internal.util.InternalUtils;
import com.github.fluent.hibernate.internal.util.InternalUtils.Asserts;

/**
 *
 * @author V.Ladynev
 */
class ConfigurationBuilderHibernate5 implements IConfigurationBuilder {

    private StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

    private MetadataSources metadataSourcesCached;

    private PhysicalNamingStrategy physicalNamingStrategy;

    private ImplicitNamingStrategy implicitNamingStartegy;

    @Override
    public void configure(String hibernateCfgXml) {
        if (hibernateCfgXml == null) {
            registryBuilder.configure();
        } else {
            registryBuilder.configure(hibernateCfgXml);
        }
    }

    @Override
    public void addHibernateProperties(HibernateProperties options) {
        registryBuilder.applySettings(options.getOptionsAsProperties());
    }

    @Override
    public SessionFactory buildSessionFactory() {
        MetadataBuilder metadataBuilder = getMetadataSources().getMetadataBuilder();

        if (physicalNamingStrategy != null) {
            metadataBuilder.applyPhysicalNamingStrategy(physicalNamingStrategy);
        }

        if (implicitNamingStartegy != null) {
            metadataBuilder.applyImplicitNamingStrategy(implicitNamingStartegy);
        }

        return metadataBuilder.build().buildSessionFactory();
    }

    @Override
    public void addPropertiesFromClassPath(String classPathResourcePath) {
        registryBuilder.loadProperties(classPathResourcePath);
    }

    @Override
    public void addPropertiesFromFile(File propertiesFilePath) {
        registryBuilder.loadProperties(propertiesFilePath);
    }

    @Override
    public void addAnnotatedClasses(Class<?>[] annotatedClasses) {
        addAnnotatedClassesToMetadata(Arrays.asList(annotatedClasses));
    }

    @Override
    public void addPackagesToScan(String[] packagesToScan) {
        addAnnotatedClassesToMetadata(EntityScanner.scanPackages(packagesToScan).result());
    }

    private void addAnnotatedClassesToMetadata(List<Class<?>> annotatedClasses) {
        MetadataSources metadataSources = getMetadataSources();

        for (Class<?> annotatedClass : annotatedClasses) {
            metadataSources.addAnnotatedClass(annotatedClass);
        }
    }

    @Override
    public void useNamingStrategy(StrategyOptions options) {
        if (options.isAutodetectMaxLength()) {
            options.setMaxLength(
                    detectMaxLength(Environment.getProperties().getProperty(Environment.DIALECT)));
        }

        useNamingStrategy(new Hibernate5NamingStrategy(options));
    }

    @Override
    public void useNamingStrategy(Object strategy) {
        if (strategy == null) {
            implicitNamingStartegy = null;
            physicalNamingStrategy = null;
            return;
        }

        if (strategy instanceof ImplicitNamingStrategy) {
            implicitNamingStartegy = (ImplicitNamingStrategy) strategy;
            return;
        }

        if (strategy instanceof PhysicalNamingStrategy) {
            physicalNamingStrategy = (PhysicalNamingStrategy) strategy;
            return;
        }

        Asserts.fail(String.format(
                "Incorrect naming strategy `%s`. "
                        + "It should be an instance of ImplicitNamingStrategy or PhysicalNamingStrategy",
                strategy.getClass().getSimpleName()));
    }

    private int detectMaxLength(String dialect) {
        isTrue(!InternalUtils.StringUtils.isEmpty(dialect), String.format(
                "Can't autodetect a max length. Property %s is not set", Environment.DIALECT));
        String dialectClass = InternalUtils.ClassUtils.getShortName(dialect);

        if (dialectClass.contains("H2Dialect")) {
            return 0; // no limitations
        }

        if (dialectClass.contains("MySQL")) {
            return 64;
        }

        if (dialectClass.contains("Oracle")) {
            return 30;
        }

        if (dialectClass.contains("PostgreSQL")) {
            return 63;
        }

        fail("Can't autodetect a max length. Specify it with StrategyOptions.setMaxLength()");
        return 0;
    }

    private MetadataSources getMetadataSources() {
        if (metadataSourcesCached == null) {
            metadataSourcesCached = new MetadataSources(registryBuilder.build());
        }

        return metadataSourcesCached;
    }

    @Override
    public ISessionControl createSessionControl() {
        return new SessionControlHibernate5();
    }

}
