package com.github.fluent.hibernate.cfg.strategy.hibernate4;

import org.hibernate.cfg.ImprovedNamingStrategy;

import com.github.fluent.hibernate.cfg.strategy.HibernateNamingStrategy;
import com.github.fluent.hibernate.cfg.strategy.JoinTableNames;
import com.github.fluent.hibernate.cfg.strategy.JoinTableNames.TableDescription;
import com.github.fluent.hibernate.cfg.strategy.NamingStrategyUtils;
import com.github.fluent.hibernate.cfg.strategy.StrategyOptions;
import com.github.fluent.hibernate.internal.util.InternalUtils;

/**
 * A naming strategy for Hibernate 4.
 *
 * @author V.Ladynev
 */
public class Hibernate4NamingStrategy extends ImprovedNamingStrategy {

    private static final long serialVersionUID = -7668259354481970558L;

    private final HibernateNamingStrategy strategy;

    private final JoinTableNames joinTableNames = new JoinTableNames();

    public Hibernate4NamingStrategy() {
        this(new StrategyOptions());
    }

    public Hibernate4NamingStrategy(StrategyOptions options) {
        strategy = new HibernateNamingStrategy(options);
    }

    public void setTablePrefix(String tablePrefix) {
        strategy.getOptions().setTablePrefix(tablePrefix);
    }

    public void setMaxLength(int maxLength) {
        strategy.getOptions().setMaxLength(maxLength);
    }

    @Override
    public String classToTableName(String className) {
        return strategy.classToTableName(className);
    }

    @Override
    public String collectionTableName(String ownerEntity, String ownerEntityTable,
            String associatedEntity, String associatedEntityTable, String propertyName) {
        String tableName = strategy.joinTableName(ownerEntityTable, associatedEntityTable);

        TableDescription description = new TableDescription(ownerEntityTable,
                associatedEntityTable, propertyName);

        String result = joinTableNames.hasSameNameForOtherProperty(tableName, description) ? strategy
                .joinTableName(ownerEntityTable, associatedEntityTable, propertyName) : tableName;

        joinTableNames.put(result, description);

        return result;
    }

    @Override
    public String foreignKeyColumnName(String propertyName, String propertyEntityName,
            String propertyTableName, String referencedColumnName) {
        return strategy.foreignKeyColumnName(propertyName, propertyTableName);
    }

    @Override
    public String joinKeyColumnName(String joinedColumn, String joinedTable) {
        return strategy.joinKeyColumnName(joinedColumn, joinedTable);
    }

    @Override
    public String propertyToColumnName(String propertyName) {
        String forEmbedded = NamingStrategyUtils.addUnderscores(propertyName);
        return strategy.propertyToColumnName(forEmbedded);
    }

    @Override
    public String logicalColumnName(String columnName, String propertyName) {
        // it is for embeddable properties
        return InternalUtils.StringUtils.isEmpty(columnName) ? propertyName : columnName;
    }

}
