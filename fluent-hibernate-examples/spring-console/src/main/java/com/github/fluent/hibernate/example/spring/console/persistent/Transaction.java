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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author V.Ladynev
 * @version $Id$
 */
@Entity
@Table
public class Transaction {

    private Long pid;

    private Long amountDue;

    private Merchant merchant;

    private Customer customer;

    @Id
    @GeneratedValue
    @Column
    public Long getPid() {
        return pid;
    }

    @Column
    public Long getAmountDue() {
        return amountDue;
    }

    @ManyToOne
    @JoinColumn
    public Merchant getMerchant() {
        return merchant;
    }

    @ManyToOne
    @JoinColumn
    public Customer getCustomer() {
        return customer;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public void setAmountDue(Long amountDue) {
        this.amountDue = amountDue;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    @Override
    public String toString() {
        return String.format("custormer: %s, merchant: %s, amountDue: %d", customer.getName(),
                merchant.getName(), amountDue);
    }

    public static Transaction create(Customer customer, Merchant merchant) {
        Transaction result = new Transaction();
        result.setCustomer(customer);
        result.setMerchant(merchant);
        return result;
    }

}
