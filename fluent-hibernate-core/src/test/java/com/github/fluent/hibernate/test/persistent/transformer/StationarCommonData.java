package com.github.fluent.hibernate.test.persistent.transformer;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

/**
 *
 * @author V.Ladynev
 */
@MappedSuperclass
public class StationarCommonData extends Persistent<Long> {

    private static final long serialVersionUID = 3385647885914129148L;

    private String name;

    @Override
    @Id
    @GeneratedValue
    public Long getPid() {
        return pid;
    }

    @Column(length = 511)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
