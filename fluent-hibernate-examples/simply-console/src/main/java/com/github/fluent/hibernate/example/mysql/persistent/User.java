package com.github.fluent.hibernate.example.mysql.persistent;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * An user.
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

    private UserAddress address;

    private List<UserFriend> friends = new ArrayList<UserFriend>();

    private List<User> goodFriends = new ArrayList<User>();

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "f_user_address_pid")
    public UserAddress getAddress() {
        return address;
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    public List<UserFriend> getFriends() {
        return friends;
    }

    public void addFriend(UserFriend friend) {
        friend.setUser(this);
        friends.add(friend);
    }

    @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(name = "good_friends", joinColumns = @JoinColumn(name = "fk_user",
    referencedColumnName = "f_pid"), inverseJoinColumns = @JoinColumn(name = "fk_friend",
    referencedColumnName = "f_pid"))
    public List<User> getGoodFriends() {
        return goodFriends;
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

    public void setAddress(UserAddress address) {
        this.address = address;
    }

    public void setFriends(List<UserFriend> friends) {
        this.friends = friends;
    }

    public void setGoodFriends(List<User> goodFriends) {
        this.goodFriends = goodFriends;
    }

    @Override
    public String toString() {
        return String.format("login = '%s', name = '%s', age = '%d'", login, name, age);
    }

    public static User create(String login, String name, int age) {
        User result = new User();
        result.setLogin(login);
        result.setName(name);
        result.setAge(age);
        return result;
    }

}
