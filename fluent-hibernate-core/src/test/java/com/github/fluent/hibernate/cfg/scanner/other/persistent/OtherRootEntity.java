package com.github.fluent.hibernate.cfg.scanner.other.persistent;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 *
 * @author V.Ladynev
 */
@Entity
@Table
public class OtherRootEntity {

    @Entity
    @Table
    public static class OtherNestedEntity {

    }

}
