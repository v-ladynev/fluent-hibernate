package com.github.fluent.hibernate.strategy;

import com.github.fluent.hibernate.internal.util.InternalUtils;

/**
 * A naming strategy for column and table names.
 *
 * @author S.Samsonov
 * @author V.Ladynev
 */
class HibernateNamingStrategy {

    private static final String COLUMN_NAME_PREFIX = "f_";

    private static final String FOREIGN_KEY_PREFIX = "fk_";

    /** Table's prefix. */
    private String tablePrefix;

    public String classToTableName(String className) {
        return tablePrefix(NamingStrategyUtils.classToTableName(className));
    }

    public String propertyToColumnName(String propertyName) {
        return COLUMN_NAME_PREFIX + NamingStrategyUtils.propertyToColumnName(propertyName);
    }

    public String tableName(String tableName) {
        return NamingStrategyUtils.addUnderscores(tableName);
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
        return tablePrefix(NamingStrategyUtils.collectionTableName(ownerEntityTable,
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
        return FOREIGN_KEY_PREFIX + columnName(header);
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

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
    }

}
