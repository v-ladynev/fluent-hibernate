package com.github.fluent.hibernate.strategy;

import org.hibernate.boot.model.naming.EntityNaming;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitBasicColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitJoinColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitJoinTableNameSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;

/**
 * A naming strategy for Hibernate 5.
 *
 * @author V.Ladynev
 */
public class Hibernate5NamingStrategy extends ImplicitNamingStrategyJpaCompliantImpl {

    private static final long serialVersionUID = 3482010804082494311L;

    private final HibernateNamingStrategy strategy = new HibernateNamingStrategy();

    // private final Set<String> joinTableNames = new HashSet<String>();

    public void setTablePrefix(final String tablePrefix) {
        strategy.setTablePrefix(tablePrefix);
    }

    @Override
    protected String transformEntityName(EntityNaming entityNaming) {
        return strategy.classToTableName(entityNaming.getEntityName());
    }

    @Override
    public Identifier determineBasicColumnName(ImplicitBasicColumnNameSource source) {
        String result = source.isCollectionElement() ? "elt" : strategy
                .propertyToColumnName(transformAttributePath(source.getAttributePath()));
        return toIdentifier(result, source.getBuildingContext());
    }

    @Override
    public Identifier determineJoinColumnName(ImplicitJoinColumnNameSource source) {
        String propertyName = source.getEntityNaming().getEntityName();
        String propertyTableName = getText(source.getReferencedTableName());
        return toIdentifier(strategy.foreignKeyColumnName(propertyName, propertyTableName),
                source.getBuildingContext());
    }

    @Override
    public Identifier determineJoinTableName(ImplicitJoinTableNameSource source) {
        String ownerEntityTable = NamingStrategyUtils.unqualify(source.getOwningEntityNaming()
                .getEntityName());
        String associatedEntityTable = NamingStrategyUtils.unqualify(source
                .getNonOwningEntityNaming().getEntityName());

        // String tableName = strategy.collectionTableName(ownerEntityTable, associatedEntityTable);
        /*
                String result = joinTableNames.contains(tableName) ? strategy.collectionTableName(
                        ownerEntityTable, associatedEntityTable,
                        transformAttributePath(source.getAssociationOwningAttributePath())) : tableName;
         */
        String result = strategy.collectionTableName(ownerEntityTable, associatedEntityTable,
                transformAttributePath(source.getAssociationOwningAttributePath()));

        // joinTableNames.add(result);
        /*
                System.out.println(String.format("owner table name %s association %s result %s",
                        ownerEntityTable, source.getAssociationOwningAttributePath(), result));
         */
        return toIdentifier(result, source.getBuildingContext());
    }

    /*

    @Override
    public Identifier determineCollectionTableName(ImplicitCollectionTableNameSource source) {
        System.out.println("determineCollectionTableName");
        return super.determineCollectionTableName(source);
    }

    @Override
    public Identifier determineIdentifierColumnName(ImplicitIdentifierColumnNameSource source) {
        System.out.println("determineIdentifierColumnName");
        return super.determineIdentifierColumnName(source);
    }

    @Override
    public Identifier determineDiscriminatorColumnName(ImplicitDiscriminatorColumnNameSource source) {
        System.out.println("determineDiscriminatorColumnName");
        return super.determineDiscriminatorColumnName(source);
    }

    @Override
    public Identifier determineTenantIdColumnName(ImplicitTenantIdColumnNameSource source) {
        System.out.println("determineTenantIdColumnName");
        return super.determineTenantIdColumnName(source);
    }

    @Override
    public Identifier determinePrimaryKeyJoinColumnName(
            ImplicitPrimaryKeyJoinColumnNameSource source) {
        System.out.println("determinePrimaryKeyJoinColumnName");
        return super.determinePrimaryKeyJoinColumnName(source);
    }

    @Override
    public Identifier determineAnyDiscriminatorColumnName(
            ImplicitAnyDiscriminatorColumnNameSource source) {
        System.out.println("determineAnyDiscriminatorColumnName");
        return super.determineAnyDiscriminatorColumnName(source);
    }

    @Override
    public Identifier determineAnyKeyColumnName(ImplicitAnyKeyColumnNameSource source) {
        System.out.println("determineAnyKeyColumnName");
        return super.determineAnyKeyColumnName(source);
    }
     */

    private static String getText(Identifier identifier) {
        return identifier == null ? null : identifier.getText();
    }

}
