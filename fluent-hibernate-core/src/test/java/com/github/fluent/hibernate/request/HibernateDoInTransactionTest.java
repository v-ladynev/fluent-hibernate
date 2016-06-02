package com.github.fluent.hibernate.request;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.hibernate.Session;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.IRequest;
import com.github.fluent.hibernate.cfg.Fluent;
import com.github.fluent.hibernate.request.persistent.Department;
import com.github.fluent.hibernate.request.persistent.User;

/**
 *
 * @author V.Ladynev
 */
public class HibernateDoInTransactionTest {

    @BeforeClass
    public static void initSessionFactory() {
        Fluent.factory().h2ConfigForTests().showSql()
                .scanPackages("com.github.fluent.hibernate.request.persistent").build();

        /*
        Fluent.factory().configForTests().showSql()
                .scanPackages("com.github.fluent.hibernate.request.persistent")
                .hibernatePropertiesFromFile(new File("/work/db/oracle.properties")).build();
        */
    }

    @AfterClass
    public static void closeSessionFactory() {
        Fluent.factory().close();
    }

    // @Test
    public void nestedRequest() {
        try {
            saveUser();
        } catch (Exception ex) {
            // ignore
        }

        List<User> users = H.<User> request(User.class).list();
        assertThat(users).isEmpty();
    }

    private void saveUser() {
        H.<Void> request(new IRequest<Void>() {
            @Override
            public Void doInTransaction(Session session) {
                H.save(User.create("login_a", H.save(Department.create("department_a"))));
                throw new RuntimeException();
            }
        });
    }

}
