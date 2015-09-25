package com.github.fluent.hibernate.test.persistent;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.github.fluent.hibernate.util.InternalUtils;

/**
 *
 * @author V.Ladynev
 */
@Entity
@Table(name = "stationars")
public class Stationar extends StationarCommonData {

    private static final long serialVersionUID = -7322586341511218979L;

    private List<StationarDepartment> departments;

    public Stationar() {

    }

    @Transient
    public Stationar assureChildren() {
        if (departments == null) {
            departments = InternalUtils.CollectionUtils.newArrayList();
        }

        return this;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "stationar", fetch = FetchType.LAZY)
    public List<StationarDepartment> getDepartments() {
        return departments;
    }

    public void setDepartments(final List<StationarDepartment> departments) {
        this.departments = departments;
    }

}
