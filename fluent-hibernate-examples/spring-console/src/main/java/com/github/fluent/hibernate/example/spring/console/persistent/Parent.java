package com.github.fluent.hibernate.example.spring.console.persistent;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 *
 * @author V.Ladynev
 * @version $Id$
 */
@Entity
@Table(name = "PARENT")
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "PARENT_ID", nullable = false)
    private Set<Child> medicalHistories = new HashSet<Child>();

    public int getId() {
        return id;
    }

    public Set<Child> getMedicalHistories() {
        return medicalHistories;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMedicalHistories(Set<Child> medicalHistories) {
        this.medicalHistories = medicalHistories;
    }

    public void addChild(Child child) {
        medicalHistories.add(child);
    }

}
