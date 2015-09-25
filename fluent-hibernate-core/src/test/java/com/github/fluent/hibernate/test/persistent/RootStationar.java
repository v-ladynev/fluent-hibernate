package com.github.fluent.hibernate.test.persistent;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;

/**
 *
 * @author V.Ladynev
 */
@Embeddable
public class RootStationar {

    private Stationar stationar;

    private StationarDepartment department;

    private String comment;

    @ManyToOne(fetch = FetchType.LAZY)
    public Stationar getStationar() {
        return stationar;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public StationarDepartment getDepartment() {
        return department;
    }

    @Column(length = 2047)
    public String getComment() {
        return comment;
    }

    public void setStationar(Stationar stationar) {
        this.stationar = stationar;
    }

    public void setDepartment(StationarDepartment department) {
        this.department = department;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public RootStationar assureChildren() {
        if (stationar == null) {
            stationar = new Stationar();
        }
        stationar.assureChildren();
        if (department == null) {
            department = new StationarDepartment();
        }

        return this;
    }

}
