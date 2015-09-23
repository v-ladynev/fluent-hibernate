package com.github.fluent.hibernate;

import org.hibernate.Criteria;

/**
 * Pagination parameters.
 *
 * @author V.Ladynev
 */
public class Pagination {

    public static final Pagination EMPTY = new Pagination(0, 0);

    /** Page index, begins from 0. */
    private final int pageIndex;

    /** Objects on page count. */
    private final int pageSize;

    public Pagination(int pageIndex, int pageSize) {
        this.pageIndex = pageIndex;
        this.pageSize = pageSize;
    }

    public void addToCriteria(final Criteria criteria) {
        if (this == EMPTY) {
            return;
        }
        criteria.setMaxResults(pageSize);
        criteria.setFirstResult(pageIndex * pageSize);
    }

}
