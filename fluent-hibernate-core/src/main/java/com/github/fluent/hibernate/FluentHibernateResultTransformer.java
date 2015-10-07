package com.github.fluent.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.transform.BasicTransformerAdapter;

import com.github.fluent.hibernate.BasicIgnoreCasePropertyAccessor.BasicSetter;

/**
 * @author DoubleF1re
 * @author V.Ladynev
 */
public class FluentHibernateResultTransformer extends BasicTransformerAdapter {

    private static final long serialVersionUID = 6825154815776629666L;

    private final Class<?> resultClass;

    private BasicSetter[] setters;

    private final BasicIgnoreCasePropertyAccessor propertyAccessor = new BasicIgnoreCasePropertyAccessor();

    public FluentHibernateResultTransformer(Class<?> resultClass) {
        this.resultClass = resultClass;
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        if (setters == null) {
            setters = new BasicSetter[aliases.length];
            for (int i = 0; i < aliases.length; i++) {
                String alias = aliases[i];
                if (alias != null) {
                    setters[i] = propertyAccessor.getSetter(resultClass, alias);
                }
            }
        }
        Object result = createResult();

        for (int i = 0; i < aliases.length; i++) {
            if (setters[i] != null) {
                setters[i].set(result, tuple[i], null);
            }
        }

        return result;
    }

    private Object createResult() {
        try {
            return resultClass.newInstance();
        } catch (Exception ex) {
            throw new HibernateException(String.format("Could not instantiate result class: %s",
                    resultClass.getName()), ex);
        }
    }

}
