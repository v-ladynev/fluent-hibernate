package com.github.fluent.hibernate;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.criteria.JoinType;

import org.hibernate.Criteria;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;

/**
 * Aliases for SQL request.
 *
 * @author V.Ladynev
 */
/* package */final class Aliases {

    private final Set<Alias> aliases = new HashSet<Alias>();

    private Aliases() {

    }

    /**
     * Add alias.
     *
     * @param associationPath
     *            original full name of field in request
     * @param alias
     *            alias for {@code associationPath}
     * @param joinType
     *            SQL request join type
     * @see javax.persistence.criteria.JoinType
     */
    public Aliases add(String associationPath, String alias, JoinType joinType) {
        add(associationPath, alias, joinType, null);
        return this;
    }

    public Aliases add(String associationPath, String alias, JoinType joinType, Criterion withClause) {
        aliases.add(new Alias(associationPath, alias, joinType, withClause));
        return this;
    }

    public void addToCriteria(Criteria criteria) {
        for (Alias alias : aliases) {
            alias.addToCriteria(criteria);
        }
    }

    public void addToCriteria(DetachedCriteria criteria) {
        for (Alias alias : aliases) {
            alias.addToCriteria(criteria);
        }
    }

    public static Aliases aliasList() {
        return new Aliases();
    }

}
