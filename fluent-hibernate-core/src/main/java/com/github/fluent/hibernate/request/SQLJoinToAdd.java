package com.github.fluent.hibernate.request;

import org.hibernate.SQLQuery;

/**
 * A join for a native SQL.
 *
 * @author V.Ladynev
 */
/* package */final class SQLJoinToAdd implements IToAddToSQLQuery {

    private String tableAlias;

    private String ownerTableAlias;

    private String joinPropertyName;

    public SQLJoinToAdd(String tableAlias, String ownerTableAlias, String joinPropertyName) {
        this.tableAlias = tableAlias;
        this.ownerTableAlias = ownerTableAlias;
        this.joinPropertyName = joinPropertyName;
    }

    @Override
    public void addToQuery(SQLQuery query) {
        if (joinPropertyName == null) {
            query.addJoin(tableAlias, ownerTableAlias);
        } else {
            query.addJoin(tableAlias, ownerTableAlias, joinPropertyName);
        }
    }

}
