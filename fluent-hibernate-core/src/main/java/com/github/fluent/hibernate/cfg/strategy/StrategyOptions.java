package com.github.fluent.hibernate.cfg.strategy;

import static com.github.fluent.hibernate.internal.util.InternalUtils.StringUtils.addSuffixIfNot;
import static com.github.fluent.hibernate.internal.util.InternalUtils.StringUtils.isEmpty;
import static com.github.fluent.hibernate.internal.util.InternalUtils.StringUtils.length;

/**
 *
 *
 * @author V.Ladynev
 * @version $Id$
 */
public class StrategyOptions {

    private static final String COLUMN_PREFIX = "f_";

    private static final String FOREIGN_KEY_COLUMN_PREFIX = "fk_";

    private static final String FOREIGN_KEY_CONSTRAINT_PREFIX = "fk_";

    private static final String UNIQUE_KEY_CONSTRAINT_PREFIX = "uk_";

    private String tablePrefix;

    private String columnPrefix = COLUMN_PREFIX;

    private String foreignKeyColumnPrefix = FOREIGN_KEY_COLUMN_PREFIX;

    private String foreignKeyConstraintPrefix = FOREIGN_KEY_CONSTRAINT_PREFIX;

    private String uniqueKeyConstraintPrefix = UNIQUE_KEY_CONSTRAINT_PREFIX;

    private int maxLength;

    private boolean restrictTableNames = true;

    private boolean restrictColumnNames = true;

    private boolean restrictEmbeddedColumnNames = true;

    private boolean restrictJoinTableNames = true;

    private boolean restrictConstraintNames = true;

    public boolean autodetectMaxLength;

    public String getTablePrefix() {
        return tablePrefix;
    }

    boolean hasTablePrefix() {
        return !isEmpty(tablePrefix);
    }

    public String getColumnPrefix() {
        return columnPrefix;
    }

    int getColumnPrefixLength() {
        return length(columnPrefix);
    }

    boolean hasColumnPrefix() {
        return !isEmpty(columnPrefix);
    }

    public String getForeignKeyColumnPrefix() {
        return foreignKeyColumnPrefix;
    }

    boolean hasForeignKeyColumnPrefix() {
        return !isEmpty(foreignKeyColumnPrefix);
    }

    public String getForeignKeyConstraintPrefix() {
        return foreignKeyConstraintPrefix;
    }

    boolean hasForeignKeyConstraintPrefix() {
        return !isEmpty(foreignKeyConstraintPrefix);
    }

    public String getUniqueKeyConstraintPrefix() {
        return uniqueKeyConstraintPrefix;
    }

    boolean hasUniqueKeyConstraintPrefix() {
        return !isEmpty(uniqueKeyConstraintPrefix);
    }

    public int getMaxLength() {
        return maxLength;
    }

    public boolean isRestrictTableNames() {
        return restrictTableNames;
    }

    public boolean isRestrictColumnNames() {
        return restrictColumnNames;
    }

    public boolean isRestrictEmbeddedColumnNames() {
        return restrictEmbeddedColumnNames;
    }

    public boolean isRestrictJoinTableNames() {
        return restrictJoinTableNames;
    }

    public boolean isRestrictConstraintNames() {
        return restrictConstraintNames;
    }

    public boolean isAutodetectMaxLength() {
        return autodetectMaxLength;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = addSuffixIfNot(tablePrefix, NamingStrategyUtils.NAME_PARTS_SEPARATOR);
    }

    public void setColumnPrefix(String columnPrefix) {
        this.columnPrefix = columnPrefix;
    }

    public void setForeignKeyColumnPrefix(String foreignKeyColumnPrefix) {
        this.foreignKeyColumnPrefix = foreignKeyColumnPrefix;
    }

    public void setForeignKeyConstraintPrefix(String foreignKeyConstraintPrefix) {
        this.foreignKeyConstraintPrefix = foreignKeyConstraintPrefix;
    }

