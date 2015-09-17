package com.github.fluent.hibernate;

import org.hibernate.StatelessSession;

/**
 *
 * @author V.Ladynev
 */
public interface IStatelessRequest<T> {

    T doInTransaction(StatelessSession session);

}
