package com.github.fluent.hibernate.strategy;

import static com.github.fluent.hibernate.strategy.NamingStrategyUtils.concat;

import com.github.fluent.hibernate.internal.util.InternalUtils;

/**
 * A naming strategy for column and table names.
 *
 * @author S.Samsonov
 * @author V.Ladynev
 */
class HibernateNamingStrategy {

    private static final String COLUMN_NAME_PREFIX = "f_";

    private static final String FOREIGN_KEY_COLUMN_PREFIX = "fk_";

    private static final String FOREIGN_KEY_PREFIX = "fk_";

    private static final String UNIQUE_KEY_PREFIX = "uk_";

    /** Table's prefix. */
    private String tablePrefix;

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

    public String classToTableName(String className) {
        return addTablePrefix(NamingStrategyUtils.classToTableName(className));
    }

    public String propertyToColumnName(String propertyName) {
        return COLUMN_NAME_PREFIX + NamingStrategyUtils.propertyToColumnName(propertyName);
    }

    public String tableName(String tableName) {
        return NamingStrategyUtils.tableName(tableName);
    }

    public String collectionTableName(String ownerEntity, String ownerEntityTable,
            String associatedEntity, String associatedEntityTable, String propertyName) {
        return collectionTableName(ownerEntityTable, associatedEntityTable, null);
    }

    public String collectionTableName(String ownerEntityTable, String associatedEntityTable) {
        return collectionTableName(ownerEntityTable, associatedEntityTable, null);
    }

    public String collectionTableName(String ownerEntityTable, String associatedEntityTable,
            String ownerProperty) {
        return addTablePrefix(NamingStrategyUtils.collectionTableName(ownerEntityTable,
                associatedEntityTable, ownerProperty));
    }

    public String joinKeyColumnName(String joinedColumn, String joinedTable) {
        return columnName(joinedColumn) + "_id";
    }

    public String foreignKeyColumnName(String propertyName, String propertyEntityName,
            String propertyTableName, String referencedColumnName) {
        return foreignKeyColumnName(propertyName, propertyTableName);
    }

    public String foreignKeyColumnName(String propertyName, String propertyTableName) {
        String header = propertyName != null ? NamingStrategyUtils.unqualify(propertyName)
                : propertyTableName;
        return FOREIGN_KEY_COLUMN_PREFIX + columnName(header);
    }

    public String logicalCollectionColumnName(String columnName, String propertyName,
            String referencedColumn) {
        return InternalUtils.StringUtils.isEmpty(columnName) ? concat(
                NamingStrategyUtils.unqualify(propertyName), referencedColumn) : columnName;
    }

    public String columnName(String columnName) {
        return NamingStrategyUtils.addUnderscores(columnName);
    }

    /**
     * Returns either the table name if explicit or if there is an associated table, the
     * concatenation of owner entity table and associated table otherwise the concatenation of owner
     * entity table and the unqualified property name
     */
    public String logicalCollectionTableName(String tableName, String ownerEntityTable,
            String associatedEntityTable, String propertyName) {
        if (tableName != null) {
            return tableName;
        }

        return concat(ownerEntityTable, associatedEntityTable != null ? associatedEntityTable
                : NamingStrategyUtils.unqualify(propertyName));
    }

    public String logicalColumnName(String columnName, String propertyName) {
        return InternalUtils.StringUtils.isEmpty(columnName) ? NamingStrategyUtils
                .unqualify(propertyName) : columnName;
    }

    public String foreignKeyName(String tableName, String columnName) {
        return FOREIGN_KEY_PREFIX + concat(tableName, columnName);
    }

    public String uniqueKeyName(String tableName, String columnName) {
        return UNIQUE_KEY_PREFIX + concat(tableName, columnName);
    }

    private String addTablePrefix(String name) {
        return tablePrefix == null ? name : tablePrefix + name;
    }

}
