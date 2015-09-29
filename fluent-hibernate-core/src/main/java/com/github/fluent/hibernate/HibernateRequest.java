package com.github.fluent.hibernate;

import com.github.fluent.hibernate.util.InternalUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.transform.ResultTransformer;

import javax.persistence.criteria.JoinType;
import java.util.Collection;
import java.util.List;

/**
 * @param <T>
 *            type of return value.
 *
 * @author DoubleF1re
 * @author V.Ladynev
 */
public final class HibernateRequest<T> {

    private final List<Criterion> restrictions = InternalUtils.CollectionUtils.newArrayList();

    private final ProjectionList projections = Projections.projectionList();

    private final Aliases aliases = Aliases.aliasList();

    private final List<Order> orders = InternalUtils.CollectionUtils.newArrayList();

    private String[] fetchJoinPaths;

    private boolean distinct;

    private ResultTransformer transformer;

    private final Class<?> persistentClass;

    private Pagination pagination;

    private Integer maxResults;

    private HibernateRequest(Class<?> persistentClass) {
        this.persistentClass = persistentClass;
    }

    public HibernateRequest<T> idEq(Object value) {
        restrictions.add(Restrictions.idEq(value));
        return this;
    }

    public HibernateRequest<T> eqOrIsNull(String propertyName, Object value) {
        restrictions.add(Restrictions.eqOrIsNull(propertyName, value));
        return this;
    }

    public HibernateRequest<T> eq(String propertyName, Object value) {
        restrictions.add(Restrictions.eq(propertyName, value));
        return this;
    }

    public HibernateRequest<T> ne(String propertyName, Object value) {
        restrictions.add(Restrictions.ne(propertyName, value));
        return this;
    }

    public HibernateRequest<T> ge(String propertyName, Object value) {
        restrictions.add(Restrictions.ge(propertyName, value));
        return this;
    }

    public HibernateRequest<T> gt(String propertyName, Object value) {
        restrictions.add(Restrictions.gt(propertyName, value));
        return this;
    }

    public HibernateRequest<T> lt(String propertyName, Object value) {
        restrictions.add(Restrictions.lt(propertyName, value));
        return this;
    }

    public HibernateRequest<T> le(String propertyName, Object value) {
        restrictions.add(Restrictions.le(propertyName, value));
        return this;
    }

    public HibernateRequest<T> isNull(String propertyName) {
        restrictions.add(Restrictions.isNull(propertyName));
        return this;
    }

    public HibernateRequest<T> isNotNull(String propertyName) {
        restrictions.add(Restrictions.isNotNull(propertyName));
        return this;
    }

    public HibernateRequest<T> in(String propertyName, Collection<?> values) {
        if (InternalUtils.CollectionUtils.isNotEmpty(values)) {
            restrictions.add(Restrictions.in(propertyName, values));
        }
        return this;
    }

    public HibernateRequest<T> inNothingForEmptyCollection(String propertyName, Collection<?> values) {
        if (InternalUtils.CollectionUtils.isEmpty(values)) {
            addFalse();
        } else {
            in(propertyName, values);
        }
        return this;
    }

    public HibernateRequest<T> addFalse() {
        restrictions.add(Restrictions.sqlRestriction("1<>1"));
        return this;
    }

    // TODO It works incorrectly for an only element array
    public HibernateRequest<T> in(String propertyName, Object... values) {
        restrictions.add(Restrictions.in(propertyName, values));
        return this;
    }

    public HibernateRequest<T> crit(Criterion criterion) {
        restrictions.add(criterion);
        return this;
    }

    public HibernateRequest<T> proj(String propertyName) {
        proj(Projections.property(propertyName).as(propertyName));
        return this;
    }

    public HibernateRequest<T> proj(String propertyName, String alias) {
        proj(Projections.property(propertyName).as(alias));
        return this;
    }

    // TODO pidProperty automatic detection name (may be impossible)
    public HibernateRequest<T> projId(String pidProperty) {
        proj(Projections.id().as(pidProperty));
        return this;
    }

    public HibernateRequest<T> projMin(String minProperty) {
        proj(Projections.min(minProperty));
        return this;
    }

    public HibernateRequest<T> projMax(String maxProperty) {
        proj(Projections.max(maxProperty));
        return this;
    }

    public HibernateRequest<T> proj(Projection projection) {
        projections.add(projection);
        return this;
    }

    public HibernateRequest<T> distinct() {
        distinct = true;
        return this;
    }

    public HibernateRequest<T> innerJoin(String associationPath) {
        innerJoin(associationPath, associationPath);
        return this;
    }

