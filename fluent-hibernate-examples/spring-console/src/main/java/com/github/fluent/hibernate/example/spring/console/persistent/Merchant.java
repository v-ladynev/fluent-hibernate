/**
 * $HeadURL$
 * $Revision$
 * $Date$
 *
 * Copyright (c) Isida-Informatica, Ltd. All Rights Reserved.
 */
package com.github.fluent.hibernate.example.spring.console.persistent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 *
 * @author V.Ladynev
 * @version $Id$
 */
@Entity
@Table(name = "merchants")
public class Merchant {

    private Long pid;

    private String name;

    @Id
    @GeneratedValue
    @Column(name = "f_pid")
    public Long getPid() {
        return pid;
    }

    @Column(name = "f_name")
    public String getName() {
        return name;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Merchant create(String name) {
        Merchant result = new Merchant();
        result.setName(name);
        return result;
    }

}
