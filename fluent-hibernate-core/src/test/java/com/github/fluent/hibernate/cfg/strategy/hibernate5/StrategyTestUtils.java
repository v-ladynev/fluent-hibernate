package com.github.fluent.hibernate.cfg.strategy.hibernate5;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataBuilder;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.Column;
import org.hibernate.mapping.Component;
import org.hibernate.mapping.ForeignKey;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Property;
import org.hibernate.mapping.Table;
import org.hibernate.mapping.UniqueKey;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.hibernate.tool.schema.TargetType;

import com.github.fluent.hibernate.internal.util.InternalUtils;

/**
 *
 * @author V.Ladynev
 */
public final class StrategyTestUtils {

    private StrategyTestUtils() {

    }

    public static Table getTable(Metadata metadata, Class<?> persistent) {
        PersistentClass result = metadata.getEntityBinding(persistent.getName());
        assertThat(result).isNotNull();
        return result.getTable();
    }

    public static Table getCollectionTable(Metadata metadata, Class<?> persistent,
            String propertyName) {
        Collection result = metadata
                .getCollectionBinding(persistent.getName() + "." + propertyName);
        assertThat(result).isNotNull();
        return result.getCollectionTable();
    }

    public static String getColumnName(Metadata metadata, Class<?> persistent,
            String propertyName) {
        PersistentClass binding = metadata.getEntityBinding(persistent.getName());
        assertThat(binding).isNotNull();
        Column result = getColumn(binding, propertyName);
        assertThat(result).isNotNull();
        return result.getName();
    }

    public static Column getColumn(PersistentClass persistentClass, String propertyName) {
        Property property = persistentClass.getProperty(propertyName);
        assertThat(property).isNotNull();
        return (Column) property.getColumnIterator().next();
    }

    public static List<String> getComponentColumnNames(PersistentClass persistentClass,
            String componentPropertyName) {

        Property componentBinding = persistentClass.getProperty(componentPropertyName);
        assertThat(componentBinding).isNotNull();

        Component component = (Component) componentBinding.getValue();
        return getColumNames(component.getColumnIterator());
    }

    public static List<String> getColumNames(Table table) {
        return getColumNames(table.getColumnIterator());
    }

    private static List<String> getColumNames(Iterator<?> columnIterator) {
        ArrayList<String> result = InternalUtils.CollectionUtils.newArrayList();

        while (columnIterator.hasNext()) {
            Column column = (Column) columnIterator.next();
            result.add(column.getQuotedName());
        }

        return result;
    }

    public static List<String> getForeignKeyConstraintNames(Table table) {
        ArrayList<String> result = InternalUtils.CollectionUtils.newArrayList();

        for (ForeignKey foreignKey : table.getForeignKeys().values()) {
            result.add(foreignKey.getName());
        }

        return result;
    }

    public static List<String> getUniqueConstraintNames(Table table) {
        ArrayList<String> result = InternalUtils.CollectionUtils.newArrayList();

        Iterator<UniqueKey> iterator = table.getUniqueKeyIterator();

        while (iterator.hasNext()) {
            UniqueKey uniqueKey = iterator.next();
            result.add(uniqueKey.getName());
        }

        return result;
    }

    public static Metadata createMetadata(ServiceRegistry serviceRegistry,
            ImplicitNamingStrategy strategy, Class<?>... annotatedClasses) {
        MetadataSources metadataSources = new MetadataSources(serviceRegistry);

        for (Class<?> annotatedClass : annotatedClasses) {
            metadataSources.addAnnotatedClass(annotatedClass);
        }

        MetadataBuilder builder = metadataSources.getMetadataBuilder();

        if (strategy != null) {
            builder.applyImplicitNamingStrategy(strategy);
        }

        return builder.build();
    }

    public static void logSchemaUpdate(ServiceRegistry serviceRegistry,
            ImplicitNamingStrategy strategy, Class<?>... annotatedClasses) {
        logSchemaUpdate(createMetadata(serviceRegistry, strategy, annotatedClasses));
    }

    public static void logSchemaUpdate(Metadata metadata) {
        new SchemaUpdate().setDelimiter(";").setFormat(true).execute(EnumSet.of(TargetType.STDOUT),
                metadata);
    }

}
