package com.github.fluent.hibernate.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.hibernate.Criteria;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.cfg.Fluent;
import com.github.fluent.hibernate.internal.util.InternalUtils.CollectionUtils;
import com.github.fluent.hibernate.request.persistent.Department;
import com.github.fluent.hibernate.request.persistent.User;
import com.github.fluent.hibernate.request.persistent.UserDto;

public class HibernateSqlRequestTest {

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
        users.add(User.create("login_a", H.save(Department.create("department_a"))));
        users.add(User.create("login_b", H.save(Department.create("department_b"))));
        H.saveAll(users);
    }

    @Test
    public void requestWihtResultAsObjects() {
        String sql = "select u.f_login, d.f_name "
                + "from users u left outer join departments d on u.fk_department = d.f_pid "
                + "order by u.f_login";
        List<Object[]> users = H.<Object[]> sqlRequest(sql).list();
        assertThat(users).hasSize(2);
        assertThat(users.get(0)).containsOnly("login_a", "department_a");
        assertThat(users.get(1)).containsOnly("login_b", "department_b");
    }

    @Test
    public void requestWihtResultAsOnlyObject() {
        String sql = "select u.f_login from users u";
        List<String> logins = H.<String> sqlRequest(sql).list();
        assertThat(logins).hasSize(2);
        assertThat(logins).containsOnly("login_a", "login_b");
    }

    @Test
    public void requestWithParameters() {
        List<User> users = H
                .<User> sqlRequest("select f_login as login from users u where u.f_login = :login")
                .p("login", "login_a").transform(User.class).list();
        assertThat(users).isNotNull().extracting("login").containsOnly("login_a");
    }

    @Test
    public void requestWithTransform() {
        List<User> users = H.<User> sqlRequest("select u.f_login as login from users u")
                .transform(User.class).list();
        assertThat(users).hasSize(2).extracting("login").containsOnly("login_a", "login_b");
        assertThat(users).extracting("pid").containsOnly((Long) null);
    }

    @Test
    public void requestWithDtoTransform() {
        String sql = "select u.f_login as login, d.f_name as departmentName "
                + "from users u left outer join departments d on u.fk_department = d.f_pid";
        List<UserDto> users = H.<UserDto> sqlRequest(sql).transform(UserDto.class).list();
        assertThat(users).hasSize(2).extracting("login").containsOnly("login_a", "login_b");
        assertThat(users).extracting("pid").containsOnly((Long) null);
        assertThat(users).extracting("department.name").containsOnly("department_a",
                "department_b");
    }

    @Test
    public void countAll() {
        assertThat(H.sqlRequest("select count(*) from users").count()).isEqualTo(2);
    }

    @Test
    public void countNothing() {
        assertThat(H.sqlRequest("select count(*) from users where 1=0").count()).isEqualTo(0);
    }

    @Test
    public void requestWithNestedTransform() {
        String sql = "select u.f_login as login, d.f_name as \"department.name\" "
                + "from users u left outer join departments d on u.fk_department = d.f_pid";
        List<User> users = H.<User> sqlRequest(sql).transform(User.class).list();

        assertThat(users).hasSize(2).extracting("login").containsOnly("login_a", "login_b");
        assertThat(users).extracting("pid").containsOnly((Long) null);
        assertThat(users).extracting("department.name").containsOnly("department_a",
                "department_b");
    }

    @Test
    public void addEntity() {
        String sql = "select f_pid, f_login, fk_department from users u";
        List<User> users = H.<User> sqlRequest(sql).addEntity(User.class).list();

        assertThat(users).hasSize(2).extracting("login").containsOnly("login_a", "login_b");
        assertThat(users).extracting("pid").doesNotContainNull();
        assertThat(users).extracting("department.name").containsOnly("department_a",
                "department_b");
    }

    @Test
    public void addEntityAndJoin() {
        final String sql = "select {u.*}, {d.*} from users u left outer join departments d on u.fk_department = d.f_pid";
        List<User> users = H.<User> sqlRequest(sql).addEntity("u", User.class)
                .addJoin("d", "u.department").addEntity("u", User.class)
                .useTransformer(Criteria.DISTINCT_ROOT_ENTITY).list();

        assertThat(users).hasSize(2).extracting("login").containsOnly("login_a", "login_b");
        assertThat(users).extracting("pid").doesNotContainNull();
        assertThat(users).extracting("department.name").containsOnly("department_a",
                "department_b");
    }

}
