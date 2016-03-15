package com.github.fluent.hibernate.cfg.scanner.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author V.Ladynev
 */
@Entity
@Table
public class FirstRootEntity {

    @Entity
    @Table
    public static class NestedEntity {

    }

}
