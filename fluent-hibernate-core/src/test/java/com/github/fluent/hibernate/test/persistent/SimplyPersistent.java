package com.github.fluent.hibernate.test.persistent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "simply_persistents")
public class SimplyPersistent {

    public static final String DEFAULT_NAME = "name";

    private Long pid;

    private String name;

    public SimplyPersistent() {

    }

    public SimplyPersistent(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue
    public Long getPid() {
        return pid;
    }

    @Column
    public String getName() {
        return name;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static SimplyPersistent createWithDefaultName() {
        return new SimplyPersistent(DEFAULT_NAME);
    }

}
