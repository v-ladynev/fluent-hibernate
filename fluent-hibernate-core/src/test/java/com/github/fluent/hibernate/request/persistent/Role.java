package com.github.fluent.hibernate.request.persistent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * A role.
 *
 * @author V.Ladynev
 */
@Entity
public class Role {

    @Id
    @GeneratedValue
    private Long pid;

    @Column
    private String name;

    public Long getPid() {
        return pid;
    }

    public String getName() {
        return name;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Role create(String name) {
        Role result = new Role();
        result.setName(name);
        return result;
    }

}
