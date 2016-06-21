package com.github.fluent.hibernate.cfg.strategy.hibernate5.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
import org.hibernate.boot.model.naming.NamingHelper;
import org.hibernate.boot.model.source.spi.AttributePath;
import org.hibernate.boot.spi.MetadataBuildingContext;
import org.hibernate.cfg.Ejb3DiscriminatorColumn;
import org.hibernate.cfg.NamingStrategy;

import com.github.fluent.hibernate.cfg.strategy.NamingStrategyUtils;
import com.github.fluent.hibernate.internal.util.InternalUtils.CollectionUtils;

/**
 * Adapter to adapt Hibernate 4 naming strategies to Hibernate 5.
 *
 * @author V.Ladynev
 */
public class Hibernate4To5NamingStrategyAdapter implements ImplicitNamingStrategy {

    private static final String ORDER_COLUMN_POSTFIX = "_ORDER";

    private static final String KEY_COLUMN_POSTFIX = "_KEY";

    private static final String FOREIGN_KEY_CONSTRAINT_PREFIX = "FK_";

    private static final String UNIQUE_CONSTRAINT_PREFIX = "UK_";

    private static final String INDEX_PREFIX = "UK_";

    private NamingStrategy hibernate4Strategy;

    public Hibernate4To5NamingStrategyAdapter(NamingStrategy hibernate4Strategy) {
        this.hibernate4Strategy = hibernate4Strategy;
    }

    @Override
    public Identifier determinePrimaryTableName(ImplicitEntityNameSource source) {
        String result = hibernate4Strategy
                .classToTableName(source.getEntityNaming().getEntityName());
        return toIdentifier(result, source.getBuildingContext());
    }

