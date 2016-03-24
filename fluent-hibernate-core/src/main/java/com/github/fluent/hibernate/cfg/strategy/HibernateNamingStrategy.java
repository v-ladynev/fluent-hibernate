package com.github.fluent.hibernate.cfg.strategy;

import static com.github.fluent.hibernate.cfg.strategy.NamingStrategyUtils.concat;
import static com.github.fluent.hibernate.cfg.strategy.NamingStrategyUtils.propertyToName;
import static com.github.fluent.hibernate.cfg.strategy.NamingStrategyUtils.propertyToPluralizedName;
import static com.github.fluent.hibernate.internal.util.InternalUtils.StringUtils.joinWithSpace;

import com.github.fluent.hibernate.internal.util.InternalUtils;

/**
 * A naming strategy for column and table names.
 *
 * @author S.Samsonov
 * @author V.Ladynev
 */
public class HibernateNamingStrategy {

    private static final String COLUMN_NAME_PREFIX = "f_";

    private static final String FOREIGN_KEY_COLUMN_PREFIX = "fk_";

    private static final String FOREIGN_KEY_PREFIX = "fk_";

    private static final String UNIQUE_KEY_PREFIX = "uk_";

    /** Table's prefix. */
    private String tablePrefix;

    private boolean hasTablePrefix;

    // private final int maxLength = 19;

    private final int maxLength = 0;

    private final boolean restrictTableNames = false;

    private final boolean restrictColumnNames = true;

    private final boolean restrictEmbeddedColumnNames = true;

    private final boolean restrictJoinTableNames = false;

    private final boolean resrictConstraintNames = false;

    public void setTablePrefix(String tablePrefix) {
        this.tablePrefix = tablePrefix;
        hasTablePrefix = !InternalUtils.StringUtils.isEmpty(tablePrefix);
    }

    public String classToTableName(String className) {
        String result = addTablePrefix(propertyToPluralizedName(className));

        if (needRestrict(restrictTableNames)) {
            return assertName(new NameShorter(maxLength, hasTablePrefix).tableName(result),
                    className, "@Table(name=\"prefix_table_name\")");
        }

        return result;
    }

    public String propertyToColumnName(String propertyName) {
        String result = COLUMN_NAME_PREFIX + propertyToName(propertyName);

        if (needRestrict(restrictColumnNames)) {
            final boolean dontTouchFirst = true;
            return assertName(new NameShorter(maxLength, dontTouchFirst).columnName(result),
                    propertyName, "@Column(name=\"f_column_name\")");
        }

        return result;
    }

    public String embeddedPropertyToColumnName(String prefix, String embeddedPropertyName,
            boolean dontTouchPrefix) {
        String fullPrefix = COLUMN_NAME_PREFIX
                + (dontTouchPrefix ? prefix : propertyToName(prefix));
        String columnPostfix = propertyToName(embeddedPropertyName);
        String column = concat(fullPrefix, columnPostfix);

        if (!needRestrict(restrictEmbeddedColumnNames)) {
            return column;
        }

        String result = null;
        if (dontTouchPrefix) {
            final boolean dontTouchFirst = false;
            result = concat(fullPrefix, new NameShorter(maxLength - fullPrefix.length(),
                    dontTouchFirst).embeddedColumnName(columnPostfix));
        } else {
            final boolean dontTouchFirst = true;
            result = new NameShorter(maxLength, dontTouchFirst).embeddedColumnName(column);
        }

        return assertName(result, joinWithSpace(prefix, embeddedPropertyName),
                "@AttributeOverrides({@AttributeOverride(name=\"propertyName\", "
                        + "column=@Column(\"f_column_name\"))");

    }

    public String joinTableName(String ownerEntityTable, String associatedEntityTable) {
        return joinTableName(ownerEntityTable, associatedEntityTable, null);
    }

    public String joinTableName(String ownerEntityTable, String associatedEntityTable,
            String ownerProperty) {
        String ownerTable = propertyToPluralizedName(ownerEntityTable);
        String associatedTable = propertyToPluralizedName(associatedEntityTable);

        String result = ownerProperty == null ? concat(ownerTable, associatedTable) : concat(
                concat(ownerTable, propertyToName(ownerProperty)), associatedTable);

        result = addTablePrefix(result);

        if (needRestrict(restrictJoinTableNames)) {
            return assertName(new NameShorter(maxLength, hasTablePrefix).joinTableName(result),
                    joinWithSpace(ownerEntityTable, associatedEntityTable, ownerProperty),
                    "@JoinTable(name=\"prefix_join_table_name\")");
        }

        return result;
    }

    /**
     * For Hibernate 4 only.
     */
    public String joinKeyColumnName(String joinedColumn, String joinedTable) {
        // TODO check
        // return propertyToColumnName(joinedColumn) + "_id";

        return NamingStrategyUtils.addUnderscores(joinedColumn) + "_id";
    }

    public String foreignKeyColumnName(String propertyName, String propertyTableName) {
        String header = propertyName != null ? NamingStrategyUtils.unqualify(propertyName)
                : propertyTableName;
        String result = FOREIGN_KEY_COLUMN_PREFIX + NamingStrategyUtils.addUnderscores(header);

        if (needRestrict(restrictColumnNames)) {
            final boolean dontTouchFirst = true;
            return assertName(new NameShorter(maxLength, dontTouchFirst).columnName(result),
                    joinWithSpace(propertyTableName, propertyName), "@JoinColumn(name=\"fk_name\")");
        }

        return result;
    }

    public String foreignKeyName(String tableName, String columnName) {
        String result = FOREIGN_KEY_PREFIX + concat(tableName, columnName);

        if (needRestrict(resrictConstraintNames)) {
            final boolean dontTouchFirst = true;
            return assertName(new NameShorter(maxLength, dontTouchFirst).constraintName(result),
                    joinWithSpace(tableName, columnName), "@ForeignKey(name=\"fk_name\")");
        }

        return result;

    }

    public String uniqueKeyName(String tableName, String columnName) {
        String result = UNIQUE_KEY_PREFIX + concat(tableName, columnName);

        if (needRestrict(resrictConstraintNames)) {
            final boolean dontTouchFirst = true;
            return assertName(new NameShorter(maxLength, dontTouchFirst).constraintName(result),
                    joinWithSpace(tableName, columnName),
                    "@UniqueConstraint (if it is appropriate)");
        }

        return result;
    }

    private String assertName(String name, String object, String annotation) {
        if (name.length() > maxLength) {
            InternalUtils.Asserts.fail(String.format(
                    "Can't restrict name of '%s'. Result '%s' is too long. "
                            + "Use '%s' to hardcode the name", object, name, annotation));
        }

        return name;
    }

    private boolean needRestrict(boolean toCheck) {
        return maxLength > 0 && toCheck;
    }

    private String addTablePrefix(String name) {
        return tablePrefix == null ? name : tablePrefix + name;
    }

}
