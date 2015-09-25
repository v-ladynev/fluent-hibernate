package com.github.fluent.hibernate.test.persistent;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.ForeignKey;

import com.github.fluent.hibernate.util.InternalUtils;

/**
 *
 * @author V.Ladynev
 */
@Entity
@Table(name = "stationar_rooms")
public class StationarRoom extends Persistent<Long> {

    private static final long serialVersionUID = 8849418147488510792L;

    private Stationar stationar;

    private List<StationarDepartment> departments;

    private Boolean individual;

    public StationarRoom() {

    }

    @Override
    @Id
    @GeneratedValue
    public Long getPid() {
        return pid;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @ForeignKey(name = "fk_stationaries_rooms")
    public Stationar getStationar() {
        return stationar;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "room", fetch = FetchType.LAZY)
    public List<StationarDepartment> getDepartments() {
        return departments;
    }

    @Column
    public Boolean getIndividual() {
        return individual;
    }

    public void setStationar(Stationar stationar) {
        this.stationar = stationar;
    }

    public void setDepartments(List<StationarDepartment> departments) {
        this.departments = departments;
    }

    public void setIndividual(Boolean individual) {
        this.individual = individual;
    }

    @Transient
    public StationarRoom assureChildren() {
        if (departments == null) {
            departments = InternalUtils.CollectionUtils.newArrayList();
        }

        if (stationar == null) {
            stationar = new Stationar();
        }
        stationar.assureChildren();

        return this;
    }

}
