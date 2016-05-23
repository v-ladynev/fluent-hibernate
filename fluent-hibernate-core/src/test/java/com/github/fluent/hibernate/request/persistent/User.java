package com.github.fluent.hibernate.request.persistent;

import java.util.Arrays;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;

import com.github.fluent.hibernate.internal.util.InternalUtils.CollectionUtils;

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

    @ManyToMany(cascade = CascadeType.ALL)
    private List<Role> roles = CollectionUtils.newArrayList();

    @ManyToOne
    private Department department;

    public Long getPid() {
        return pid;
    }

    public String getLogin() {
        return login;
    }

    public List<Role> getRoles() {
        return roles;
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

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public static User create(String login, Role... roles) {
        return create(login, null, roles);
    }

    public static User create(String login, Department department, Role... roles) {
        User result = new User();
        result.setLogin(login);
        result.getRoles().addAll(Arrays.asList(roles));
        result.setDepartment(department);
        return result;
    }

    @Override
    public String toString() {
        return String.format("User pid: %d, login: %s, department: %s", pid, login,
                department == null ? null : department.getName());
    }

}
