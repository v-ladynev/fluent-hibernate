package com.github.fluent.hibernate;

import org.hibernate.Session;

/**
 *
 * @author V.Ladynev
 */
public interface IRequest<T> {

    T doInTransaction(Session session);

}
