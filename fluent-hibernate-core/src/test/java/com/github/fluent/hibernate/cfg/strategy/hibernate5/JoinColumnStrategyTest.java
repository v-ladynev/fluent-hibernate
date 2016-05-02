package com.github.fluent.hibernate.cfg.strategy.hibernate5;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

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
public class JoinColumnStrategyTest {

    private static final Class<?>[] PERISTENTS = new Class<?>[] { Author.class, Book.class };

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
        Table table = getBooksTable(StrategyOptions.builder().tablePrefix("table").build());

        assertThat(table.getName()).isEqualTo("table_books");
        assertThat(StrategyTestUtils.getColumNames(table.getColumnIterator())).containsOnly("f_pid",
                "fk_books");
    }

    @Test
    public void testWithoutPrefixes() {
        Table table = getBooksTable(StrategyOptions.builder().withoutPrefixes().build());

        assertThat(table.getName()).isEqualTo("books");
        assertThat(StrategyTestUtils.getColumNames(table.getColumnIterator())).containsOnly("pid",
                "books");
    }

    @Test
    public void testRestrictLength() {
        Table table = getBooksTable(StrategyOptions.builder().restrictLength(6)
                .restrictTableNames(false).restrictConstraintNames(false).build());

        assertThat(table.getName()).isEqualTo("books");
        assertThat(StrategyTestUtils.getColumNames(table.getColumnIterator())).containsOnly("f_pid",
                "fk_bks");
    }

    private static Table getBooksTable(StrategyOptions options) {
        Metadata metadata = StrategyTestUtils.createMetadata(serviceRegistry,
                new Hibernate5NamingStrategy(options), PERISTENTS);
        PersistentClass binding = metadata.getEntityBinding(Book.class.getName());
        assertThat(binding).isNotNull();
        return binding.getTable();
    }

    @Entity
    public class Author {

        @Id
        private Integer pid;

        @OneToMany
        @JoinColumn
        private List<Book> books;

    }

    @Entity
    public class Book {

        @Id
        private Integer pid;

    }

}
