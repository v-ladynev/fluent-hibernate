package com.github.fluent.hibernate.strategy;

import static com.github.fluent.hibernate.internal.util.InternalUtils.equal;

import java.util.Map;

import org.hibernate.boot.model.naming.ImplicitJoinTableNameSource;

import com.github.fluent.hibernate.internal.util.InternalUtils;

/**
 * Hibernate ask for generating a table's name two times.
 *
 * @author V.Ladynev
 * @version $Id$
 */
class JoinTableNames {

    private final Map<String, TableDescription> names = InternalUtils.CollectionUtils.newHashMap();

    public boolean hasSameNameForOtherProperty(String tableName, ImplicitJoinTableNameSource source) {
        TableDescription description = names.get(tableName);
        return description != null && !description.isSame(source);
    }

    public void put(String tableName, ImplicitJoinTableNameSource source) {
        names.put(tableName, new TableDescription(source));
    }

    private static class TableDescription {

        private final String owningTable;

        private final String otherTable;

        private final String owningTableProperty;

        public TableDescription(ImplicitJoinTableNameSource source) {
            owningTable = source.getOwningEntityNaming().getEntityName();
            otherTable = source.getNonOwningEntityNaming().getEntityName();
            owningTableProperty = source.getAssociationOwningAttributePath().getProperty();
        }

        public boolean isSame(ImplicitJoinTableNameSource source) {
            return equal(owningTable, source.getOwningEntityNaming().getEntityName())
                    && equal(otherTable, source.getNonOwningEntityNaming().getEntityName())
                    && equal(owningTableProperty, source.getAssociationOwningAttributePath()
                            .getProperty());
        }

    }

}
