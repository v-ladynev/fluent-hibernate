package com.github.fluent.hibernate.cfg;

import java.lang.reflect.Method;

import org.hibernate.Session;
import org.hibernate.StatelessSession;

import com.github.fluent.hibernate.internal.util.InternalUtils;
import com.github.fluent.hibernate.internal.util.reflection.ReflectionUtils;

/**
 *
 * @author V.Ladynev
 */
class SessionControlHibernate4 implements ISessionControl {

    private static Method sessionClose = getCloseMethod(Session.class);

    private static Method statelessSessionClose = getCloseMethod(StatelessSession.class);

    @Override
    public void close(Session session) {
        if (session == null || !session.isOpen()) {
            return;
        }

        try {
            ReflectionUtils.invoke(session, sessionClose);
        } catch (Exception ex) {
            throw InternalUtils.toRuntimeException("Can't close Session using a reflection.", ex);
        }
    }

    @Override
    public void close(StatelessSession session) {
        if (session == null) {
            return;
        }

        try {
            ReflectionUtils.invoke(session, statelessSessionClose);
        } catch (Exception ex) {
            throw InternalUtils
                    .toRuntimeException("Can't close StatelessSession using a reflection.", ex);
        }

    }

    private static Method getCloseMethod(Class<?> classFrom) {
        try {
            return ReflectionUtils.extractMethod(classFrom, "close");
        } catch (Exception ex) {
            throw InternalUtils.toRuntimeException(String.format(
                    "Can't get %s close() method by a reflection.", classFrom.getSimpleName()), ex);
        }
    }

}
