package com.github.fluent.hibernate.cfg;

import static com.github.fluent.hibernate.internal.util.InternalUtils.Asserts.fail;
import static com.github.fluent.hibernate.internal.util.InternalUtils.Asserts.isTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import com.github.fluent.hibernate.cfg.scanner.EntityScanner;
import com.github.fluent.hibernate.cfg.strategy.StrategyOptions;
import com.github.fluent.hibernate.cfg.strategy.hibernate4.Hibernate4NamingStrategy;
import com.github.fluent.hibernate.internal.util.InternalUtils;
import com.github.fluent.hibernate.internal.util.InternalUtils.Asserts;
import com.github.fluent.hibernate.internal.util.InternalUtils.ClassUtils;
import com.github.fluent.hibernate.internal.util.reflection.ReflectionUtils;

/**
 *
 * @author V.Ladynev
 */
class ConfigurationBuilderHibernate4 implements IConfigurationBuilder {

    private static final String HIBERNATE4_NAMING_STRATEGY_INTERFACE = "org.hibernate.cfg.NamingStrategy";

    private StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

    private final Configuration result = new Configuration();

    @Override
    public void configure(String hibernateCfgXml) {
        if (hibernateCfgXml == null) {
            result.configure();
        } else {
            result.configure(hibernateCfgXml);
        }
    }

    @Override
    public void addHibernateProperties(HibernateProperties options) {
        addProperties(options.getOptionsAsProperties());
    }

    @Override
    public SessionFactory buildSessionFactory() {
        return result
                .buildSessionFactory(registryBuilder.applySettings(result.getProperties()).build());
    }

    @Override
    public void addPropertiesFromClassPath(String classPathResourcePath) {
        registryBuilder.loadProperties(classPathResourcePath);
    }

    @Override
    public void addPropertiesFromFile(File propertiesFilePath) {
        try {
            addProperties(loadProperties(new FileInputStream(propertiesFilePath)));
        } catch (Exception ex) {
            throw InternalUtils.toRuntimeException(ex);
        }
    }

    private Properties loadProperties(InputStream stream) {
        Properties result = new Properties();
        try {
            result.load(stream);
            return result;
        } catch (Exception ex) {
            throw InternalUtils.toRuntimeException(ex);
        } finally {
            InternalUtils.closeQuietly(stream);
        }
    }

    private void addProperties(Properties properties) {
        registryBuilder.applySettings(properties);
    }

    @Override
    public void addAnnotatedClasses(Class<?>[] annotatedClasses) {
        if (annotatedClasses == null) {
            return;
        }

        for (Class<?> annotatedClass : annotatedClasses) {
            result.addAnnotatedClass(annotatedClass);
        }
    }

    @Override
    public void addPackagesToScan(String[] packagesToScan) {
        EntityScanner.scanPackages(packagesToScan).addTo(result);
    }

    @Override
    public void useNamingStrategy(StrategyOptions options) {
        if (options.isAutodetectMaxLength()) {
            options.setMaxLength(
                    detectMaxLength(Environment.getProperties().getProperty(Environment.DIALECT)));
        }

        useNamingStrategy(new Hibernate4NamingStrategy(options));
    }

    @Override
    public void useNamingStrategy(Object strategy) {
        Class<?> namingStartegyInterface = ClassUtils
                .classForNameFromContext(HIBERNATE4_NAMING_STRATEGY_INTERFACE);

        if (strategy != null && !(namingStartegyInterface.isInstance(strategy))) {
            Asserts.fail(String.format(
                    "Incorrect naming strategy `%s`. It should be an instance of NamingStrategy",
                    strategy.getClass().getSimpleName()));
        }

        try {
            ReflectionUtils.invoke(result, getSetNamingStrategyMethod(namingStartegyInterface),
                    strategy);
        } catch (Exception ex) {
            throw InternalUtils.toRuntimeException(
                    "Can't invoke setNamingStrategy() method by reflection", ex);
        }
    }

    private static Method getSetNamingStrategyMethod(Class<?> namingStartegyInterface) {
        try {
            return ReflectionUtils.extractMethod(Configuration.class, "setNamingStrategy",
                    namingStartegyInterface);
        } catch (Exception ex) {
            throw InternalUtils.toRuntimeException(
                    "Can't get setNamingStrategy() method from Configuration by a reflection.", ex);
        }
    }

    private int detectMaxLength(String dialect) {
        isTrue(!InternalUtils.StringUtils.isEmpty(dialect), String.format(
                "Can't autodetect the max length. Property %s is not set", Environment.DIALECT));
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

        fail("Can't autodetect the max length. Specify it with StrategyOptions.setMaxLength()");
        return 0;
    }

    @Override
    public ISessionControl createSessionControl() {
        return new SessionControlHibernate4();
    }

}
