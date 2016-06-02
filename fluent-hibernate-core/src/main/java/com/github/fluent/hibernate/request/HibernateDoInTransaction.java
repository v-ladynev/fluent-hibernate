package com.github.fluent.hibernate.request;

import com.github.fluent.hibernate.IRequest;
import com.github.fluent.hibernate.cfg.HibernateSessionFactory;

/**
 *
 * @author V.Ladynev
 */
public class HibernateDoInTransaction {

    public static <T> T execute(IRequest<T> request) {
        return HibernateSessionFactory.doInTransaction(request);
    }

}
