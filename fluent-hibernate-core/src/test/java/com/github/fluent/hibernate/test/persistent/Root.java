package com.github.fluent.hibernate.test.persistent;

import java.util.List;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.github.fluent.hibernate.util.InternalUtils;

/**
 * Root object.
 *
 * @author V.Ladynev
 */
@Entity
@Table(name = "roots")
public class Root extends Persistent<Long> {

    public static final String ROOT_NAME = "rootName";

    private static final long serialVersionUID = -9173676908924647155L;

    private String rootName;

    private RootStationar stationarFrom;

    private RootStationar stationarTo;

    private List<RootConsultation> consultations = InternalUtils.CollectionUtils.newArrayList();

    @Override
    @Id
    @GeneratedValue
    public Long getPid() {
        return pid;
    }

    public String getRootName() {
        return rootName;
    }

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "comment", column = @Column(
            name = "f_stationar_from_comment", length = 1023)) })
    @AssociationOverrides({
        @AssociationOverride(name = "stationar", joinColumns = @JoinColumn(
                    name = "fk_stationar_from")),
                @AssociationOverride(name = "department", joinColumns = @JoinColumn(
                    name = "fk_stationar_from_department")) })
    public RootStationar getStationarFrom() {
        return stationarFrom;
    }

    @Embedded
    @AttributeOverrides({ @AttributeOverride(name = "comment", column = @Column(
            name = "f_stationar_to_comment", length = 1023)) })
    @AssociationOverrides({
        @AssociationOverride(name = "stationar", joinColumns = @JoinColumn(
                    name = "fk_stationar_to")),
                @AssociationOverride(name = "department", joinColumns = @JoinColumn(
                    name = "fk_stationar_to_department")) })
    public RootStationar getStationarTo() {
        return stationarTo;
    }

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "root", fetch = FetchType.LAZY,
            orphanRemoval = true)
    public List<RootConsultation> getConsultations() {
        return consultations;
    }

    public void setRootName(String rootName) {
        this.rootName = rootName;
    }

    public void setStationarFrom(RootStationar stationarFrom) {
        this.stationarFrom = stationarFrom;
    }

    public void setStationarTo(RootStationar stationarTo) {
        this.stationarTo = stationarTo;
    }

    public void setConsultations(List<RootConsultation> consultations) {
        this.consultations = consultations;
    }

    public Root assureChildren() {
        if (stationarFrom == null) {
            stationarFrom = new RootStationar();
        }
        stationarFrom.assureChildren();

        return this;
    }

}