    @Override
    public Identifier determineJoinTableName(ImplicitJoinTableNameSource source) {
        String ownerEntity = source.getOwningEntityNaming().getEntityName();
        String ownerEntityTable = NamingStrategyUtils.unqualifyEntityName(ownerEntity);
        String associatedEntity = source.getNonOwningEntityNaming().getEntityName();
        String associatedEntityTable = NamingStrategyUtils.unqualifyEntityName(associatedEntity);
        String propertyName = getPropertyName(source.getAssociationOwningAttributePath());

        String result = hibernate4Strategy.collectionTableName(ownerEntity, ownerEntityTable,
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

        String result = hibernate4Strategy.collectionTableName(ownerEntity, ownerEntityTable,
                associatedEntity, associatedEntityTable, propertyName);
        return toIdentifier(result, source.getBuildingContext());
    }

    @Override
    public Identifier determineBasicColumnName(ImplicitBasicColumnNameSource source) {
        AttributePath attributePath = source.getAttributePath();
        String propertyName = getPropertyName(attributePath);
        String result = hibernate4Strategy.propertyToColumnName(propertyName);
        return toIdentifier(result, source.getBuildingContext());
    }

    @Override
    public Identifier determineJoinColumnName(ImplicitJoinColumnNameSource source) {
        String propertyName = getPropertyName(source.getAttributePath());
        String propertyEntityName = source.getEntityNaming().getEntityName();
        String propertyTableName = NamingStrategyUtils.unqualifyEntityName(propertyEntityName);
        String referencedColumnName = source.getReferencedColumnName().getText();

        String result = hibernate4Strategy.foreignKeyColumnName(propertyName, propertyEntityName,
                propertyTableName, referencedColumnName);
        return toIdentifier(result, source.getBuildingContext());
    }

    /**
     * Generates a name for a foreign key constraint.
     */
    @Override
    public Identifier determineForeignKeyName(ImplicitForeignKeyNameSource source) {
        String result = generateHashedConstraintName(FOREIGN_KEY_CONSTRAINT_PREFIX,
                source.getTableName(), source.getColumnNames());
        return toIdentifier(result, source.getBuildingContext());
    }

    /**
     * Generates a name for a unique constraint.
     */
    @Override
    public Identifier determineUniqueKeyName(ImplicitUniqueKeyNameSource source) {
        String result = generateHashedConstraintName(UNIQUE_CONSTRAINT_PREFIX,
                source.getTableName(), source.getColumnNames());
        return toIdentifier(result, source.getBuildingContext());
    }

    /**
     * Generates a name for an index.
     */
    @Override
    public Identifier determineIndexName(ImplicitIndexNameSource source) {
        String result = generateHashedConstraintName(INDEX_PREFIX, source.getTableName(),
                source.getColumnNames());
        return toIdentifier(result, source.getBuildingContext());
    }

    /**
     * Generates a name for @DiscriminatorColumn. Hibernate doesn't use this method because of an
     * issue. Hiibernate generates "DTYPE" name for the descriminator column. Hibernate 4 uses
     * ImprovedNamingStrategy#columnName() to convert "DTYPE" to "dtype".
     */
    @Override
    public Identifier determineDiscriminatorColumnName(
            ImplicitDiscriminatorColumnNameSource source) {
        String discriminatorColumnName = Ejb3DiscriminatorColumn.DEFAULT_DISCRIMINATOR_COLUMN_NAME;
        String result = hibernate4Strategy.columnName(discriminatorColumnName);
        return toIdentifier(result, source.getBuildingContext());
    }

    /**
     * Generates a name for @OrderColumn. Hibernate doesn't use this method because of an issue.
     * Hibernate 5 generates "booksOrdered_ORDER", opposite "books_ordered_order" is generated by
     * Hibernate 4 ImprovedNamingStrategy.
     */
    @Override
    public Identifier determineListIndexColumnName(ImplicitIndexColumnNameSource source) {
        String propertyName = getPropertyName(source.getPluralAttributePath());
        String orderColumnName = propertyName + ORDER_COLUMN_POSTFIX;
        String result = hibernate4Strategy.columnName(orderColumnName);
        return toIdentifier(result, source.getBuildingContext());
    }

    /**
     * This method is not used by Hibernate for @MapKeyColumn because of an issue. Hibernate 5
     * generates "booksMap_KEY", opposite "books_map_key" is generated by Hibernate 4
     * ImprovedNamingStrategy.
     */
    @Override
    public Identifier determineMapKeyColumnName(ImplicitMapKeyColumnNameSource source) {
        String propertyName = getPropertyName(source.getPluralAttributePath());
        String orderColumnName = propertyName + KEY_COLUMN_POSTFIX;
        String result = hibernate4Strategy.columnName(orderColumnName);
        return toIdentifier(result, source.getBuildingContext());
    }

    // can't test, looks like not used
    @Override
    public Identifier determineTenantIdColumnName(ImplicitTenantIdColumnNameSource source) {
        throw new UnsupportedOperationException();
    }

    // can't test, looks like not used
    @Override
    public Identifier determineIdentifierColumnName(ImplicitIdentifierColumnNameSource source) {
        throw new UnsupportedOperationException();
    }

    // can't test, looks like not used
    @Override
    public Identifier determinePrimaryKeyJoinColumnName(
            ImplicitPrimaryKeyJoinColumnNameSource source) {
        throw new UnsupportedOperationException();
    }

    // not implemented yet
    @Override
    public Identifier determineAnyDiscriminatorColumnName(
            ImplicitAnyDiscriminatorColumnNameSource source) {
        throw new UnsupportedOperationException();
    }

    // not implemented yet
    @Override
    public Identifier determineAnyKeyColumnName(ImplicitAnyKeyColumnNameSource source) {
        throw new UnsupportedOperationException();
    }

    /**
     * Generates a constraint name. This code is from Hibernate 4. It is slightly different from
     * Hibernate 5 NamingHelper code.
     */
    public static String generateHashedConstraintName(String prefix, Identifier tableName,
            List<Identifier> columnNames) {
        StringBuilder sb = new StringBuilder("table`" + tableName.getText() + "`");

        List<String> alphabeticalColumnNames = toString(columnNames);
        Collections.sort(alphabeticalColumnNames);
        for (String columnName : alphabeticalColumnNames) {
            sb.append("column`" + columnName + "`");
        }

        return prefix + NamingHelper.INSTANCE.hashedName(sb.toString());
    }

    private static List<String> toString(List<Identifier> columnNames) {
        ArrayList<String> result = CollectionUtils
                .newArrayListWithCapacity(CollectionUtils.size(columnNames));
        for (Identifier columnName : columnNames) {
            result.add(columnName.getText());
        }

        return result;
    }

    private static String getPropertyName(AttributePath attributePath) {
        return attributePath == null ? null : attributePath.getProperty();
    }

    private static Identifier toIdentifier(String stringForm,
            MetadataBuildingContext buildingContext) {
        // buildingContext can be null during a unit testing
        return buildingContext == null ? Identifier.toIdentifier(stringForm)
                : buildingContext.getMetadataCollector().getDatabase().getJdbcEnvironment()
                        .getIdentifierHelper().toIdentifier(stringForm);
    }

}
