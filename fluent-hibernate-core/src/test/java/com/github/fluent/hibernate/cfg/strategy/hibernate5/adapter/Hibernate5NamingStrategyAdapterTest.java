package com.github.fluent.hibernate.cfg.strategy.hibernate5.adapter;

import static com.github.fluent.hibernate.cfg.strategy.hibernate5.StrategyTestUtils.createMetadata;
import static com.github.fluent.hibernate.cfg.strategy.hibernate5.StrategyTestUtils.getCollectionTable;
import static com.github.fluent.hibernate.cfg.strategy.hibernate5.StrategyTestUtils.getColumNames;
import static com.github.fluent.hibernate.cfg.strategy.hibernate5.StrategyTestUtils.getColumnName;
import static com.github.fluent.hibernate.cfg.strategy.hibernate5.StrategyTestUtils.getTable;
import static com.github.fluent.hibernate.cfg.strategy.hibernate5.StrategyTestUtils.logSchemaUpdate;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Version;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.mapping.Table;
import org.hibernate.service.ServiceRegistry;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class Hibernate5NamingStrategyAdapterTest {

    private static final Class<?>[] ENTITIES = new Class<?>[] { AuthorTable.class, Book.class };

    private static ServiceRegistry serviceRegistry;

    private static Metadata metadata;

    @BeforeClass
    public static void setUp() {
        serviceRegistry = new StandardServiceRegistryBuilder().build();
        metadata = createMetadata(serviceRegistry,
                new Hibernate5NamingStrategyAdapter(ImprovedNamingStrategy.INSTANCE,
                        ImplicitNamingStrategyJpaCompliantImpl.INSTANCE),
                ENTITIES);
        logSchemaUpdate(metadata);
    }

    @AfterClass
    public static void tearDown() {
        StandardServiceRegistryBuilder.destroy(serviceRegistry);
    }

    @Test
    public void classToTableName() {
        Table authorTable = getTable(metadata, AuthorTable.class);
        assertThat(authorTable.getName())
                .isEqualTo("hibernate5naming_strategy_adapter_test$author_table");

        Table bookTable = getTable(metadata, Book.class);
        assertThat(bookTable.getName()).isEqualTo("hibernate5naming_strategy_adapter_test$book");
    }

    @Test
    public void propertyToColumnName() {
        Table authorTable = getTable(metadata, AuthorTable.class);
        assertThat(getColumNames(authorTable)).containsOnly("author_pid", "info", "best_book",
                "type", "version");

        String bookPid = getColumnName(metadata, Book.class, "pid");
        assertThat(bookPid).isEqualTo("pid");
    }

    @Test
    public void collectionTableName() {
        Table booksOne = getCollectionTable(metadata, AuthorTable.class, "booksOne");
        assertThat(booksOne.getName())
                .isEqualTo("hibernate5naming_strategy_adapter_test$author_table_books_one");

        Table booksTwo = getCollectionTable(metadata, AuthorTable.class, "booksTwo");
        assertThat(booksTwo.getName())
                .isEqualTo("hibernate5naming_strategy_adapter_test$author_table_books_two");
    }

    @Test
    public void joinTableColumnNames() {
        Table booksOne = getCollectionTable(metadata, AuthorTable.class, "booksOne");
        assertThat(getColumNames(booksOne))
                .containsOnly("hibernate5naming_strategy_adapter_test$author_table", "books_one");

        Table booksTwo = getCollectionTable(metadata, AuthorTable.class, "booksTwo");
        assertThat(getColumNames(booksTwo))
                .containsOnly("hibernate5naming_strategy_adapter_test$author_table", "books_two");
    }

    @Test
    public void foreignKeyColumnName() {
        Table booksOne = getCollectionTable(metadata, AuthorTable.class, "booksOne");
        assertThat(getColumNames(booksOne)).contains("books_one");

        Table booksTwo = getCollectionTable(metadata, AuthorTable.class, "booksTwo");
        assertThat(getColumNames(booksTwo)).contains("books_two");

        Table bookTable = getTable(metadata, Book.class);
        assertThat(getColumNames(bookTable)).contains("books_three");
    }

    @Test
    public void elementCollection() {
        Table bookTitles = getCollectionTable(metadata, AuthorTable.class, "bookTitles");

        assertThat(bookTitles.getName())
                .isEqualTo("hibernate5naming_strategy_adapter_test$author_table_book_titles");

        assertThat(getColumNames(bookTitles))
                .containsOnly("hibernate5naming_strategy_adapter_test$author_table", "book_titles");
    }

    @Test
    public void elementCollectionEmbedded() {
        Table elementCollectionAuthorInfo = getCollectionTable(metadata, AuthorTable.class,
                "elementCollectionAuthorInfo");

        assertThat(elementCollectionAuthorInfo.getName()).isEqualTo(
                "hibernate5naming_strategy_adapter_test$author_table_element_collection_author_info");

        assertThat(getColumNames(elementCollectionAuthorInfo)).containsOnly(
                "hibernate5naming_strategy_adapter_test$author_table", "best_book", "info");
    }

    @Entity
    public static class AuthorTable {

        @Id
        private Long authorPid;

        @Embedded
        private AuthorInfo authorInfo;

        @ManyToMany
        private List<Book> booksOne;

        @ManyToMany
        private List<Book> booksTwo;

        @OneToMany
        @JoinColumn
        private List<Book> booksThree;

        @ElementCollection
        private List<String> bookTitles;

        @Enumerated
        private AuthorType type;

        @Version
        private Integer version;

        @ElementCollection
        @Embedded
        private List<AuthorInfo> elementCollectionAuthorInfo;

    }

    @Entity
    public static class Book {

        @Id
        private Long pid;

    }

    @Embeddable
    public static class AuthorInfo {

        @Column
        private String info;

        @OneToOne
        private Book bestBook;

    }

    public enum AuthorType {
        FAMOUS, NOT_FAMOUS
    }

}
