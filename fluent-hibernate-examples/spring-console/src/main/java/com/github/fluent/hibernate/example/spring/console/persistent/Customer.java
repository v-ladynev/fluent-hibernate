/**
 * $HeadURL$
 * $Revision$
 * $Date$
 *
 * Copyright (c) Isida-Informatica, Ltd. All Rights Reserved.
 */
package com.github.fluent.hibernate.example.spring.console.persistent;

import java.util.Calendar;

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
@Table(name = "customers")
public class Customer {

    private Long pid;

    private Calendar dateCreated;

    private String name;

    @Id
    @GeneratedValue
    @Column(name = "f_pid")
    public Long getPid() {
        return pid;
    }

    @Column(name = "f_date_created")
    public Calendar getDateCreated() {
        return dateCreated;
    }

    @Column(name = "f_name")
    public String getName() {
        return name;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public void setDateCreated(Calendar dateCreated) {
        this.dateCreated = dateCreated;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static Customer create(String name) {
        Customer result = new Customer();
        result.setDateCreated(now());
        result.setName(name);
        return result;
    }

    private static Calendar now() {
        return Calendar.getInstance();
    }

}
