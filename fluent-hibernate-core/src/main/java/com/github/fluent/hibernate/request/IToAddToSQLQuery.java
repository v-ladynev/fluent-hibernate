package com.github.fluent.hibernate.request;

import org.hibernate.SQLQuery;

interface IToAddToSQLQuery {

    void addToQuery(SQLQuery query);

}
