package com.github.fluent.hibernate.example.spring.console.persistent;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

/**
 *
 * @author V.Ladynev
 */
@Table
@Entity
public class Student {

    @Id
    @GeneratedValue
    private int id;

    private String name;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Student> friends = new ArrayList<Student>();

    public Student() {

    }

    public Student(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Student> getFriends() {
        return friends;
    }

    public void setFriends(List<Student> friends) {
        this.friends = friends;
    }

}
