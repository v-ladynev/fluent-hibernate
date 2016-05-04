package com.github.fluent.hibernate.cfg;

import java.io.File;

import org.hibernate.SessionFactory;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;

import com.github.fluent.hibernate.cfg.strategy.StrategyOptions;

interface IConfigurationBuilder {

    void configure(String hibernateCfgXml);

    void addHibernateProperties(HibernateProperties options);

    SessionFactory buildSessionFactory();

    void addPropertiesFromClassPath(String classPathResourcePath);

    void addPropertiesFromFile(File propertiesFilePath);

    void addAnnotatedClasses(Class<?>[] annotatedClasses);

    void addPackagesToScan(String[] packagesToScan);

    void useNamingStrategy();

    void useNamingStrategy(StrategyOptions options);

    void useNamingStrategy(ImplicitNamingStrategy startegy);

}
