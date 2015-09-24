package com.github.fluent.hibernate.test.persistent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * A user.
 *
 * @author V.Ladynev
 */
@Entity
@Table(name = "users")
public class User {

    public static final String LOGIN = "login";

    private Long pid;

    private String login;

    private String name;

    private Integer age;

    @Id
    @GeneratedValue
    @Column(name = "f_pid")
    public Long getPid() {
        return pid;
    }

    @Column(name = "f_login")
    public String getLogin() {
        return login;
    }

    @Column(name = "f_name")
    public String getName() {
        return name;
    }

    @Column(name = "f_age")
    public Integer getAge() {
        return age;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public User cloneUser() throws CloneNotSupportedException {
        return (User) clone();
    }

    @Override
    public String toString() {
        return String.format("login = '%s', name = '%s', age = '%d'", login, name, age);
    }

}
