package com.github.fluent.hibernate;

import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.transform.ResultTransformer;

import com.github.fluent.hibernate.BasicIgnoreCasePropertyAccessor.BasicSetter;

/**
 * @author DoubleF1re
 * @author V.Ladynev
 */
public class FluentHibernateResultTransformer implements ResultTransformer {

    private static final long serialVersionUID = 6825154815776629666L;

    private static final String COULD_NOT_INSTANTIATE = "Could not instantiate resultclass: %s";

    @SuppressWarnings("rawtypes")
    private final Class resultClass;

    private BasicSetter[] setters;

    private final BasicIgnoreCasePropertyAccessor propertyAccessor;

    private final String[] instAliases;

    @SuppressWarnings("rawtypes")
    public FluentHibernateResultTransformer(Class resultClass) {
        this(resultClass, null);
    }

    /**
     *
     * @param resultClass
     *            : lCreated bean target class.
     * @param aliases
     *            : allows to override default aliases.
     */
    @SuppressWarnings("rawtypes")
    public FluentHibernateResultTransformer(Class resultClass, String[] aliases) {
        if (resultClass == null) {
            throw new IllegalArgumentException("resultClass cannot be null");
        }
        this.resultClass = resultClass;
        propertyAccessor = new BasicIgnoreCasePropertyAccessor();
        instAliases = aliases;
    }

    /**
     * @see org.hibernate.transform.ResultTransformer#transformTuple(java.lang.Object[],
     *      java.lang.String[])
     */
    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        Object result;

        if (instAliases != null) {
            aliases = instAliases;
        }

        try {
            if (setters == null) {
                setters = new BasicSetter[aliases.length];
                for (int i = 0; i < aliases.length; i++) {
                    String alias = aliases[i];
                    if (alias != null) {
                        setters[i] = propertyAccessor.getSetter(resultClass, alias);
                    }
                }
            }
            result = resultClass.newInstance();

            for (int i = 0; i < aliases.length; i++) {
                if (setters[i] != null) {
                    setters[i].set(result, tuple[i], null);
                }
            }
        } catch (InstantiationException e) {
            throw new HibernateException(
                    String.format(COULD_NOT_INSTANTIATE, resultClass.getName()), e);
        } catch (IllegalAccessException e) {
            throw new HibernateException(
                    String.format(COULD_NOT_INSTANTIATE, resultClass.getName()), e);
        }

        return result;
    }

    /**
     * @see org.hibernate.transform.ResultTransformer#transformList(java.util.List)
     */
    @Override
    @SuppressWarnings("rawtypes")
    public List transformList(List collection) {
        return collection;
    }

}
