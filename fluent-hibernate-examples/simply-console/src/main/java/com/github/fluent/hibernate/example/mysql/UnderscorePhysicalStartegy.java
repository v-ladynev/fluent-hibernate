package com.github.fluent.hibernate.example.mysql;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

import com.github.fluent.hibernate.cfg.strategy.NamingStrategyUtils;

public class UnderscorePhysicalStartegy extends PhysicalNamingStrategyStandardImpl {

    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment context) {
        return context.getIdentifierHelper()
                .toIdentifier(NamingStrategyUtils.classToName(name.getText()));
    }

}
