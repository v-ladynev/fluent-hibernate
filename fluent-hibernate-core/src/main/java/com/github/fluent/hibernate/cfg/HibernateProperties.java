package com.github.fluent.hibernate.cfg;

import java.util.Properties;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.dialect.H2Dialect;

/**
 * Hibernate properties. Such properties is used in hibernate.properties.
 *
 * @author V.Ladynev
 */
public final class HibernateProperties {

    private final Properties options = new Properties();

    private HibernateProperties() {

    }

    public static HibernateProperties forH2CreateDrop() {
        return forH2().hbm2DllAuto(Hbm2DllAuto.CREATE_DROP);
    }

    public static HibernateProperties forH2() {
        return HibernateProperties.create().dialect(H2Dialect.class).driverClass("org.h2.Driver")
                .userName("sa").password("").connectionUrl("jdbc:h2:mem:di;MODE=ORACLE");
    }

    public static HibernateProperties create() {
        return new HibernateProperties();
    }

    public HibernateProperties connectionUrl(String connectionUrl) {
        options.put(AvailableSettings.URL, connectionUrl);
        return this;
    }

    public HibernateProperties userName(String userName) {
        options.put(AvailableSettings.USER, userName);
        return this;
    }

    public HibernateProperties password(String password) {
        options.put(AvailableSettings.PASS, password);
        return this;
    }

    public HibernateProperties driverClass(String driverClass) {
        options.put(AvailableSettings.DRIVER, driverClass);
        return this;
    }

    public HibernateProperties dialect(String dialectClass) {
        options.put(AvailableSettings.DIALECT, dialectClass);
        return this;
    }

    public HibernateProperties dialect(Class<?> dialectClass) {
        options.put(AvailableSettings.DIALECT, dialectClass.getName());
        return this;
    }

    public HibernateProperties hbm2DllAuto(Hbm2DllAuto hbm2DllAuto) {
        options.put(AvailableSettings.HBM2DDL_AUTO, hbm2DllAuto.getAsString());
        return this;
    }

    public HibernateProperties showSql(boolean showSql) {
        options.put(AvailableSettings.SHOW_SQL, Boolean.toString(showSql));
        return this;
    }

    public HibernateProperties useSqlComments(boolean useSqlComments) {
        options.put(AvailableSettings.USE_SQL_COMMENTS, Boolean.toString(useSqlComments));
        return this;
    }

    public HibernateProperties formatSql(boolean formatSql) {
        options.put(AvailableSettings.FORMAT_SQL, Boolean.toString(formatSql));
        return this;
    }

    public HibernateProperties property(String name, String value) {
        options.put(name, value);
        return this;
    }

    Properties getOptionsAsProperties() {
        return options;
    }

    public static enum Hbm2DllAuto {
        /**
         * Drop the schema and recreate it on SessionFactory startup.
         */
        CREATE("create"),
        /**
         * Drop the schema and recreate it on SessionFactory startup. Additionally, drop the schema
         * on SessionFactory shutdown.
         */
        CREATE_DROP("create-drop"),
        /**
         * Update (alter) the schema on SessionFactory startup.
         */
        UPDATE("update"),
        /**
         * Validate the schema on SessionFactory startup.
         */
        VALIDATE("validate"),
        /**
         * Do not attempt to update nor validate the schema.
         */
        NONE("none");

        private String asString;

        private Hbm2DllAuto(String asString) {
            this.asString = asString;

        }

        public String getAsString() {
            return asString;
        }

    }

}
