package com.github.fluent.hibernate.cfg.strategy.hibernate5;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;

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
public class ForeignKeyConstraintStrategyTest {

    private static final Class<?>[] ENTITIES = new Class<?>[] { User.class, Phone.class };

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
        Table table = getUserPhonesTable(StrategyOptions.builder().tablePrefix("table").build());
        assertThat(StrategyTestUtils.getForeignKeyConstraintNames(table))
                .containsOnly("F_table_users_phones_fk_phones", "F_table_users_phones_fk_user");
    }

    @Test
    public void testWithoutPrefixes() {
        Table table = getUserPhonesTable(StrategyOptions.builder().withoutPrefixes().build());
        assertThat(StrategyTestUtils.getForeignKeyConstraintNames(table)).containsOnly("users_phones_phones",
                "users_phones_user");
    }

    @Test
    public void testRestrictLength() {
        Table table = getUserPhonesTable(
                StrategyOptions.builder().restrictLength(19).restrictJoinTableNames(false).build());
        assertThat(StrategyTestUtils.getForeignKeyConstraintNames(table)).containsOnly("F_usrs_phns_fk_phns",
                "F_users_phns_fk_usr");
    }

    private static Table getUserPhonesTable(StrategyOptions options) {
        Metadata metadata = StrategyTestUtils.createMetadata(serviceRegistry,
                new Hibernate5NamingStrategy(options), ENTITIES);
        Collection binding = metadata.getCollectionBinding(User.class.getName() + ".phones");
        assertThat(binding).isNotNull();
        return binding.getCollectionTable();
    }

    @Entity
    public static class User {

        @Id
        private Integer pid;

        @OneToMany
        private List<Phone> phones;

    }

    @Entity
    public static class Phone {

        @Id
        private Integer pid;

    }

}
