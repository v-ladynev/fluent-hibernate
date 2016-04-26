package com.github.fluent.hibernate.cfg.strategy.hibernate5;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.mapping.PersistentClass;
import org.hibernate.mapping.Table;
import org.hibernate.service.ServiceRegistry;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.fluent.hibernate.cfg.strategy.StrategyOptions;

/**
 *
 * @author V.Ladynev
 */
public class UniqueConstraintStrategyTest {

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
        // @OneToMany unique, and @Column(unique = true) don't use a naming strategy
        Table table = getTable(UserWithPrefix.class,
                StrategyOptions.builder().tablePrefix("table").build());
        assertThat(StrategyTestUtils.getUniqueConstraintNames(table))
                .containsOnly("U_table_user_with_prefixes_f_login");
    }

    @Test
    public void testWithoutPrefixes() {
        Table table = getTable(UserWithoutPrefix.class,
                StrategyOptions.builder().withoutPrefixes().build());
        assertThat(StrategyTestUtils.getUniqueConstraintNames(table))
                .containsOnly("user_without_prefixes_login");
    }

    @Test
    public void testRestrictLength() {
        Table table = getTable(UserWithPrefix.class,
                StrategyOptions.builder().restrictLength(21).build());
        assertThat(StrategyTestUtils.getUniqueConstraintNames(table))
                .containsOnly("U_usr_wth_prfxs_f_lgn");
    }

    private static Table getTable(Class<?> clazz, StrategyOptions options) {
        Metadata metadata = StrategyTestUtils.createMetadata(serviceRegistry,
                new Hibernate5NamingStrategy(options), clazz);
        PersistentClass binding = metadata.getEntityBinding(clazz.getName());
        assertThat(binding).isNotNull();
        return binding.getTable();
    }

    @Entity
    @javax.persistence.Table(uniqueConstraints = @UniqueConstraint(columnNames = { "f_login" }))
    public static class UserWithPrefix {

        @Id
        private Integer pid;

        @Column
        private String login;

    }

    @Entity
    @javax.persistence.Table(uniqueConstraints = @UniqueConstraint(columnNames = { "login" }))
    public static class UserWithoutPrefix {

        @Id
        private Integer pid;

        @Column
        private String login;

    }

}
