package com.github.fluent.hibernate.test.persistent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author V.Ladynev
 */
@Entity
@Table(name = "root_consultations")
public class RootConsultation extends Persistent<Long> {

    private static final long serialVersionUID = -5311889751492165807L;

    private Root root;

    private String consultant;

    private Integer consultationNumber;

    @Override
    @Id
    @GeneratedValue
    public Long getPid() {
        return pid;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    public Root getRoot() {
        return root;
    }

    public void setRoot(Root root) {
        this.root = root;
    }

    @Column(length = 511)
    public String getConsultant() {
        return consultant;
    }

    @Column
    public Integer getConsultationNumber() {
        return consultationNumber;
    }

    public void setConsultant(String consultant) {
        this.consultant = consultant;
    }

    public void setConsultationNumber(Integer consultationNumber) {
        this.consultationNumber = consultationNumber;
    }

}
