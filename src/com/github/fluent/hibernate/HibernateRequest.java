package com.github.fluent.hibernate;

import java.util.Collection;
import java.util.List;

import javax.persistence.criteria.JoinType;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.internal.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;

import com.github.fluent.hibernate.util.InternalUtils;

/**
 * @param <T>
 *            тип возвращаемого значения.
 *
 * @author DoubleF1re
 * @author V.Ladynev
 */
public final class HibernateRequest<T> {

    private final List<Criterion> restrictions = InternalUtils.CollectionUtils.newArrayList();

    private final ProjectionList projections = Projections.projectionList();

    private final Aliases aliases = Aliases.aliasList();

    private boolean distinct;

    private ResultTransformer transformer;

    // TODO delete
    // private CriteriaImpl criteria;

    private final Class<?> persistentClass;

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
        restrictions.add(Restrictions.in(propertyName, values));
        return this;
    }

    // TODO может ли это привести к ошибкам?
    // TODO доработать если один элемент и он массив, чтобы работало правильно
    public HibernateRequest<T> in(String propertyName, Object... values) {
        restrictions.add(Restrictions.in(propertyName, values));
        return this;
    }

    public HibernateRequest<T> proj(String propertyName) {
        projections.add(Projections.property(propertyName).as(propertyName));
        return this;
    }

    public HibernateRequest<T> proj(String propertyName, String alias) {
        projections.add(Projections.property(propertyName).as(alias));
        return this;
    }

    // TODO возможно, pidProperty можно определеить автоматически
    public HibernateRequest<T> projId(String pidProperty) {
        projections.add(Projections.id().as(pidProperty));
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
        criteria.createAlias(associationPath, alias, JoinType.LEFT.ordinal(), withClause);
        return this;
    }

    public HibernateRequest<T> rightJoin(String associationPath) {
        rightJoin(associationPath, associationPath);
        return this;
    }

    public HibernateRequest<T> rightJoin(String associationPath, String alias) {
        criteria.createAlias(associationPath, alias, JoinType.RIGHT.ordinal());
        return this;
    }

    public HibernateRequest<T> rightJoin(String associationPath, String alias, Criterion withClause) {
        criteria.createAlias(associationPath, alias, JoinType.RIGHT.ordinal(), withClause);
        return this;
    }

    public HibernateRequest<T> transform(Class<?> clazz) {
        /* Напрямую трансформер устанавливать нельзя, он теряется при вызове criteria.setProjection.
         * так нельзя - criteria.setResultTransformer(new IgnoreCaseAliasToBeanResultTransformer(clazz))
         */
        transformer = new IgnoreCaseAliasToBeanResultTransformer(clazz);
        return this;
    }

    public HibernateRequest<T> crit(Criterion criterion) {
        criteria.add(criterion);
        return this;
    }

    public HibernateRequest<T> pagination(Pagination pagination) {
        pagination.addToCriteria(criteria);
        return this;
    }

    public HibernateRequest<T> maxResults(int maxResults) {
        criteria.setMaxResults(maxResults);
        return this;
    }

    /**
     * Сортировка от меньшего к большему.
     */
    public HibernateRequest<T> orderAsc(String propertyName) {
        criteria.addOrder(Order.asc(propertyName));
        return this;
    }

    /**
     * Сортировка от большего к меньшему.
     */
    public HibernateRequest<T> orderDesc(String propertyName) {
        criteria.addOrder(Order.desc(propertyName));
        return this;
    }

    // TODO возможно имя метода fetchJoin?
    // TODO доработать если один элемент и он массив, чтобы работало правильно
    public HibernateRequest<T> fetch(String... associationPaths) {
        for (String associationPath : associationPaths) {
            criteria.setFetchMode(associationPath, FetchMode.JOIN);
        }
        return this;
    }

    public T first(T defaultValue) {
        T result = first();
        return result == null ? defaultValue : result;
    }

    public T first() {
        return InternalUtils.CollectionUtils.<T> first(list());
    }

    public List<T> list() {
        return HibernateSessionFactory.doInTransaction(new IRequest<List<T>>() {
            @Override
            public List<T> doInTransaction(Session session) {
                session.createCriteria(persistentClass)

                return list(attachSession(session));
            }
        });
    }

    // TODO возможно здесь возвращать long?
    public int count() {
        Number result = HibernateSessionFactory.doInTransaction(new IRequest<Number>() {
            @Override
            public Number doInTransaction(Session session) {
                return (Number) count(attachSession(session));
            }
        });
        return result == null ? 0 : result.intValue();
    }

    private CriteriaImpl attachSession(Session session) {
        criteria.setSession((SessionImplementor) session);
        return criteria;
    }

    @SuppressWarnings("unchecked")
    private List<T> list(CriteriaImpl crit) {
        if (projections.getLength() > 0) {
            crit.setProjection(distinct ? Projections.distinct(projections) : projections);
        }
        if (transformer != null) {
            crit.setResultTransformer(transformer);
        }
        List<T> result = crit.list();

        return result;
    }

    private Object count(Criteria crit) {
        // TODO для запросов, в которых осуществляется join таблиц, будет работать некорректно
        // select count(*) from (select distinct pid1, pid2 from ...) попробовать
        crit.setProjection(Projections.rowCount());
        return crit.uniqueResult();
    }

    public static <T> HibernateRequest<T> create(Class<?> clazz) {
        return new HibernateRequest<T>(clazz);
    }

    public static <T> HibernateRequest<T> create(CriteriaImpl criteria) {
        return new HibernateRequest<T>(criteria);
    }

}
