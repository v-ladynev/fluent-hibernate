package com.github.fluent.hibernate.cfg;

import org.hibernate.Session;
import org.hibernate.StatelessSession;

/**
 *
 * @author V.Ladynev
 */
class SessionControlHibernate5 implements ISessionControl {

    @Override
    public void close(Session session) {
        if (session == null || !session.isOpen()) {
            return;
        }

        session.close();
    }

    @Override
    public void close(StatelessSession session) {
        if (session == null) {
            return;
        }

        session.close();
    }

}
