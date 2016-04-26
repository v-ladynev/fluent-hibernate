package com.github.fluent.hibernate.cfg.strategy.hibernate5;

import static org.assertj.core.api.Assertions.assertThat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.UniqueConstraint;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.mapping.Collection;
import org.hibernate.mapping.Table;
import org.hibernate.service.ServiceRegistry;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import com.github.fluent.hibernate.cfg.strategy.StrategyOptions;
import com.github.fluent.hibernate.factory.HibernateSessionFactory;

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

    // @Test
    public void testWithPrefixes() {
        // @OneToMany unique, and @Column(unique = true) don't use a naming strategy

        /*
        Table table = getUserPhonesTable(StrategyOptions.builder().tablePrefix("table").build());
        assertThat(StrategyTestUtils.getForeignKeyNames(table))
                .containsOnly("F_table_users_phones_fk_phones", "F_table_users_phones_fk_user");
        
        */

        HibernateSessionFactory.Builder.configureWithoutHibernateCfgXml().useNamingStrategy()
                .annotatedClasses(UserWithPrefix.class).createSessionFactory();

    }

    // @Test
    public void testWithoutPrefixes() {
        Table table = getUserPhonesTable(StrategyOptions.builder().withoutPrefixes().build());
        assertThat(StrategyTestUtils.getForeignKeyNames(table)).containsOnly("users_phones_phones",
                "users_phones_user");
    }

    // @Test
    public void testRestrictLength() {
        Table table = getUserPhonesTable(StrategyOptions.builder().restrictLength(6)
                .restrictJoinTableNames(false).restrictConstraintNames(false).build());

        assertThat(table.getName()).isEqualTo("users_roles");
        assertThat(table.getColumnSpan()).isEqualTo(2);
        assertThat(StrategyTestUtils.getColumNames(table.getColumnIterator()))
                .containsOnly("fk_usr", "fk_rls");
    }

    private static Table getUserPhonesTable(StrategyOptions options) {
        Metadata metadata = StrategyTestUtils.createMetadata(serviceRegistry,
                new Hibernate5NamingStrategy(options), UserWithPrefix.class);
        Collection binding = metadata
                .getCollectionBinding(UserWithPrefix.class.getName() + ".phones");
        assertThat(binding).isNotNull();
        return binding.getCollectionTable();
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
