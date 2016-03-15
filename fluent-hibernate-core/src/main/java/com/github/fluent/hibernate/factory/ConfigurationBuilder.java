package com.github.fluent.hibernate.factory;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Properties;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.BootstrapServiceRegistry;
import org.hibernate.boot.registry.BootstrapServiceRegistryBuilder;
import org.hibernate.boot.registry.classloading.spi.ClassLoaderService;
import org.hibernate.cfg.Configuration;

import com.github.fluent.hibernate.cfg.scanner.EntityScanner;
import com.github.fluent.hibernate.internal.util.InternalUtils;

/**
 *
 * @author V.Ladynev
 */
class ConfigurationBuilder {

    private final Configuration result = new Configuration();

    public void configure(String hibernateCfgXml) {
        if (hibernateCfgXml == null) {
            result.configure();
        } else {
            result.configure(hibernateCfgXml);
        }
    }

    public SessionFactory buildSessionFactory() {
        return result.buildSessionFactory();
    }

    public void addPropertiesFromClassPath(String classPathResourceName) {
        InputStream stream = createBootstrapServiceRegistry().getService(ClassLoaderService.class)
                .locateResourceStream(classPathResourceName);
        addProperties(loadProperties(stream));
    }

    public void addPropertiesFromFile(File pathToPropertiesFile) {
        try {
            addProperties(loadProperties(new FileInputStream(pathToPropertiesFile)));
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

    public void addProperties(Properties properties) {
        result.addProperties(properties);
    }

    public void addAnnotatedClasses(Class<?>[] annotatedClasses) {
        if (annotatedClasses == null) {
            return;
        }

        for (Class<?> annotatedClass : annotatedClasses) {
            result.addAnnotatedClass(annotatedClass);
        }
    }

    public void addPackagesToScan(String[] packagesToScan) {
        List<Class<?>> classes = EntityScanner.scanPackages(packagesToScan);
        for (Class<?> annotatedClass : classes) {
            result.addAnnotatedClass(annotatedClass);
        }
    }

    private BootstrapServiceRegistry createBootstrapServiceRegistry() {
        return new BootstrapServiceRegistryBuilder().enableAutoClose().build();
    }

}
