package com.github.fluent.hibernate.request;

import org.hibernate.SQLQuery;

/**
 * An entity for a native SQL.
 *
 * @author V.Ladynev
 */
/* package */final class SQLEntityToAdd implements IToAddToSQLQuery {

    private String tableAlias;

    private Class<?> entityType;

    public SQLEntityToAdd(String tableAlias, Class<?> entityType) {
        this.tableAlias = tableAlias;
        this.entityType = entityType;
    }

    @Override
    public void addToQuery(SQLQuery query) {
        if (tableAlias == null) {
            query.addEntity(entityType);
        } else {
            query.addEntity(tableAlias, entityType);
        }
    }

}
