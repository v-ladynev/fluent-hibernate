package com.github.fluent.hibernate.cfg.strategy.hibernate5;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.List;

import org.hibernate.boot.model.naming.EntityNaming;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitBasicColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitForeignKeyNameSource;
import org.hibernate.boot.model.naming.ImplicitJoinColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitJoinTableNameSource;
import org.hibernate.boot.model.naming.ImplicitNameSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.model.naming.ImplicitUniqueKeyNameSource;
import org.hibernate.boot.model.source.spi.AttributePath;
import org.hibernate.cfg.Ejb3Column;
import org.hibernate.internal.util.ReflectHelper;

import com.github.fluent.hibernate.annotations.FluentName;
import com.github.fluent.hibernate.cfg.strategy.HibernateNamingStrategy;
import com.github.fluent.hibernate.cfg.strategy.JoinTableNames;
import com.github.fluent.hibernate.cfg.strategy.JoinTableNames.TableDescription;
import com.github.fluent.hibernate.cfg.strategy.NamingStrategyUtils;
import com.github.fluent.hibernate.cfg.strategy.StrategyOptions;
import com.github.fluent.hibernate.internal.util.InternalUtils;

/**
 * A naming strategy for Hibernate 5.
 *
 * @author V.Ladynev
 */
public class Hibernate5NamingStrategy extends ImplicitNamingStrategyJpaCompliantImpl {

    private static final long serialVersionUID = 3482010804082494311L;

    private final HibernateNamingStrategy strategy;

    private final JoinTableNames joinTableNames = new JoinTableNames();

    public Hibernate5NamingStrategy() {
        this(new StrategyOptions());
    }

    public Hibernate5NamingStrategy(StrategyOptions options) {
        strategy = new HibernateNamingStrategy(options);
    }

    public void setOptions(StrategyOptions options) {
        strategy.setOptions(options);
    }

    public void setTablePrefix(String tablePrefix) {
        strategy.getOptions().setTablePrefix(tablePrefix);
    }

    public void setMaxLength(int maxLength) {
        strategy.getOptions().setMaxLength(maxLength);
    }

    /**
     * Get a name of a table for a persistent.
     */
    @Override
    protected String transformEntityName(EntityNaming entityNaming) {
        return strategy.classToTableName(entityNaming.getEntityName());
    }

    @Override
    public Identifier determineBasicColumnName(ImplicitBasicColumnNameSource source) {
        // don't know what it means
        if (source.isCollectionElement()) {
            return toIdentifier("elt", source);
        }

        AttributePath attributePath = source.getAttributePath();
        String propertyName = getPropertyName(attributePath);
        String parentPropertyName = getPropertyName(attributePath.getParent());

        Ejb3Column column = getEjb3Column(source);

        if (isEmbedded(column)) {
            String fluentNamePrefix = getFluentNamePrefix(column, parentPropertyName);

            boolean hasEmbeddedPrefix = !InternalUtils.StringUtils.isEmpty(fluentNamePrefix);
            String prefix = hasEmbeddedPrefix ? fluentNamePrefix : parentPropertyName;

            boolean dontTouchPrefix = hasEmbeddedPrefix;
            return toIdentifier(
                    strategy.embeddedPropertyToColumnName(prefix, propertyName, dontTouchPrefix),
                    source);
        }

        // Hibernate calls this method the first time for @Embedded column, but doesn't use a result
        return toIdentifier(strategy.propertyToColumnName(propertyName), source);
    }

    // TODO remove ReflectHelper
    private static String getFluentNamePrefix(Ejb3Column column, String propertyName) {
        Class<?> mappedClass = column.getPropertyHolder().getPersistentClass().getMappedClass();

        try {
            String result = getFluentNamePrefix(findGetterMethod(mappedClass, propertyName));
            return result == null ? getFluentNamePrefix(findField(mappedClass, propertyName))
                    : result;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static AccessibleObject findGetterMethod(Class<?> containerClass, String propertyName) {
        try {
            return ReflectHelper.findGetterMethod(containerClass, propertyName);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static AccessibleObject findField(Class<?> containerClass, String propertyName) {
        try {
            return ReflectHelper.findField(containerClass, propertyName);
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    private static String getFluentNamePrefix(AccessibleObject accessableObject) {
        if (accessableObject == null) {
            return null;
        }

        FluentName annotation = accessableObject.getAnnotation(FluentName.class);
        return annotation == null ? null : annotation.prefix();
    }

    private static boolean isEmbedded(Ejb3Column column) {
        return column.getPropertyHolder().isComponent();
    }

    private static Ejb3Column getEjb3Column(ImplicitBasicColumnNameSource source) {
        try {
            Field ejb3ColumnField = source.getClass().getDeclaredField("this$0");
            ejb3ColumnField.setAccessible(true);
            return (Ejb3Column) ejb3ColumnField.get(source);
        } catch (Exception ex) {
            throw InternalUtils.toRuntimeException(ex);
        }
    }

    @Override
    public Identifier determineJoinColumnName(ImplicitJoinColumnNameSource source) {
        String propertyTableName = NamingStrategyUtils
                .unqualify(source.getEntityNaming().getEntityName());
        // a property name is null for join tables for an owner table foreign key
        String propertyName = getPropertyName(source.getAttributePath());
        String result = strategy.foreignKeyColumnName(propertyName, propertyTableName);
        return toIdentifier(result, source);
    }

    @Override
    public Identifier determineJoinTableName(ImplicitJoinTableNameSource source) {
        String ownerEntityTable = source.getOwningEntityNaming().getEntityName();
        String associatedEntityTable = source.getNonOwningEntityNaming().getEntityName();

        String propertyName = getPropertyName(source.getAssociationOwningAttributePath());

        String tableName = strategy.joinTableName(ownerEntityTable, associatedEntityTable);

        TableDescription description = new TableDescription(ownerEntityTable, associatedEntityTable,
                propertyName);

        String result = joinTableNames.hasSameNameForOtherProperty(tableName, description)
                ? strategy.joinTableName(ownerEntityTable, associatedEntityTable, propertyName)
                : tableName;

        joinTableNames.put(result, description);

        return toIdentifier(result, source);
    }

    /**
     * Generates a name for a foreign key constraint.
     */
    @Override
    public Identifier determineForeignKeyName(ImplicitForeignKeyNameSource source) {
        List<Identifier> columnNames = source.getColumnNames();

        // constraints are supported for one column only
        if (InternalUtils.CollectionUtils.size(columnNames) != 1) {
            return super.determineForeignKeyName(source);
        }

        String result = strategy.foreignKeyConstraintName(source.getTableName().getText(),
                columnNames.get(0).getText());

        return toIdentifier(result, source);
    }

    @Override
    public Identifier determineUniqueKeyName(ImplicitUniqueKeyNameSource source) {
        List<Identifier> columnNames = source.getColumnNames();

        // constraints are supported for one column only
        if (InternalUtils.CollectionUtils.size(columnNames) != 1) {
            return super.determineUniqueKeyName(source);
        }

        String result = strategy.uniqueKeyConstraintName(source.getTableName().getText(),
                columnNames.get(0).getText());

        return toIdentifier(result, source);
    }

    private static String getPropertyName(AttributePath attributePath) {
        return attributePath == null ? null : attributePath.getProperty();
    }

    private static Identifier toIdentifier(String stringForm, ImplicitNameSource source) {
        return source.getBuildingContext().getMetadataCollector().getDatabase().getJdbcEnvironment()
                .getIdentifierHelper().toIdentifier(stringForm);
    }

}
