package com.github.fluent.hibernate.strategy;

import com.github.fluent.hibernate.internal.util.InternalUtils;

/**
 * A naming strategy for database column and table names.
 *
 * @author S.Samsonov
 * @author V.Ladynev
 */
class HibernateNamingStrategy {

    private static final String COLUMN_NAME_PREFIX = "f_";

    private static final String FOREIGN_KEY_PREFIX = "fk_";

    /** Table's prefix. */
    private String tablePrefix;

    public String tableName(String tableName) {
        return NamingStrategyUtils.addUnderscores(tableName);
    }

    public String classToTableName(final String className) {
        return tablePrefix(NamingStrategyUtils.classToTableName(className));
    }

    public String collectionTableName(final String ownerEntity, final String ownerEntityTable,
            final String associatedEntity, final String associatedEntityTable,
            final String propertyName) {
        return tablePrefix(NamingStrategyUtils.collectionTableName(ownerEntityTable,
                associatedEntityTable));
    }

    public String foreignKeyColumnName(final String propertyName, final String propertyEntityName,
            final String propertyTableName, final String referencedColumnName) {
        String header = propertyName != null ? NamingStrategyUtils.unqualify(propertyName)
                : propertyTableName;
        return FOREIGN_KEY_PREFIX + columnName(header);
    }

    public String joinKeyColumnName(final String joinedColumn, final String joinedTable) {
        return columnName(joinedColumn) + "_id";
    }

    public String propertyToColumnName(final String propertyName) {
        return COLUMN_NAME_PREFIX + NamingStrategyUtils.propertyToColumnName(propertyName);
    }

    public String logicalCollectionColumnName(String columnName, String propertyName,
            String referencedColumn) {
        return InternalUtils.StringUtils.isEmpty(columnName) ? NamingStrategyUtils
                .unqualify(propertyName) + "_" + referencedColumn : columnName;

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

        return new StringBuilder(ownerEntityTable)
                .append("_")
                .append(associatedEntityTable != null ? associatedEntityTable : NamingStrategyUtils
                .unqualify(propertyName)).toString();

    }

    public String logicalColumnName(String columnName, String propertyName) {
        return InternalUtils.StringUtils.isEmpty(columnName) ? NamingStrategyUtils
                .unqualify(propertyName) : columnName;
    }

    private String tablePrefix(String name) {
        return tablePrefix == null ? name : tablePrefix + name;
    }

    public void setTablePrefix(final String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

}
