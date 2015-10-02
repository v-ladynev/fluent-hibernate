package com.github.fluent.hibernate.test.request;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.HibernateRequest;
import com.github.fluent.hibernate.test.persistent.SimplyPersistent;
import com.github.fluent.hibernate.test.util.FluentHibernateBaseTest;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 *
 * @author alexey.pchelnikov
 */
public class InOperationTest extends FluentHibernateBaseTest {

    public static final Comparator<SimplyPersistent> SIMPLY_PERSISTENT_COMPARATOR = new Comparator<SimplyPersistent>() {
        @Override
        public int compare(SimplyPersistent o1, SimplyPersistent o2) {
            return o1.getPid().compareTo(o2.getPid());
        }
    };

    private List<SimplyPersistent> models = new ArrayList<SimplyPersistent>();

    @Before
    public void beforeEachTest() {
        dropAll();
        generateModels();
    }

    private void generateModels() {
        models.add(new SimplyPersistent("test0"));
        models.add(new SimplyPersistent("test1"));
        models.add(new SimplyPersistent("test2"));
        H.saveAll(models);
    }

    private void dropAll() {
        //TODO add method delete(Iterable<? extends T> entities)
        for (SimplyPersistent model : getRequest().list()) {
            H.delete(model);
        }
    }

    @Test
    public void simpleTest() {
        List<SimplyPersistent> list = getRequest().list();
        assertThat(list)
                .isNotNull()
                .hasSize(3);

    }

    @Test
    public void restrictionTest() {
        SimplyPersistent[] expectedResult = new SimplyPersistent[] { models.get(0), models.get(1) };
        List<String> inRestriction = getNameRestriction(expectedResult);

        List<SimplyPersistent> foundList = getRequest()
                .in("name", inRestriction)
                .list();

        assertThat(foundList)
                .isNotNull()
                .hasSize(2)
                .usingElementComparator(SIMPLY_PERSISTENT_COMPARATOR)
                .containsOnly(expectedResult);
    }

    @Test
    public void emptyRestrictionTest() {
        List<String> inRestriction = new ArrayList<String>();

        List<SimplyPersistent> foundList = getRequest()
                .in("name", inRestriction)
                .list();

        assertThat(foundList)
                .isNotNull()
                .hasSize(3);
    }

    @Test
    public void restrictionNothingForEmptyCollectionTest() {
        List<String> inRestriction = new ArrayList<String>();

        List<SimplyPersistent> foundList = getRequest()
                .inNothingForEmptyCollection("name", inRestriction)
                .list();

        assertThat(foundList)
                .isNotNull()
                .hasSize(0);
    }

    @Test
    public void emptyRestrictionNothingForEmptyCollectionTest() {
        SimplyPersistent[] expectedResult = new SimplyPersistent[] { models.get(0), models.get(1) };
        List<String> inRestriction = getNameRestriction(expectedResult);

        List<SimplyPersistent> foundList = getRequest()
                .inNothingForEmptyCollection("name", inRestriction)
                .list();

        assertThat(foundList)
                .isNotNull()
                .hasSize(2)
                .usingElementComparator(SIMPLY_PERSISTENT_COMPARATOR)
                .containsOnly(expectedResult);
    }

    private List<String> getNameRestriction(SimplyPersistent[] expectedResult) {
        List<String> inRestriction = new ArrayList<String>();
        for (SimplyPersistent simplyPersistent : expectedResult) {
            inRestriction.add(simplyPersistent.getName());
        }
        return inRestriction;
    }

    private HibernateRequest<SimplyPersistent> getRequest() {
        return H.request(SimplyPersistent.class);
    }

}
