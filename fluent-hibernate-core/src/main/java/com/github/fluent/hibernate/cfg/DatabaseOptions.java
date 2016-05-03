package com.github.fluent.hibernate.cfg;

import java.util.Properties;

import org.hibernate.cfg.AvailableSettings;

/**
 * An options for a database connection.
 *
 * @author V.Ladynev
 */
public final class DatabaseOptions {

    public static final DatabaseOptions H2_ORACLE_MODE = DatabaseOptions.create().userName("user")
            .password("").connectionUrl("jdbc:h2:mem:di;MODE=ORACLE");

    private final Properties options = new Properties();

    private DatabaseOptions() {

    }

    public static DatabaseOptions create() {
        return new DatabaseOptions();
    }

    public DatabaseOptions connectionUrl(String connectionUrl) {
        options.put(AvailableSettings.URL, connectionUrl);
        return this;
    }

    public DatabaseOptions userName(String userName) {
        options.put(AvailableSettings.USER, userName);
        return this;
    }

    public DatabaseOptions password(String password) {
        options.put(AvailableSettings.PASS, password);
        return this;
    }

    Properties getOptionsAsProperties() {
        return options;
    }

}