    public void setUniqueKeyConstraintPrefix(String uniqueKeyConstraintPrefix) {
        this.uniqueKeyConstraintPrefix = uniqueKeyConstraintPrefix;
    }

    public void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setRestrictTableNames(boolean restrictTableNames) {
        this.restrictTableNames = restrictTableNames;
    }

    public void setRestrictColumnNames(boolean restrictColumnNames) {
        this.restrictColumnNames = restrictColumnNames;
    }

    public void setRestrictEmbeddedColumnNames(boolean restrictEmbeddedColumnNames) {
        this.restrictEmbeddedColumnNames = restrictEmbeddedColumnNames;
    }

    public void setRestrictJoinTableNames(boolean restrictJoinTableNames) {
        this.restrictJoinTableNames = restrictJoinTableNames;
    }

    public void setRestrictConstraintNames(boolean restrictConstraintNames) {
        this.restrictConstraintNames = restrictConstraintNames;
    }

    public void setAutodetectMaxLength(boolean autodetectMaxLength) {
        this.autodetectMaxLength = autodetectMaxLength;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {

        private final StrategyOptions result = new StrategyOptions();

        public Builder tablePrefix(String tablePrefix) {
            result.setTablePrefix(tablePrefix);
            return this;
        }

        public Builder dontRestrictLength() {
            return restrictLength(0);
        }

        public Builder restrictLength(int maxLength) {
            result.maxLength = maxLength;
            return this;
        }

        public Builder columnPrefix(String columnPrefix) {
            result.columnPrefix = columnPrefix;
            return this;
        }

        /**
         * Sets a foreign key column prefix.
         */
        public Builder foreignKeyColumnPrefix(String foreignKeyColumnPrefix) {
            result.foreignKeyColumnPrefix = foreignKeyColumnPrefix;
            return this;
        }

        /**
         * Sets a foreign key constraint prefix.
         */
        public Builder foreignKeyConstraintPrefix(String foreignKeyConstraintPrefix) {
            result.foreignKeyConstraintPrefix = foreignKeyConstraintPrefix;
            return this;
        }

        public Builder uniqueKeyConstraintPrefix(String uniqueKeyConstraintPrefix) {
            result.uniqueKeyConstraintPrefix = uniqueKeyConstraintPrefix;
            return this;
        }

        public Builder restrictConstraintNames(boolean restrictConstraintNames) {
            result.setRestrictConstraintNames(restrictConstraintNames);
            return this;
        }

        public Builder restrictTableNames(boolean restrictTableNames) {
            result.restrictTableNames = restrictTableNames;
            return this;
        }

        public Builder restrictColumnNames(boolean restrictColumnNames) {
            result.restrictColumnNames = restrictColumnNames;
            return this;
        }

        public Builder restrictEmbeddedColumnNames(boolean restrictEmbeddedColumnNames) {
            result.restrictEmbeddedColumnNames = restrictEmbeddedColumnNames;
            return this;
        }

        public Builder restrictJoinTableNames(boolean restrictJoinTableNames) {
            result.restrictJoinTableNames = restrictJoinTableNames;
            return this;
        }

        /**
         * Don't use any prefixes like a table name prefix.
         */
        public Builder withoutPrefixes() {
            result.tablePrefix = null;
            result.columnPrefix = null;
            result.foreignKeyColumnPrefix = null;
            result.foreignKeyConstraintPrefix = null;
            result.uniqueKeyConstraintPrefix = null;
            return this;
        }

        /**
         * Autodetect a maximum name length for a database. Can be used only with a dialect in the
         * hibernate.properties.
         *
         * TODO It shoul work with a dialect in the hibernate.cfg.xml.
         */
        public Builder autodetectMaxLength() {
            result.autodetectMaxLength = true;
            return this;
        }

        public StrategyOptions build() {
            return result;
        }

    }

}
