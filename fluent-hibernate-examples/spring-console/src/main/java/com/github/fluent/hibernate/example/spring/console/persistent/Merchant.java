/**
 * $HeadURL$
 * $Revision$
 * $Date$
 *
 * Copyright (c) Isida-Informatica, Ltd. All Rights Reserved.
 */
package com.github.fluent.hibernate.example.spring.console.persistent;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.github.fluent.hibernate.internal.util.InternalUtils;

/**
 *
 * @author V.Ladynev
 * @version $Id$
 */
@Entity
@Table
public class Merchant {

    private Long pid;

    private String name;

    private List<Customer> primaryCustomers = InternalUtils.CollectionUtils.newArrayList();

    private List<Customer> friends = InternalUtils.CollectionUtils.newArrayList();

    @Id
    @GeneratedValue
    @Column
    public Long getPid() {
        return pid;
    }

    @Column
    public String getName() {
        return name;
    }

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    public List<Customer> getPrimaryCustomers() {
        return primaryCustomers;
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public List<Customer> getFriends() {
        return friends;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrimaryCustomers(List<Customer> primaryCustomers) {
        this.primaryCustomers = primaryCustomers;
    }

    public void setFriends(List<Customer> friends) {
        this.friends = friends;
    }

    public static Merchant create(String name) {
        Merchant result = new Merchant();
        result.setName(name);
        return result;
    }

}
