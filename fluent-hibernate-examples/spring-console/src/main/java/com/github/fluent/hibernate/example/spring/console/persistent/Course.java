package com.github.fluent.hibernate.example.spring.console.persistent;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

//@Entity
@Table
public class Course {

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @ManyToMany(mappedBy = "orderedCourses")
    private List<User> participants;

    public Long getId() {
        return id;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

}
