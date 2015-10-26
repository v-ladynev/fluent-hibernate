package com.github.fluent.hibernate.request;

import static com.github.fluent.hibernate.builder.Builders.in;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.test.persistent.SimplyPersistent;
import com.github.fluent.hibernate.test.util.FluentHibernateBaseTest;

/**
 *
 * @author alexey.pchelnikov
 */
public class InOperatorTest extends FluentHibernateBaseTest {

    public static final Comparator<SimplyPersistent> COMPARE_BY_PID = new Comparator<SimplyPersistent>() {
        @Override
        public int compare(SimplyPersistent o1, SimplyPersistent o2) {
            return o1.getPid().compareTo(o2.getPid());
        }
    };

    private final List<SimplyPersistent> persistents = new ArrayList<SimplyPersistent>();

    @Before
    public void beforeEachTest() {
        dropAll();
        generateModels();
    }

    private void dropAll() {
        H.update("delete from SimplyPersistent").execute();
    }

    private void generateModels() {
        persistents.add(new SimplyPersistent("p0"));
        persistents.add(new SimplyPersistent("p1"));
        persistents.add(new SimplyPersistent("p2"));
        H.saveAll(persistents);
    }

    @Test
    public void listAll() {
        List<SimplyPersistent> list = getRequest().list();
        assertThat(list).isNotNull().hasSize(3);
    }

    @Test
    public void inWithCollection() {
        SimplyPersistent[] expectedResult = twoFirstPersistent();
        List<SimplyPersistent> list = getRequest().in("name", getNames(expectedResult)).list();

        assertThat(list).isNotNull().hasSize(2).usingElementComparator(COMPARE_BY_PID)
                .containsOnly(expectedResult);
    }

    @Test
    public void inWithEmptyCollection() {
        List<SimplyPersistent> list = getRequest().in("name", Collections.emptyList()).list();
        assertThat(list).isNotNull().hasSize(3);
    }

    @Test
    public void inWithEmptyArray() {
        SimplyPersistent[] expectedResult = twoFirstPersistent();
        Object[] names = getNames(expectedResult).toArray(new Object[0]);
        List<SimplyPersistent> list = getRequest().in("name", names).list();

        assertThat(list).isNotNull().hasSize(2).usingElementComparator(COMPARE_BY_PID)
                .containsOnly(expectedResult);
    }

    @Test
    public void inWithArray() {
        List<SimplyPersistent> list = getRequest().in("name", new Object[0]).list();
        assertThat(list).isNotNull().hasSize(3);
    }

    @Test
    public void nothingForEmptyCollectionEmpty() {
        List<SimplyPersistent> list = getRequest().add(
                in("name", Collections.emptyList()).nothingForEmptyCollection()).list();

        assertThat(list).isNotNull().hasSize(0);
    }

    @Test
    public void nothingForEmptyCollectionNotEmpty() {
        SimplyPersistent[] expectedResult = twoFirstPersistent();

        List<SimplyPersistent> list = getRequest().add(
                in("name", getNames(expectedResult)).nothingForEmptyCollection()).list();

        assertThat(list).isNotNull().hasSize(2).usingElementComparator(COMPARE_BY_PID)
                .containsOnly(expectedResult);
    }

    @Test
    public void onlyArrayArgument() {
        SimplyPersistent expected = persistents.get(0);
        List<SimplyPersistent> list = getRequest().in("name", new Object[] { expected.getName() })
                .list();
        assertThat(list).isNotNull().hasSize(1).usingElementComparator(COMPARE_BY_PID)
        .containsOnly(expected);
    }

    private SimplyPersistent[] twoFirstPersistent() {
        return new SimplyPersistent[] { persistents.get(0), persistents.get(1) };
    }

    private static List<String> getNames(SimplyPersistent[] fromPersistents) {
        List<String> result = new ArrayList<String>();
        for (SimplyPersistent persistent : fromPersistents) {
            result.add(persistent.getName());
        }
        return result;
    }

    private static HibernateRequest<SimplyPersistent> getRequest() {
        return H.request(SimplyPersistent.class);
    }

}
