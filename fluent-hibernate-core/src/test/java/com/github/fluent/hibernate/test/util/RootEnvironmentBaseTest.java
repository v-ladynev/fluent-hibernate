package com.github.fluent.hibernate.test.util;

import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.factory.HibernateSessionFactory;
import com.github.fluent.hibernate.test.persistent.Root;

/**
 *
 * @author V.Ladynev
 */
public class RootEnvironmentBaseTest extends FluentHibernateBaseTest
{

    @Before
    public void beforeEachTest() {
        clearRoots();
    }

    private void clearRoots() {
        H.update("delete from Root").execute();
        H.update("delete from Stationar").execute();
    }

    public static List<Root> rootsAB() {
        return Arrays.asList(rootA(), rootB());
    }

    public static Root rootA() {
        return saveStationarFrom(FluentHibernateTestData.rootA());
    }

    public static Root rootB() {
        return saveStationarFrom(FluentHibernateTestData.rootB());
    }

    private static Root saveStationarFrom(Root root) {
        H.saveOrUpdate(root.getStationarFrom().getStationar());
        return root;
    }

}
