package com.github.fluent.hibernate.internal.transformer;

import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.ROOT_NAME_A;
import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.ROOT_NAME_B;
import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.STATIONAR_NAME_A;
import static com.github.fluent.hibernate.test.util.FluentHibernateTestData.STATIONAR_NAME_B;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.Test;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.internal.util.InternalUtils;
import com.github.fluent.hibernate.test.persistent.Root;
import com.github.fluent.hibernate.test.persistent.RootStationar;
import com.github.fluent.hibernate.test.persistent.Stationar;
import com.github.fluent.hibernate.test.util.RootEnvironmentBaseTest;

/**
 *
 * @author V.Ladynev
 */
public class FluentHibernateResultTransformerTest extends RootEnvironmentBaseTest {

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

        assertThat(root).isNotNull();
        assertThat(root.getRootName()).isEqualTo(rootName);

        RootStationar stationarFrom = root.getStationarFrom();
        assertThat(stationarFrom).isNotNull();

        Stationar stationar = stationarFrom.getStationar();
        assertThat(stationar).isNotNull();
        assertThat(stationar.getName()).isEqualTo(stationarName);
    }

}
