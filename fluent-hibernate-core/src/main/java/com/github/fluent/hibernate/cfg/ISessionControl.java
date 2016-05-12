package com.github.fluent.hibernate.cfg;

import org.hibernate.Session;
import org.hibernate.StatelessSession;

/**
 *
 * @author V.Ladynev
 */
interface ISessionControl {

    void close(Session session);

    void close(StatelessSession session);

}
