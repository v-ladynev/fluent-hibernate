package com.github.fluent.hibernate.cfg.strategy.hibernate5;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.service.ServiceRegistry;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.fluent.hibernate.annotations.FluentName;
import com.github.fluent.hibernate.cfg.strategy.StrategyOptions;

/**
 *
 * @author V.Ladynev
 */
public class EmbeddedStrategyTest {

    private static ServiceRegistry serviceRegistry;

    @BeforeClass
    public static void setUp() {
        serviceRegistry = new StandardServiceRegistryBuilder().build();
    }

    @AfterClass
    public static void tearDown() {
        StandardServiceRegistryBuilder.destroy(serviceRegistry);
    }

    @Test
    public void testWithPrefixes() {
        assertWithPrefixes(User.class);
        assertWithPrefixes(UserField.class);
    }

    private void assertWithPrefixes(Class<?> clazz) {
        Metadata metadata = StrategyTestUtils.createMetadata(serviceRegistry,
                new Hibernate5NamingStrategy(), clazz);

        PersistentClass binding = metadata.getEntityBinding(clazz.getName());

        assertThat(StrategyTestUtils.getComponentColumnNames(binding, "justName"))
                .containsOnly("f_just_name_first_name", "f_just_name_last_name");
        assertThat(StrategyTestUtils.getComponentColumnNames(binding, "prefixName"))
                .containsOnly("f_xx_prefix_first_name", "f_xx_prefix_last_name");
    }

    @Test
    public void testWithoutPrefixes() {
        assertWithoutPrefixes(User.class);
    }

    private void assertWithoutPrefixes(Class<?> clazz) {
        Metadata metadata = StrategyTestUtils.createMetadata(serviceRegistry,
                new Hibernate5NamingStrategy(StrategyOptions.builder().withoutPrefixes().build()),
                clazz);

        PersistentClass binding = metadata.getEntityBinding(clazz.getName());

        assertThat(StrategyTestUtils.getComponentColumnNames(binding, "justName"))
                .containsOnly("just_name_first_name", "just_name_last_name");
        assertThat(StrategyTestUtils.getComponentColumnNames(binding, "prefixName"))
                .containsOnly("xx_prefix_first_name", "xx_prefix_last_name");
    }

    @Test
    public void testRestrictLength() {
        assertRestrictLength(User.class);
    }

    private void assertRestrictLength(Class<?> clazz) {
        Metadata metadata = StrategyTestUtils.createMetadata(serviceRegistry,
                new Hibernate5NamingStrategy(StrategyOptions.builder().restrictLength(20).build()),
                clazz);

        PersistentClass binding = metadata.getEntityBinding(clazz.getName());

        assertThat(StrategyTestUtils.getComponentColumnNames(binding, "justName"))
                .containsOnly("f_jst_nme_first_name", "f_just_nme_last_name");

        assertThat(StrategyTestUtils.getComponentColumnNames(binding, "prefixName"))
                .containsOnly("f_xx_prefix_frst_nme", "f_xx_prefix_last_nme");
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
        @FluentName(prefix = "xx_prefix")
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

    @Entity
    public static class UserField {

        @Id
        @GeneratedValue
        private String pid;

        @Embedded
        private NameField justName;

        @Embedded
        @FluentName(prefix = "xx_prefix")
        private Name prefixName;

    }

    @Embeddable
    public static class NameField {

        @Column
        private String firstName;

        @Column
        private String lastName;

    }

}
