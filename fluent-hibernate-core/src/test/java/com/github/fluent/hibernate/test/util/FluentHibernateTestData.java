package com.github.fluent.hibernate.test.util;

import com.github.fluent.hibernate.H;
import com.github.fluent.hibernate.HibernateRequest;
import com.github.fluent.hibernate.test.persistent.Root;
import com.github.fluent.hibernate.test.persistent.RootStationar;
import com.github.fluent.hibernate.test.persistent.Stationar;

/**
 *
 * @author V.Ladynev
 */
public final class FluentHibernateTestData {

    public static final String ROOT_NAME_A = "A rootName";

    public static final String STATIONAR_NAME_A = "A stationarName";

    public static final String ROOT_NAME_B = "B rootName";

    public static final String STATIONAR_NAME_B = "B stationarName";

    private FluentHibernateTestData() {

    }

    public static HibernateRequest<Root> createRequestForRootA() {
        return createRootRequest().eq(Root.ROOT_NAME, ROOT_NAME_A);
    }

    public static HibernateRequest<Root> createRootRequest() {
        return H.<Root> request(Root.class);
    }

    public static Root rootA() {
        return new RootBuilder("A").build();
    }

    public static Root rootB() {
        return new RootBuilder("B").build();
    }

    private static final class RootBuilder {

        private final String prefix;

        private RootBuilder(String prefix) {
            this.prefix = prefix;
        }

        public Root build() {
            Root result = new Root();
            result.setRootName(p("rootName"));
            result.setStationarFrom(createRootStationar());
            return result;
        }

        private RootStationar createRootStationar() {
            RootStationar result = new RootStationar();
            result.setComment(p("rootStationarComment"));
            result.setStationar(createStationar());
            return result;
        }

        private Stationar createStationar() {
            Stationar result = new Stationar();
            result.setName(p("stationarName"));
            return result;
        }

        private String p(String name) {
            return prefix + " " + name;
        }

    }

}
