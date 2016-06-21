package com.github.fluent.hibernate.cfg.strategy.hibernate5.adapter.persistent;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public class Customer {

    @Id
    private Long pid;

}
