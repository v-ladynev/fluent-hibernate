package com.github.fluent.hibernate.cfg.strategy;

import com.github.fluent.hibernate.internal.util.InternalUtils;

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

    private boolean hasTablePrefix;

    private int maxLength;

    private boolean restrictTableNames = true;

    private boolean restrictColumnNames = true;

    private boolean restrictEmbeddedColumnNames = true;

    private boolean restrictJoinTableNames = true;

    private boolean resrictConstraintNames = true;

    public String getColumnNamePrefix() {
        return columnNamePrefix;
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

    public String addTablePrefix(String name) {
        return tablePrefix == null ? name : tablePrefix + name;
    }

    public boolean isHasTablePrefix() {
        return hasTablePrefix;
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

    public boolean isResrictConstraintNames() {
        return resrictConstraintNames;
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
        hasTablePrefix = !InternalUtils.StringUtils.isEmpty(tablePrefix);
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

    public void setResrictConstraintNames(boolean resrictConstraintNames) {
        this.resrictConstraintNames = resrictConstraintNames;
    }

    public static class Builder {

        private final StrategyOptions result = new StrategyOptions();

        public Builder restrictLength(int maxLength) {
            result.setMaxLength(maxLength);
            return this;
        }

        public StrategyOptions build() {
            return result;
        }

    }

}
