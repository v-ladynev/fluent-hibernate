package com.github.fluent.hibernate;

import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.ROOT_NAME_A;
import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.ROOT_NAME_B;
import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.STATIONAR_NAME_A;
import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.STATIONAR_NAME_B;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.github.fluent.hibernate.test.persistent.Root;
import com.github.fluent.hibernate.test.persistent.RootStationar;
import com.github.fluent.hibernate.test.persistent.Stationar;
import com.github.fluent.hibernate.test.util.FluentHibernateBaseTest;
import com.github.fluent.hibernate.util.InternalUtils;

/**
 *
 * @author V.Ladynev
 */
public class FluentHibernateResultTransformerTest extends FluentHibernateBaseTest {

    @Test
    public void transformStationar() {
        H.saveOrUpdateAll(rootsAB());

        transform(ROOT_NAME_A, STATIONAR_NAME_A);
        transform(ROOT_NAME_B, STATIONAR_NAME_B);
    }

    private void transform(String rootName, String stationarName) {
        List<Root> roots = H.<Root> request(Root.class).proj(Root.ROOT_NAME)
                .innerJoin("stationarFrom.stationar", "stationar")
                .proj("stationar.name", "stationarFrom.stationar.name")
                .eq(Root.ROOT_NAME, rootName).transform(Root.class).list();

        Root root = InternalUtils.CollectionUtils.first(roots);
        Assert.assertNotNull(root);
        Assert.assertEquals(rootName, root.getRootName());

        RootStationar stationarFrom = root.getStationarFrom();
        Assert.assertNotNull(stationarFrom);

        Stationar stationar = stationarFrom.getStationar();
        Assert.assertNotNull(stationar);
        Assert.assertEquals(stationarName, stationar.getName());
    }

}
