package com.github.fluent.hibernate.cfg.strategy.hibernate5.adapter.persistent;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Book {

    @Id
    private Long pid;

}