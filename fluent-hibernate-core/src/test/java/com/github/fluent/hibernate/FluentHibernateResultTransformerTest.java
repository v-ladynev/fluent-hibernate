package com.github.fluent.hibernate;

import static com.github.fluent.hibernate.test.util.FluentHibernateTransformerTestData.ROOT_NAME_A;
import static com.github.fluent.hibernate.test.util.FluentHibernateTransformerTestData.ROOT_NAME_B;
import static com.github.fluent.hibernate.test.util.FluentHibernateTransformerTestData.STATIONAR_NAME_A;
import static com.github.fluent.hibernate.test.util.FluentHibernateTransformerTestData.STATIONAR_NAME_B;
import static com.github.fluent.hibernate.test.util.FluentHibernateTransformerTestData.rootA;
import static com.github.fluent.hibernate.test.util.FluentHibernateTransformerTestData.rootB;

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.github.fluent.hibernate.test.persistent.transformer.Root;
import com.github.fluent.hibernate.test.persistent.transformer.RootStationar;
import com.github.fluent.hibernate.test.persistent.transformer.Stationar;
import com.github.fluent.hibernate.test.util.FluentHibernateTransformerBaseTest;
import com.github.fluent.hibernate.util.InternalUtils;

/**
 *
 * @author V.Ladynev
 */
public class FluentHibernateResultTransformerTest extends FluentHibernateTransformerBaseTest {

    @Test
    public void transformStationar() {
        H.saveOrUpdate(saveStationarFrom(rootA()));
        H.saveOrUpdate(saveStationarFrom(rootB()));

        transform(ROOT_NAME_A, STATIONAR_NAME_A);
        transform(ROOT_NAME_B, STATIONAR_NAME_B);
    }

    private Root saveStationarFrom(Root root) {
        H.saveOrUpdate(root.getStationarFrom().getStationar());
        return root;
    }

    private void transform(String rootName, String stationarName) {
        List<Root> roots = H.<Root> request(Root.class).proj("rootName")
                .innerJoin("stationarFrom.stationar", "stationar")
                .proj("stationar.name", "stationarFrom.stationar.name").eq("rootName", rootName)
                .transform(Root.class).list();

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
