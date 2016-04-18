package com.github.fluent.hibernate.cfg.strategy;

import static com.github.fluent.hibernate.internal.util.InternalUtils.StringUtils.isEmpty;
import static com.github.fluent.hibernate.internal.util.InternalUtils.StringUtils.length;

/**
 *
 *
 * @author V.Ladynev
 * @version $Id$
 */
public class StrategyOptions {

    private static final String COLUMN_NAME_PREFIX = "f_";

    private static final String FOREIGN_KEY_COLUMN_PREFIX = "fk_";

    private static final String FOREIGN_KEY_PREFIX = "fk_";

    private static final String UNIQUE_KEY_PREFIX = "uk_";

    private String columnNamePrefix = COLUMN_NAME_PREFIX;

    private String foreignKeyColumnPrefix = FOREIGN_KEY_COLUMN_PREFIX;

    private String foreignKeyPrefix = FOREIGN_KEY_PREFIX;

    private String uniqueKeyPrefix = UNIQUE_KEY_PREFIX;

    private String tablePrefix;

    private int maxLength;

    private boolean restrictTableNames = true;

    private boolean restrictColumnNames = true;

    private boolean restrictEmbeddedColumnNames = true;

    private boolean restrictJoinTableNames = true;

    private boolean restrictConstraintNames = true;

    public String getColumnNamePrefix() {
        return columnNamePrefix;
    }

    int getColumnNamePrefixLength() {
        return length(columnNamePrefix);
    }

    public String getForeignKeyColumnPrefix() {
        return foreignKeyColumnPrefix;
    }

    public String getForeignKeyPrefix() {
        return foreignKeyPrefix;
    }

    public String getUniqueKeyPrefix() {
        return uniqueKeyPrefix;
    }

    public String getTablePrefix() {
        return tablePrefix;
    }

    boolean hasTablePrefix() {
        return !isEmpty(tablePrefix);
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

    public void setColumnNamePrefix(String columnNamePrefix) {
        this.columnNamePrefix = columnNamePrefix;
    }

    public void setForeignKeyColumnPrefix(String foreignKeyColumnPrefix) {
        this.foreignKeyColumnPrefix = foreignKeyColumnPrefix;
    }

    public void setForeignKeyPrefix(String foreignKeyPrefix) {
        this.foreignKeyPrefix = foreignKeyPrefix;
    }

    public void setUniqueKeyPrefix(String uniqueKeyPrefix) {
        this.uniqueKeyPrefix = uniqueKeyPrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
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

        public Builder columnNamePrefix(String columnNamePrefix) {
            result.columnNamePrefix = columnNamePrefix;
            return this;
        }

        public Builder foreignKeyColumnPrefix(String foreignKeyColumnPrefix) {
            result.foreignKeyColumnPrefix = foreignKeyColumnPrefix;
            return this;
        }

        public Builder setForeignKeyPrefix(String foreignKeyPrefix) {
            result.foreignKeyPrefix = foreignKeyPrefix;
            return this;
        }

        public Builder setUniqueKeyPrefix(String uniqueKeyPrefix) {
            result.uniqueKeyPrefix = uniqueKeyPrefix;
            return this;
        }

        public Builder restrictConstraintNames(boolean restrictConstraintNames) {
            result.setRestrictConstraintNames(restrictConstraintNames);
            return this;
        }

        public Builder setRestrictTableNames(boolean restrictTableNames) {
            result.restrictTableNames = restrictTableNames;
            return this;
        }

        public Builder setRestrictColumnNames(boolean restrictColumnNames) {
            result.restrictColumnNames = restrictColumnNames;
            return this;
        }

        public Builder setRestrictEmbeddedColumnNames(boolean restrictEmbeddedColumnNames) {
            result.restrictEmbeddedColumnNames = restrictEmbeddedColumnNames;
            return this;
        }

        public Builder setRestrictJoinTableNames(boolean restrictJoinTableNames) {
            result.restrictJoinTableNames = restrictJoinTableNames;
            return this;
        }

        public StrategyOptions build() {
            return result;
        }

    }

}
