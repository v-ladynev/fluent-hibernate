package com.github.fluent.hibernate.cfg.strategy;

import static com.github.fluent.hibernate.cfg.strategy.NamingStrategyUtils.concat;
import static com.github.fluent.hibernate.cfg.strategy.NamingStrategyUtils.propertyToName;
import static com.github.fluent.hibernate.cfg.strategy.NamingStrategyUtils.propertyToPluralizedName;
import static com.github.fluent.hibernate.internal.util.InternalUtils.StringUtils.join;
import static com.github.fluent.hibernate.internal.util.InternalUtils.StringUtils.joinWithSpace;

import com.github.fluent.hibernate.internal.util.InternalUtils;

/**
 * A naming strategy for column and table names.
 *
 * @author S.Samsonov
 * @author V.Ladynev
 */
public class HibernateNamingStrategy {

    private StrategyOptions options;

    public HibernateNamingStrategy(StrategyOptions options) {
        this.options = options;
    }

    public StrategyOptions getOptions() {
        return options;
    }

    public void setOptions(StrategyOptions options) {
        this.options = options;
    }

    public String classToTableName(String className) {
        String result = join(options.getTablePrefix(), propertyToPluralizedName(className));

        if (needRestrict(options.isRestrictTableNames())) {
            return assertName(new NameShorter(options.getMaxLength(), options.hasTablePrefix())
                    .tableName(result), className, "@Table(name=\"prefix_table_name\")");
        }

        return result;
    }

    public String propertyToColumnName(String propertyName) {
        String result = join(options.getColumnPrefix(), propertyToName(propertyName));

        if (needRestrict(options.isRestrictColumnNames())) {
            return assertName(new NameShorter(options.getMaxLength(), options.hasColumnPrefix())
                    .columnName(result), propertyName, "@Column(name=\"f_column_name\")");
        }

        return result;
    }

    public String embeddedPropertyToColumnName(String prefix, String embeddedPropertyName,
            boolean dontTouchPrefix) {
        String fullPrefix = join(options.getColumnPrefix(),
                dontTouchPrefix ? prefix : propertyToName(prefix));
        String columnPostfix = propertyToName(embeddedPropertyName);

        if (!needRestrict(options.isRestrictEmbeddedColumnNames())) {
            return concat(fullPrefix, columnPostfix);
        }

        String result = null;
        if (dontTouchPrefix) {
            final boolean dontTouchFirst = false;
            int maxPostfixLength = options.getMaxLength() - (fullPrefix.length() + 1);
            result = concat(fullPrefix, new NameShorter(maxPostfixLength, dontTouchFirst)
                    .embeddedColumnName(columnPostfix));
        } else {
            result = join(options.getColumnPrefix(),
                    embeddedColumnName(prefix, embeddedPropertyName,
                            options.getMaxLength() - options.getColumnPrefixLength()));
        }

        return assertName(result, joinWithSpace(prefix, embeddedPropertyName),
                "@AttributeOverrides({@AttributeOverride(name=\"propertyName\", "
                        + "column=@Column(\"f_column_name\"))");
    }

    private static String embeddedColumnName(String prefix, String property, int maxLength) {
        final boolean dontTouchFirst = false;

        maxLength--;// concat

        int maxPrefixlength = maxLength - property.length();
        String prefixShorted = new NameShorter(maxPrefixlength, dontTouchFirst)
                .embeddedColumnName(prefix);

        int maxPropertyLength = maxLength - prefixShorted.length();
        String propertyShorted = new NameShorter(maxPropertyLength, dontTouchFirst)
                .embeddedColumnName(property);

        return NamingStrategyUtils.concat(prefixShorted, propertyShorted);
    }

    public String joinTableName(String ownerEntityTable, String associatedEntityTable) {
        return joinTableName(ownerEntityTable, associatedEntityTable, null);
    }

    public String joinTableName(String ownerEntityTable, String associatedEntityTable,
            String ownerProperty) {
        String ownerTable = propertyToPluralizedName(ownerEntityTable);
        String associatedTable = propertyToPluralizedName(associatedEntityTable);

        String result = ownerProperty == null ? concat(ownerTable, associatedTable)
                : concat(concat(ownerTable, propertyToName(ownerProperty)), associatedTable);

        result = join(options.getTablePrefix(), result);

        if (needRestrict(options.isRestrictJoinTableNames())) {
            return assertName(
                    new NameShorter(options.getMaxLength(), options.hasTablePrefix())
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
        String result = join(options.getForeignKeyColumnPrefix(),
                NamingStrategyUtils.addUnderscores(header));

        if (needRestrict(options.isRestrictColumnNames())) {
            return assertName(
                    new NameShorter(options.getMaxLength(), options.hasForeignKeyColumnPrefix())
                            .columnName(result),
                    joinWithSpace(propertyTableName, propertyName),
                    "@JoinColumn(name=\"fk_name\")");
        }

        return result;
    }

    /**
     * Generates a name for a foreign key constraint.
     */
    public String foreignKeyConstraintName(String tableName, String columnName) {
        String result = join(options.getForeignKeyConstraintPrefix(),
                concat(tableName, columnName));

        if (needRestrict(options.isRestrictConstraintNames())) {
            return assertName(
                    new NameShorter(options.getMaxLength(), options.hasForeignKeyConstraintPrefix())
                            .constraintName(result),
                    "a foreign key constraint for " + joinWithSpace(tableName, columnName),
                    "@ForeignKey(name=\"fk_name\")");
        }

        return result;

    }

    public String uniqueKeyConstraintName(String tableName, String columnName) {
        String result = join(options.getUniqueKeyConstraintPrefix(), concat(tableName, columnName));

        if (needRestrict(options.isRestrictConstraintNames())) {
            return assertName(
                    new NameShorter(options.getMaxLength(), options.hasUniqueKeyConstraintPrefix())
                            .constraintName(result),
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
                            + "Use '%s' to hardcode the name",
                    object, name, currentLength, maxLength, annotation));
        }

        return name;
    }

    private boolean needRestrict(boolean toCheck) {
        return options.getMaxLength() > 0 && toCheck;
    }

}
