package com.github.fluent.hibernate.cfg.strategy;

import static com.github.fluent.hibernate.internal.util.InternalUtils.equal;

import java.util.Map;

import com.github.fluent.hibernate.internal.util.InternalUtils;

/**
 *
 * @author V.Ladynev
 * @version $Id$
 */
public final class JoinTableNames {

    private final Map<String, TableDescription> names = InternalUtils.CollectionUtils.newHashMap();

    public boolean hasSameNameForOtherProperty(String tableName, TableDescription descriptionToCheck) {
        TableDescription description = names.get(tableName);
        return description != null && !description.isSame(descriptionToCheck);
    }

    public void put(String tableName, TableDescription description) {
        names.put(tableName, description);
    }

    public static final class TableDescription {

        private final String owningTable;

        private final String otherTable;

        private final String owningTableProperty;

        public TableDescription(String owningTable, String otherTable, String owningTableProperty) {
            this.owningTable = owningTable;
            this.otherTable = otherTable;
            this.owningTableProperty = owningTableProperty;
        }

        public boolean isSame(TableDescription description) {
            return equal(owningTable, description.owningTable)
                    && equal(otherTable, description.otherTable)
                    && equal(owningTableProperty, description.owningTableProperty);
        }

    }

}