    public HibernateRequest<T> innerJoin(String associationPath, String alias) {
        aliases.add(associationPath, alias, JoinType.INNER);
        return this;
    }

    public HibernateRequest<T> innerJoin(String associationPath, String alias, Criterion withClause) {
        aliases.add(associationPath, alias, JoinType.INNER, withClause);
        return this;
    }

    public HibernateRequest<T> leftJoin(String associationPath) {
        leftJoin(associationPath, associationPath);
        return this;
    }

    public HibernateRequest<T> leftJoin(String associationPath, String alias) {
        aliases.add(associationPath, alias, JoinType.LEFT);
        return this;
    }

    public HibernateRequest<T> leftJoin(String associationPath, String alias, Criterion withClause) {
        aliases.add(associationPath, alias, JoinType.LEFT, withClause);
        return this;
    }

    public HibernateRequest<T> rightJoin(String associationPath) {
        rightJoin(associationPath, associationPath);
        return this;
    }

    public HibernateRequest<T> rightJoin(String associationPath, String alias) {
        aliases.add(associationPath, alias, JoinType.RIGHT);
        return this;
    }

    public HibernateRequest<T> rightJoin(String associationPath, String alias, Criterion withClause) {
        aliases.add(associationPath, alias, JoinType.RIGHT, withClause);
        return this;
    }

    public HibernateRequest<T> transform(Class<?> clazz) {
        transformer = new FluentHibernateResultTransformer(clazz);
        return this;
    }

    public HibernateRequest<T> distinctToRootEntity() {
        this.transformer = Criteria.DISTINCT_ROOT_ENTITY;
        return this;
    }

    public HibernateRequest<T> pagination(Pagination pagination) {
        this.pagination = pagination;
        return this;
    }

    public HibernateRequest<T> maxResults(int maxResults) {
        this.maxResults = maxResults;
        return this;
    }

    /**
     * Sort from smallest to largest.
     */
    public HibernateRequest<T> orderAsc(String propertyName) {
        orders.add(Order.asc(propertyName));
        return this;
    }

    /**
     * Sort from largest to smallest.
     */
    public HibernateRequest<T> orderDesc(String propertyName) {
        orders.add(Order.desc(propertyName));
        return this;
    }

    // TODO It works incorrectly for an only element array
    public HibernateRequest<T> fetchJoin(String... associationPaths) {
        fetchJoinPaths = associationPaths;
        return this;
    }

    public T first(T defaultValue) {
        T result = first();
        return result == null ? defaultValue : result;
    }

    public T first() {
        return InternalUtils.CollectionUtils.<T> first(list());
    }

    @SuppressWarnings("unchecked")
    public List<T> list() {
        return HibernateSessionFactory.doInTransaction(new IRequest<List<T>>() {
            @Override
            public List<T> doInTransaction(Session session) {
                return tuneCriteriaForList(createCriteria(session)).list();
            }
        });
    }

    // TODO may be return long?
    public int count() {
        Number result = HibernateSessionFactory.doInTransaction(new IRequest<Number>() {
            @Override
            public Number doInTransaction(Session session) {
                return (Number) count(createCriteria(session));
            }
        });
        return result == null ? 0 : result.intValue();
    }

    private Criteria createCriteria(Session session) {
        Criteria result = session.createCriteria(persistentClass);
        aliases.addToCriteria(result);

        for (Criterion restriction : restrictions) {
            result.add(restriction);
        }

        if (fetchJoinPaths != null) {
            for (String associationPath : fetchJoinPaths) {
                result.setFetchMode(associationPath, FetchMode.JOIN);
            }
        }

        return result;
    }

    private Criteria tuneCriteriaForList(Criteria criteria) {
        if (projections.getLength() > 0) {
            criteria.setProjection(distinct ? Projections.distinct(projections) : projections);
        }

        if (transformer != null) {
            criteria.setResultTransformer(transformer);
        }

        for (Order order : orders) {
            criteria.addOrder(order);
        }

        if (maxResults != null) {
            criteria.setMaxResults(maxResults);
        }

        // can replace maxResults
        if (pagination != null) {
            pagination.addToCriteria(criteria);
        }

        return criteria;
    }

    private Object count(Criteria criteria) {
        // TODO for requests with tables joins will be work incorrect
        // select count(*) from (select distinct pid1, pid2 from ...) check this
        criteria.setProjection(Projections.rowCount());
        return criteria.uniqueResult();
    }

    public static <T> HibernateRequest<T> create(Class<?> clazz) {
        return new HibernateRequest<T>(clazz);
    }

}
