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

    public static List<String> getComponentColumnNames(PersistentClass persistentClass,
            String propertyName) {

        Property componentBinding = persistentClass.getProperty(propertyName);
        assertThat(componentBinding).isNotNull();

        Component component = (Component) componentBinding.getValue();
        return getColumNames(component.getColumnIterator());
    }

    public static List<String> getColumNames(Iterator<?> columnIterator) {
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
        new SchemaUpdate().setDelimiter(";").setFormat(true).execute(EnumSet.of(TargetType.STDOUT),
                createMetadata(serviceRegistry, strategy, annotatedClasses));
    }

}
