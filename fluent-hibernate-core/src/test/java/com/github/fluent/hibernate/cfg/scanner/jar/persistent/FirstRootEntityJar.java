package com.github.fluent.hibernate.cfg.scanner.jar.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author V.Ladynev
 */
@Entity
@Table
public class FirstRootEntityJar {

    @Entity
    @Table
    public static class NestedEntityJar {

    }

}
