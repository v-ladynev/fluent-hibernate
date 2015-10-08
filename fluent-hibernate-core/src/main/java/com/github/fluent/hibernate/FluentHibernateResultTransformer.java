package com.github.fluent.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.transform.BasicTransformerAdapter;

import com.github.fluent.hibernate.internal.transformer.Setter;

/**
 * @author DoubleF1re
 * @author V.Ladynev
 */
public class FluentHibernateResultTransformer extends BasicTransformerAdapter {

    private static final long serialVersionUID = 6825154815776629666L;

    private final Class<?> resultClass;

    private Setter[] setters;

    private final PropertyAccessor propertyAccessor = new PropertyAccessor();

    public FluentHibernateResultTransformer(Class<?> resultClass) {
        this.resultClass = resultClass;
    }

    @Override
    public Object transformTuple(Object[] tuple, String[] aliases) {
        if (setters == null) {
            setters = new Setter[aliases.length];
            for (int i = 0; i < aliases.length; i++) {
                String alias = aliases[i];
                setters[i] = propertyAccessor.getSetter(resultClass, alias);
            }
        }
        Object result = createResult();

        for (int i = 0; i < aliases.length; i++) {
            setters[i].set(result, tuple[i], null);
        }

        return result;
    }

    private Object createResult() {
        try {
            return resultClass.newInstance();
        } catch (Exception ex) {
            throw new HibernateException(
                    String.format("Could not instantiate result class: %s", resultClass.getName()),
                    ex);
        }
    }

}
