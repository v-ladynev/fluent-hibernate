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
public class JoinTableStrategyTest {

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
        Table table = getAuthorBooksTable(StrategyOptions.builder().tablePrefix("table").build());

        assertThat(table.getName()).isEqualTo("table_authors_books");
        assertThat(StrategyTestUtils.getColumNames(table.getColumnIterator()))
                .containsOnly("fk_author", "fk_books");
    }

    @Test
    public void testWithoutPrefixes() {
        Table table = getAuthorBooksTable(StrategyOptions.builder().withoutPrefixes().build());

        assertThat(table.getName()).isEqualTo("authors_books");
        assertThat(StrategyTestUtils.getColumNames(table.getColumnIterator()))
                .containsOnly("author", "books");
    }

    @Test
    public void testRestrictLength() {
        Table table = getAuthorBooksTable(StrategyOptions.builder().restrictLength(7)
                .restrictJoinTableNames(false).restrictConstraintNames(false).build());

        assertThat(table.getName()).isEqualTo("authors_books");
        assertThat(StrategyTestUtils.getColumNames(table.getColumnIterator()))
                .containsOnly("fk_athr", "fk_boks");
    }

    private static Table getAuthorBooksTable(StrategyOptions options) {
        Metadata metadata = StrategyTestUtils.createMetadata(serviceRegistry,
                new Hibernate5NamingStrategy(options), PERISTENTS);
        Collection binding = metadata.getCollectionBinding(Author.class.getName() + ".books");
        assertThat(binding).isNotNull();
        return binding.getCollectionTable();
    }

    @Entity
    public class Author {

        @Id
        private Integer pid;

        @OneToMany
        private List<Book> books;

    }

    @Entity
    public class Book {

        @Id
        private Integer pid;

    }

}
