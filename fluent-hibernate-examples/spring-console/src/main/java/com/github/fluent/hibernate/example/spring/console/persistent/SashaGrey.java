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
@Table
public class SashaGrey extends Person {

    private Long pid;

    @Id
    @GeneratedValue
    @Column
    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

}
