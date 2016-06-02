package com.github.fluent.hibernate.request;

import static com.github.fluent.hibernate.request.builder.Builders.eq;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.ListAssert;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.cfg.Fluent;
import com.github.fluent.hibernate.test.persistent.SimplyPersistent;

/**
 *
 * @author alexey.pchelnikov
 */
public class EqOperatorTest {

    private static final List<SimplyPersistent> PERSISTENTS = new ArrayList<SimplyPersistent>();

    @BeforeClass
    public static void initSessionFactory() {
        Fluent.factory().h2ConfigForTests().annotatedClasses(SimplyPersistent.class).build();
        createTestData();
    }

    @AfterClass
    public static void closeSessionFactory() {
        Fluent.factory().close();
    }

    private static void createTestData() {
        PERSISTENTS.add(new SimplyPersistent("just_name"));
        PERSISTENTS.add(new SimplyPersistent("two_objects"));
        PERSISTENTS.add(new SimplyPersistent("two_objects"));
        PERSISTENTS.add(new SimplyPersistent(null));
        H.saveAll(PERSISTENTS);
    }

    @Test
    public void restrictionTest() {
        List<SimplyPersistent> list = request().eq("name", "two_objects").list();

        Long[] expectedResult = new Long[] { PERSISTENTS.get(1).getPid(),
                PERSISTENTS.get(2).getPid() };
        check(list, expectedResult);
    }

    @Test
    public void restrictionNullTest() {
        List<SimplyPersistent> list = request().eq("name", null).list();
        check(list, new Long[0]);
    }

    @Test
    public void restrictionOrIsNullTest() {
        List<SimplyPersistent> list = request().eqOrIsNull("name", null).list();

        Long[] expectedResult = new Long[] { PERSISTENTS.get(3).getPid() };
        check(list, expectedResult);
    }

    @Test
    public void restrictionBuilderTest() {
        List<SimplyPersistent> list = request().add(eq("name", "two_objects")).list();

        Long[] expectedResult = new Long[] { PERSISTENTS.get(1).getPid(),
                PERSISTENTS.get(2).getPid() };
        check(list, expectedResult);
    }

    @Test
    public void restrictionBuilderNullTest() {
        List<SimplyPersistent> list = request().add(eq("name", null)).list();
        check(list, new Long[0]);
    }

    @Test
    public void restrictionBuilderIfNotNullTest() {
        List<SimplyPersistent> list = request().add(eq("name", null).ifNotNull()).list();

        Long[] expectedResult = new Long[] { PERSISTENTS.get(0).getPid(),
                PERSISTENTS.get(1).getPid(), PERSISTENTS.get(2).getPid(),
                PERSISTENTS.get(3).getPid() };
        check(list, expectedResult);

    }

    @Test
    public void restrictionBuilderOrIsNullTest() {
        List<SimplyPersistent> list = request().add(eq("name", null).orIsNull()).list();

        Long[] expectedResult = new Long[] { PERSISTENTS.get(3).getPid() };
        check(list, expectedResult);

    }

    private ListAssert<Object> check(List<SimplyPersistent> list, Long[] expectedResult) {
        return assertThat(list).isNotNull().hasSize(expectedResult.length).extracting("pid")
                .containsOnly(expectedResult);
    }

    private HibernateRequest<SimplyPersistent> request() {
        return H.request(SimplyPersistent.class);
    }

}
