package com.github.fluent.hibernate.request.persistent;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * A user.
 *
 * @author V.Ladynev
 */
@Entity
public class User {

    @Id
    @GeneratedValue
    private Long pid;

    @Column
    private String login;

    @ManyToOne
    private Department department;

    public Long getPid() {
        return pid;
    }

    public String getLogin() {
        return login;
    }

    public Department getDepartment() {
        return department;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public static User create(String login, Department department) {
        User result = new User();
        result.setLogin(login);
        result.setDepartment(department);
        return result;
    }

    @Override
    public String toString() {
        return String.format("User pid: %d, login: %s, department: %s", pid, login,
                department == null ? null : department.getName());
    }

}
