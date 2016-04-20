package com.github.fluent.hibernate.cfg.strategy.hibernate5;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.mapping.PersistentClass;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.fluent.hibernate.annotations.FluentName;
import com.github.fluent.hibernate.cfg.strategy.StrategyOptions;

/**
 *
 * @author V.Ladynev
 */
public class EmbeddedMethodsStrategyTest {

    private static Metadata metadata;

    @BeforeClass
    public static void initMetadata() {
        metadata = new MetadataSources().addAnnotatedClass(User.class).getMetadataBuilder()
                .applyImplicitNamingStrategy(new Hibernate5NamingStrategy(
                        StrategyOptions.builder().autodetectMaxLength().build()))
                .build();
    }

    @AfterClass
    public static void closeMetadata() {

    }

    @Test
    public void testStrategy() {
        PersistentClass userBinding = metadata.getEntityBinding(User.class.getName());
        assertThat(StrategyTestUtils.getComponentColumnNames(userBinding, "justName"))
                .containsExactly("f_just_name_first_name", "f_just_name_last_name");
        assertThat(StrategyTestUtils.getComponentColumnNames(userBinding, "prefixName"))
                .containsExactly("f_prefix_first_name", "f_prefix_last_name");
    }

    @Entity
    public static class User {

        private String pid;

        private Name justName;

        private Name prefixName;

        @Id
        @GeneratedValue
        public String getPid() {
            return pid;
        }

        @Embedded
        public Name getJustName() {
            return justName;
        }

        @Embedded
        @FluentName(prefix = "prefix")
        public Name getPrefixName() {
            return prefixName;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public void setJustName(Name justName) {
            this.justName = justName;
        }

        public void setPrefixName(Name prefixName) {
            this.prefixName = prefixName;
        }

    }

    @Embeddable
    public static class Name {

        private String firstName;

        private String lastName;

        @Column
        public String getFirstName() {
            return firstName;
        }

        @Column
        public String getLastName() {
            return lastName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

    }

}
