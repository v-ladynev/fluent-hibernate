package com.github.fluent.hibernate.cfg.strategy.hibernate5.adapter;

import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitAnyDiscriminatorColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitAnyKeyColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitBasicColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitCollectionTableNameSource;
import org.hibernate.boot.model.naming.ImplicitDiscriminatorColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitEntityNameSource;
import org.hibernate.boot.model.naming.ImplicitForeignKeyNameSource;
import org.hibernate.boot.model.naming.ImplicitIdentifierColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitIndexColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitIndexNameSource;
import org.hibernate.boot.model.naming.ImplicitJoinColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitJoinTableNameSource;
import org.hibernate.boot.model.naming.ImplicitMapKeyColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategy;
import org.hibernate.boot.model.naming.ImplicitPrimaryKeyJoinColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitTenantIdColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitUniqueKeyNameSource;
import org.hibernate.boot.model.source.spi.AttributePath;
import org.hibernate.boot.spi.MetadataBuildingContext;
import org.hibernate.cfg.NamingStrategy;

import com.github.fluent.hibernate.cfg.strategy.NamingStrategyUtils;

/**
 * Adapter to adapt Hibernate 4 naming strategies to Hibernate 5.
 *
 * @author V.Ladynev
 */
public class Hibernate5NamingStrategyAdapter implements ImplicitNamingStrategy {

    private NamingStrategy delegate;

    private ImplicitNamingStrategy implicitNamingStrategy;

    public Hibernate5NamingStrategyAdapter(NamingStrategy delegate,
            ImplicitNamingStrategy implicitNamingStrategy) {
        this.delegate = delegate;
        this.implicitNamingStrategy = implicitNamingStrategy;
    }

    @Override
    public Identifier determinePrimaryTableName(ImplicitEntityNameSource source) {
        String result = delegate.classToTableName(source.getEntityNaming().getEntityName());
        return toIdentifier(result, source.getBuildingContext());
    }

    @Override
    public Identifier determineJoinTableName(ImplicitJoinTableNameSource source) {
        String ownerEntity = source.getOwningEntityNaming().getEntityName();
        String ownerEntityTable = NamingStrategyUtils.unqualifyEntityName(ownerEntity);
        String associatedEntity = source.getNonOwningEntityNaming().getEntityName();
        String associatedEntityTable = NamingStrategyUtils.unqualifyEntityName(associatedEntity);
        String propertyName = getPropertyName(source.getAssociationOwningAttributePath());

        String result = delegate.collectionTableName(ownerEntity, ownerEntityTable,
                associatedEntity, associatedEntityTable, propertyName);
        return toIdentifier(result, source.getBuildingContext());
    }

    @Override
    public Identifier determineCollectionTableName(ImplicitCollectionTableNameSource source) {
        String ownerEntity = source.getOwningEntityNaming().getEntityName();
        String ownerEntityTable = NamingStrategyUtils.unqualifyEntityName(ownerEntity);
        String associatedEntity = null;
        String associatedEntityTable = null;
        String propertyName = getPropertyName(source.getOwningAttributePath());

        String result = delegate.collectionTableName(ownerEntity, ownerEntityTable,
                associatedEntity, associatedEntityTable, propertyName);
        return toIdentifier(result, source.getBuildingContext());
    }

    @Override
    public Identifier determineBasicColumnName(ImplicitBasicColumnNameSource source) {
        AttributePath attributePath = source.getAttributePath();
        String propertyName = getPropertyName(attributePath);

        String result = delegate.propertyToColumnName(propertyName);
        return toIdentifier(result, source.getBuildingContext());
    }

    @Override
    public Identifier determineJoinColumnName(ImplicitJoinColumnNameSource source) {
        String propertyName = getPropertyName(source.getAttributePath());
        String propertyEntityName = source.getEntityNaming().getEntityName();
        String propertyTableName = NamingStrategyUtils.unqualifyEntityName(propertyEntityName);
        String referencedColumnName = source.getReferencedColumnName().getText();

        String result = delegate.foreignKeyColumnName(propertyName, propertyEntityName,
                propertyTableName, referencedColumnName);
        return toIdentifier(result, source.getBuildingContext());
    }

    @Override
    public Identifier determineDiscriminatorColumnName(
            ImplicitDiscriminatorColumnNameSource source) {
        return implicitNamingStrategy.determineDiscriminatorColumnName(source);
    }

    @Override
    public Identifier determineTenantIdColumnName(ImplicitTenantIdColumnNameSource source) {
        return implicitNamingStrategy.determineTenantIdColumnName(source);
    }

    @Override
    public Identifier determineIdentifierColumnName(ImplicitIdentifierColumnNameSource source) {
        return implicitNamingStrategy.determineIdentifierColumnName(source);
    }

    @Override
    public Identifier determinePrimaryKeyJoinColumnName(
            ImplicitPrimaryKeyJoinColumnNameSource source) {
        return implicitNamingStrategy.determinePrimaryKeyJoinColumnName(source);
    }

    @Override
    public Identifier determineAnyDiscriminatorColumnName(
            ImplicitAnyDiscriminatorColumnNameSource source) {
        return implicitNamingStrategy.determineAnyDiscriminatorColumnName(source);
    }

    @Override
    public Identifier determineAnyKeyColumnName(ImplicitAnyKeyColumnNameSource source) {
        return implicitNamingStrategy.determineAnyKeyColumnName(source);
    }

    @Override
    public Identifier determineMapKeyColumnName(ImplicitMapKeyColumnNameSource source) {
        return implicitNamingStrategy.determineMapKeyColumnName(source);
    }

    @Override
    public Identifier determineListIndexColumnName(ImplicitIndexColumnNameSource source) {
        return implicitNamingStrategy.determineListIndexColumnName(source);
    }

    /**
     * Generates a name for a foreign key constraint.
     */
    @Override
    public Identifier determineForeignKeyName(ImplicitForeignKeyNameSource source) {
        return implicitNamingStrategy.determineForeignKeyName(source);
    }

    /**
     * Generates a name for a unique constraint.
     */
    @Override
    public Identifier determineUniqueKeyName(ImplicitUniqueKeyNameSource source) {
        return implicitNamingStrategy.determineUniqueKeyName(source);
    }

    @Override
    public Identifier determineIndexName(ImplicitIndexNameSource source) {
        return implicitNamingStrategy.determineIndexName(source);
    }

    private static String getPropertyName(AttributePath attributePath) {
        return attributePath == null ? null : attributePath.getProperty();
    }

    private static Identifier toIdentifier(String stringForm,
            MetadataBuildingContext buildingContext) {
        return buildingContext.getMetadataCollector().getDatabase().getJdbcEnvironment()
                .getIdentifierHelper().toIdentifier(stringForm);
    }

}
