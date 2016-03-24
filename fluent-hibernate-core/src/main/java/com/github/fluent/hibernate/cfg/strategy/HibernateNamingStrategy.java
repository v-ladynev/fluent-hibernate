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

    private StrategyOptions options = new StrategyOptions();

    public StrategyOptions getOptions() {
        return options;
    }

    public void setOptions(StrategyOptions options) {
        this.options = options;
    }

    public String classToTableName(String className) {
        String result = options.addTablePrefix(propertyToPluralizedName(className));

        if (needRestrict(options.isRestrictTableNames())) {
            return assertName(
                    new NameShorter(options.getMaxLength(), options.isHasTablePrefix())
                            .tableName(result),
                    className, "@Table(name=\"prefix_table_name\")");
        }

        return result;
    }

    public String propertyToColumnName(String propertyName) {
        String result = options.getColumnNamePrefix() + propertyToName(propertyName);

        if (needRestrict(options.isRestrictColumnNames())) {
            final boolean dontTouchFirst = true;
            return assertName(
                    new NameShorter(options.getMaxLength(), dontTouchFirst).columnName(result),
                    propertyName, "@Column(name=\"f_column_name\")");
        }

        return result;
    }

    public String embeddedPropertyToColumnName(String prefix, String embeddedPropertyName,
            boolean dontTouchPrefix) {
        String fullPrefix = options.getColumnNamePrefix()
                + (dontTouchPrefix ? prefix : propertyToName(prefix));
        String columnPostfix = propertyToName(embeddedPropertyName);
        String column = concat(fullPrefix, columnPostfix);

        if (!needRestrict(options.isRestrictEmbeddedColumnNames())) {
            return column;
        }

        String result = null;
        if (dontTouchPrefix) {
            final boolean dontTouchFirst = false;
            result = concat(fullPrefix,
                    new NameShorter(options.getMaxLength() - fullPrefix.length(), dontTouchFirst)
            .embeddedColumnName(columnPostfix));
        } else {
            final boolean dontTouchFirst = true;
            result = new NameShorter(options.getMaxLength(), dontTouchFirst)
            .embeddedColumnName(column);
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

        result = options.addTablePrefix(result);

        if (needRestrict(options.isRestrictJoinTableNames())) {
            return assertName(
                    new NameShorter(options.getMaxLength(), options.isHasTablePrefix())
                    .joinTableName(result),
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
        String result = options.getForeignKeyColumnPrefix()
                + NamingStrategyUtils.addUnderscores(header);

        if (needRestrict(options.isRestrictColumnNames())) {
            final boolean dontTouchFirst = true;
            return assertName(
                    new NameShorter(options.getMaxLength(), dontTouchFirst).columnName(result),
                    joinWithSpace(propertyTableName, propertyName), "@JoinColumn(name=\"fk_name\")");
        }

        return result;
    }

    public String foreignKeyName(String tableName, String columnName) {
        String result = options.getForeignKeyPrefix() + concat(tableName, columnName);

        if (needRestrict(options.isRestrictConstraintNames())) {
            final boolean dontTouchFirst = true;
            return assertName(
                    new NameShorter(options.getMaxLength(), dontTouchFirst).constraintName(result),
                    joinWithSpace(tableName, columnName), "@ForeignKey(name=\"fk_name\")");
        }

        return result;

    }

    public String uniqueKeyName(String tableName, String columnName) {
        String result = options.getUniqueKeyPrefix() + concat(tableName, columnName);

        if (needRestrict(options.isRestrictConstraintNames())) {
            final boolean dontTouchFirst = true;
            return assertName(
                    new NameShorter(options.getMaxLength(), dontTouchFirst).constraintName(result),
                    joinWithSpace(tableName, columnName),
                    "@UniqueConstraint (if it is appropriate)");
        }

        return result;
    }

    private String assertName(String name, String object, String annotation) {
        int currentLength = name.length();
        int maxLength = options.getMaxLength();
        if (currentLength > maxLength) {
            InternalUtils.Asserts.fail(String.format(
                    "Can't restrict name of '%s'. Result '%s' has the length %d, max length is %d. "
                            + "Use '%s' to hardcode the name", object, name, currentLength,
                    maxLength, annotation));
        }

        return name;
    }

    private boolean needRestrict(boolean toCheck) {
        return options.getMaxLength() > 0 && toCheck;
    }

}
