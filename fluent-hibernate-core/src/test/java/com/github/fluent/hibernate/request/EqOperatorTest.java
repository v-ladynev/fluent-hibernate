package com.github.fluent.hibernate.request;

import static com.github.fluent.hibernate.builder.Builders.eq;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.api.ListAssert;
import org.junit.Before;
import org.junit.Test;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.test.persistent.SimplyPersistent;
import com.github.fluent.hibernate.test.util.FluentHibernateBaseTest;

/**
 *
 * @author alexey.pchelnikov
 */
public class EqOperatorTest extends FluentHibernateBaseTest {

    private final List<SimplyPersistent> persistents = new ArrayList<SimplyPersistent>();

    @Before
    public void beforeEachTest() {
        dropAll();
        generateModels();
    }

    private void generateModels() {
        persistents.add(new SimplyPersistent("p0"));
        persistents.add(new SimplyPersistent("p1"));
        persistents.add(new SimplyPersistent("p2"));
        persistents.add(new SimplyPersistent(null));
        H.saveAll(persistents);
    }

    private void dropAll() {
        H.update("delete from SimplyPersistent").execute();
    }

    @Test
    public void restrictionTest() {
        List<SimplyPersistent> list = getRequest().eq("name", "p0").list();

        Long[] expectedResult = new Long[] { persistents.get(1).getPid(),
                persistents.get(2).getPid() };
        check(list, expectedResult);
    }

    @Test
    public void restrictionNullTest() {
        List<SimplyPersistent> list = getRequest().eq("name", null).list();
        check(list, new Long[0]);
    }

    @Test
    public void restrictionOrIsNullTest() {
        List<SimplyPersistent> list = getRequest().eqOrIsNull("name", null).list();

        Long[] expectedResult = new Long[] { persistents.get(3).getPid() };
        check(list, expectedResult);
    }

    @Test
    public void restrictionBuilderTest() {
        List<SimplyPersistent> list = getRequest().add(eq("name", "p0")).list();

        Long[] expectedResult = new Long[] { persistents.get(1).getPid(),
                persistents.get(2).getPid() };
        check(list, expectedResult);
    }

    @Test
    public void restrictionBuilderNullTest() {
        List<SimplyPersistent> list = getRequest().add(eq("name", null)).list();
        check(list, new Long[0]);
    }

    @Test
    public void restrictionBuilderIfNotNullTest() {
        List<SimplyPersistent> list = getRequest().add(eq("name", null).ifNotNull()).list();

        Long[] expectedResult = new Long[] { persistents.get(0).getPid(),
                persistents.get(1).getPid(), persistents.get(2).getPid(),
                persistents.get(3).getPid() };
        check(list, expectedResult);

    }

    @Test
    public void restrictionBuilderOrIsNullTest() {
        List<SimplyPersistent> list = getRequest().add(eq("name", null).orIsNull()).list();

        Long[] expectedResult = new Long[] { persistents.get(3).getPid() };
        check(list, expectedResult);

    }

    private ListAssert<Object> check(List<SimplyPersistent> list, Long[] expectedResult) {
        return assertThat(list).isNotNull().hasSize(expectedResult.length).extracting("pid")
                .containsOnly(expectedResult);
    }

    private HibernateRequest<SimplyPersistent> getRequest() {
        return H.request(SimplyPersistent.class);
    }

}
