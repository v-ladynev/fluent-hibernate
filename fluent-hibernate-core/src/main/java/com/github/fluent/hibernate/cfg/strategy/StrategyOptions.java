package com.github.fluent.hibernate.cfg.strategy;

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

    private String foreignkeyColumnPrefix = FOREIGN_KEY_PREFIX;

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

    public String getForeignkeyColumnPrefix() {
        return foreignkeyColumnPrefix;
    }

    public String getUniqueKeyPrefix() {
        return uniqueKeyPrefix;
    }

    public String getTablePrefix() {
        return tablePrefix;
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

    public void setForeignkeyColumnPrefix(String foreignkeyColumnPrefix) {
        this.foreignkeyColumnPrefix = foreignkeyColumnPrefix;
    }

    public void setUniqueKeyPrefix(String uniqueKeyPrefix) {
        this.uniqueKeyPrefix = uniqueKeyPrefix;
    }

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public void setHasTablePrefix(boolean hasTablePrefix) {
        this.hasTablePrefix = hasTablePrefix;
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

}
