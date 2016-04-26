package com.github.fluent.hibernate.cfg.strategy.hibernate5;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.mapping.Collection;
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
public class JoinTableStrategyTest {

    private static final Class<?>[] PERISTENTS = new Class<?>[] { User.class, Role.class };

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
        Table table = getUserRolesTable(StrategyOptions.builder().tablePrefix("table").build());

        assertThat(table.getName()).isEqualTo("table_users_roles");
        assertThat(table.getColumnSpan()).isEqualTo(2);
        assertThat(StrategyTestUtils.getColumNames(table.getColumnIterator()))
                .containsOnly("fk_user", "fk_roles");
    }

    @Test
    public void testWithoutPrefixes() {
        Table table = getUserRolesTable(StrategyOptions.builder().withoutPrefixes().build());

        assertThat(table.getName()).isEqualTo("users_roles");
        assertThat(table.getColumnSpan()).isEqualTo(2);
        assertThat(StrategyTestUtils.getColumNames(table.getColumnIterator())).containsOnly("user",
                "roles");
    }

    @Test
    public void testRestrictLength() {
        Table table = getUserRolesTable(StrategyOptions.builder().restrictLength(6)
                .restrictJoinTableNames(false).restrictConstraintNames(false).build());

        assertThat(table.getName()).isEqualTo("users_roles");
        assertThat(table.getColumnSpan()).isEqualTo(2);
        assertThat(StrategyTestUtils.getColumNames(table.getColumnIterator()))
                .containsOnly("fk_usr", "fk_rls");
    }

    private static Table getUserRolesTable(StrategyOptions options) {
        Metadata metadata = StrategyTestUtils.createMetadata(serviceRegistry,
                new Hibernate5NamingStrategy(options), PERISTENTS);
        Collection binding = metadata.getCollectionBinding(User.class.getName() + ".roles");
        assertThat(binding).isNotNull();
        return binding.getCollectionTable();
    }

    @Entity
    public static class User {

        @Id
        private Integer pid;

        @ManyToMany
        private List<Role> roles;

    }

    @Entity
    public static class Role {

        @Id
        private Integer pid;

    }

}
