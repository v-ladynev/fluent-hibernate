package com.github.fluent.hibernate.example.mysql.persistent;

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
@Table(name = "user_friends")
public class UserFriend extends Person {

    private Long pid;

    private User user;

    @Id
    @GeneratedValue
    @Column(name = "f_pid")
    public Long getPid() {
        return pid;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "fk_user")
    public User getUser() {
        return user;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static UserFriend createByName(String name) {
        UserFriend result = new UserFriend();
        result.setName(name);
        return result;
    }

}
