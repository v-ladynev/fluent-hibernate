package com.github.fluent.hibernate.example.mysql;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.HibernateException;
import org.hibernate.criterion.CriteriaQuery;
import org.hibernate.criterion.SimpleProjection;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.type.StringType;
import org.hibernate.type.Type;

/**
 *
 * @author V.Ladynev
 */
public class ConcatProjection extends SimpleProjection {

    private static final String CONCAT_FUNCTION_NAME = "concat";

    private final String[] properties;

    public ConcatProjection(String... properties) {
        this.properties = properties;
    }

    @Override
    public String toSqlString(Criteria criteria, int loc, CriteriaQuery criteriaQuery)
            throws HibernateException {
        String result = getFunction(criteriaQuery).render(StringType.INSTANCE,
                propertiesToColumns(criteria, criteriaQuery), criteriaQuery.getFactory());
        return result + " as y" + loc + '_';
    }

    private List<String> propertiesToColumns(Criteria criteria, CriteriaQuery criteriaQuery) {
        List<String> result = new ArrayList<String>(properties.length);

        for (String property : properties) {
            result.add(criteriaQuery.getColumn(criteria, property));
        }

        return result;
    }

    private SQLFunction getFunction(CriteriaQuery criteriaQuery) {
        return criteriaQuery.getFactory().getSqlFunctionRegistry()
                .findSQLFunction(CONCAT_FUNCTION_NAME);
    }

    @Override
    public Type[] getTypes(Criteria criteria, CriteriaQuery criteriaQuery)
            throws HibernateException {
        return new Type[] { StringType.INSTANCE };
    }

}
