package com.github.fluent.hibernate.example.spring.console.persistent;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

//@Entity
@Table
public class User {

    @Id
    @GeneratedValue
    @Column
    private Long id;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "users_roles", joinColumns = { @JoinColumn(name = "user_id",
            referencedColumnName = "f_id") }, inverseJoinColumns = { @JoinColumn(name = "role_id",
            referencedColumnName = "f_id") })
    private List<Role> userRoles;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "users_courses", joinColumns = { @JoinColumn(name = "user_id",
            referencedColumnName = "f_id") }, inverseJoinColumns = { @JoinColumn(
            name = "course_id", referencedColumnName = "f_id") })
    private List<Course> orderedCourses;

    public Long getId() {
        return id;
    }

    public List<Role> getUserRoles() {
        return userRoles;
    }

    public List<Course> getOrderedCourses() {
        return orderedCourses;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setUserRoles(List<Role> userRoles) {
        this.userRoles = userRoles;
    }

    public void setOrderedCourses(List<Course> orderedCourses) {
        this.orderedCourses = orderedCourses;
    }

}
