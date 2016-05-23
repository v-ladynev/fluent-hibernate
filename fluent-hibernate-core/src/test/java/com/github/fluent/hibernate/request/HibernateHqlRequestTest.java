package com.github.fluent.hibernate.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.cfg.Fluent;
import com.github.fluent.hibernate.internal.util.InternalUtils.CollectionUtils;
import com.github.fluent.hibernate.request.persistent.Department;
import com.github.fluent.hibernate.request.persistent.Role;
import com.github.fluent.hibernate.request.persistent.User;
import com.github.fluent.hibernate.request.persistent.UserDto;

public class HibernateHqlRequestTest {

    @BeforeClass
    public static void initSessionFactory() {
        Fluent.factory().h2ConfigForTests().showSql()
                .scanPackages("com.github.fluent.hibernate.request.persistent").build();
        createTestData();
    }

    @AfterClass
    public static void closeSessionFactory() {
        Fluent.factory().close();
    }

    private static void createTestData() {
        List<User> users = CollectionUtils.newArrayList();

        users.add(User.create("login_a", H.save(Department.create("department_a")),
                Role.create("role_a_first")));
        users.add(User.create("login_b", H.save(Department.create("department_b")),
                Role.create("role_b_first"), Role.create("role_b_second")));

        H.saveAll(users);
    }

    @Test
    public void requestWithParameters() {
        List<User> users = H.<User> request("from User u where u.login = :login")
                .p("login", "login_a").list();
        assertThat(users).isNotNull().extracting("login").containsOnly("login_a");
    }

    @Test
    public void requestWithTransform() {
        List<User> users = H.<User> request("select u.login as login from User u")
                .transform(User.class).list();
        assertThat(users).hasSize(2).extracting("login").containsOnly("login_a", "login_b");
        assertThat(users).extracting("pid").containsOnly((Long) null);
    }

    @Test
    public void requestWithDtoTransform() {
        List<UserDto> users = H
                .<UserDto> request("select u.login as login, d.name as departmentName"
                        + " from User u left join u.department d")
                .transform(UserDto.class).list();
        assertThat(users).hasSize(2).extracting("login").containsOnly("login_a", "login_b");
        assertThat(users).extracting("pid").containsOnly((Long) null);
        assertThat(users).extracting("department.name").containsOnly("department_a",
                "department_b");
    }

    @Test
    public void countAll() {
        assertThat(H.request("select count(u) from User u").count()).isEqualTo(2);
    }

    @Test
    public void countNothing() {
        assertThat(H.request("select count(u) from User u where 1=0").count()).isEqualTo(0);
    }

}
