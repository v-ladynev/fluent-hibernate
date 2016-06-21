package com.github.fluent.hibernate.cfg.strategy.hibernate5.adapter;

import org.hibernate.cfg.ImprovedNamingStrategy;

/**
 * Implementation of Hibernate 4 ImprovedNamingStrategy for Hibernate 5.
 *
 * @author V.Ladynev
 */
public class ImprovedNamingStrategyHibernate5 extends Hibernate4To5NamingStrategyAdapter {

    public ImprovedNamingStrategyHibernate5() {
        super(ImprovedNamingStrategy.INSTANCE);
    }

}
