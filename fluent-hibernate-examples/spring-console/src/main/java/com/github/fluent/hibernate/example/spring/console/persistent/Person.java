package com.github.fluent.hibernate.example.spring.console.persistent;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author V.Ladynev
 */
@MappedSuperclass
public class Person {

    private String name;

    @Column
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
