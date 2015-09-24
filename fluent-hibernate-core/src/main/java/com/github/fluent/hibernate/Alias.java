package com.github.fluent.hibernate;

import javax.persistence.criteria.JoinType;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;

import com.github.fluent.hibernate.util.InternalUtils;

/**
 * Field alias for SQL request.
 *
 * @author V.Ladynev
 */
public final class Alias {

    private final String associationPath;

    private final String alias;

    private final JoinType joinType;

    private Criterion withClause;

    public Alias(final String associationPath, final String alias, final JoinType joinType) {
        this.associationPath = associationPath;
        this.alias = alias;
        this.joinType = joinType;
    }

    public Alias(final String associationPath, final String alias, final JoinType joinType,
            Criterion withClause) {
        this.associationPath = associationPath;
        this.alias = alias;
        this.joinType = joinType;
        this.withClause = withClause;
    }

    public void addToCriteria(final Criteria criteria) {
        if (withClause == null) {
            criteria.createAlias(associationPath, alias, joinType.ordinal());
        } else {
            criteria.createAlias(associationPath, alias, joinType.ordinal(), withClause);
        }
    }

    public void addToCriteria(final DetachedCriteria criteria) {
        if (joinType == null) {
            criteria.createAlias(associationPath, alias);
        } else if (withClause == null) {
            criteria.createAlias(associationPath, alias, joinType.ordinal());
        } else {
            criteria.createAlias(associationPath, alias, joinType.ordinal(), withClause);
        }
    }

    @Override
    public int hashCode() {
        return InternalUtils.hashCode(associationPath, alias, joinType);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        Alias other = (Alias) obj;

        return InternalUtils.equal(associationPath, other.associationPath)
                && InternalUtils.equal(alias, other.alias)
                && InternalUtils.equal(joinType, other.joinType)
                && InternalUtils.equal(withClause, other.withClause);
    }

}
