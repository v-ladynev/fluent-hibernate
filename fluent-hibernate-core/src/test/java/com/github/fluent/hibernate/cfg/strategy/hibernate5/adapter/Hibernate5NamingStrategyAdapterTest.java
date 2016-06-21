package com.github.fluent.hibernate.cfg.strategy.hibernate5.adapter;

import static com.github.fluent.hibernate.cfg.strategy.hibernate5.StrategyTestUtils.createMetadata;
import static com.github.fluent.hibernate.cfg.strategy.hibernate5.StrategyTestUtils.getCollectionTable;
import static com.github.fluent.hibernate.cfg.strategy.hibernate5.StrategyTestUtils.getColumNames;
import static com.github.fluent.hibernate.cfg.strategy.hibernate5.StrategyTestUtils.getColumnName;
import static com.github.fluent.hibernate.cfg.strategy.hibernate5.StrategyTestUtils.getForeignKeyConstraintNames;
import static com.github.fluent.hibernate.cfg.strategy.hibernate5.StrategyTestUtils.getIndexNames;
import static com.github.fluent.hibernate.cfg.strategy.hibernate5.StrategyTestUtils.getTable;
import static com.github.fluent.hibernate.cfg.strategy.hibernate5.StrategyTestUtils.getUniqueConstraintNames;
import static com.github.fluent.hibernate.cfg.strategy.hibernate5.StrategyTestUtils.logSchemaUpdate;
import static org.assertj.core.api.Assertions.assertThat;

import org.hibernate.boot.Metadata;
import org.hibernate.boot.model.naming.EntityNaming;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.boot.model.naming.ImplicitDiscriminatorColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitIndexColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitMapKeyColumnNameSource;
import org.hibernate.boot.model.naming.ImplicitNamingStrategyJpaCompliantImpl;
import org.hibernate.boot.model.source.spi.AttributePath;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataBuildingContext;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.mapping.Table;
import org.hibernate.service.ServiceRegistry;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import com.github.fluent.hibernate.cfg.strategy.hibernate5.adapter.persistent.AuthorTable;
import com.github.fluent.hibernate.cfg.strategy.hibernate5.adapter.persistent.Book;
import com.github.fluent.hibernate.cfg.strategy.hibernate5.adapter.persistent.Customer;
import com.github.fluent.hibernate.cfg.strategy.hibernate5.adapter.persistent.ValuedCustomer;

public class Hibernate5NamingStrategyAdapterTest {

    private static final Class<?>[] ENTITIES = new Class<?>[] { AuthorTable.class, Book.class,
            Customer.class, ValuedCustomer.class };

    private static ServiceRegistry serviceRegistry;

    private static Metadata metadata;

    @BeforeClass
    public static void setUp() {
        serviceRegistry = new StandardServiceRegistryBuilder().build();
        metadata = createMetadata(serviceRegistry, createAdapter(), ENTITIES);
        logSchemaUpdate(metadata);
    }

    private static Hibernate5NamingStrategyAdapter createAdapter() {
        return new Hibernate5NamingStrategyAdapter(ImprovedNamingStrategy.INSTANCE,
                ImplicitNamingStrategyJpaCompliantImpl.INSTANCE);
    }

    @AfterClass
    public static void tearDown() {
        StandardServiceRegistryBuilder.destroy(serviceRegistry);
    }

    @Test
    public void classToTableName() {
        Table authorTable = getTable(metadata, AuthorTable.class);
        assertThat(authorTable.getName()).isEqualTo("author_table");

        Table bookTable = getTable(metadata, Book.class);
        assertThat(bookTable.getName()).isEqualTo("book");
    }

    @Test
    public void propertyToColumnName() {
        Table authorTable = getTable(metadata, AuthorTable.class);
        assertThat(getColumNames(authorTable)).containsOnly("author_pid", "author_info",
                "unique_field", "best_book", "author_type", "author_version");

        String bookPid = getColumnName(metadata, Book.class, "pid");
        assertThat(bookPid).isEqualTo("pid");
    }

    @Test
    public void collectionTableName() {
        Table booksOne = getCollectionTable(metadata, AuthorTable.class, "booksOne");
        assertThat(booksOne.getName()).isEqualTo("author_table_books_one");

        Table booksTwo = getCollectionTable(metadata, AuthorTable.class, "booksTwo");
        assertThat(booksTwo.getName()).isEqualTo("author_table_books_two");
    }

    @Test
    public void joinTableColumnNames() {
        Table booksOne = getCollectionTable(metadata, AuthorTable.class, "booksOne");
        assertThat(getColumNames(booksOne)).containsOnly("author_table", "books_one");

        Table booksTwo = getCollectionTable(metadata, AuthorTable.class, "booksTwo");
        assertThat(getColumNames(booksTwo)).containsOnly("author_table", "books_two");
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

        assertThat(bookTitles.getName()).isEqualTo("author_table_book_titles");

        assertThat(getColumNames(bookTitles)).containsOnly("author_table", "book_titles");
    }

    @Test
    public void elementCollectionEmbedded() {
        Table elementCollectionAuthorInfo = getCollectionTable(metadata, AuthorTable.class,
                "elementCollectionAuthorInfo");

        assertThat(elementCollectionAuthorInfo.getName())
                .isEqualTo("author_table_element_collection_author_info");

        assertThat(getColumNames(elementCollectionAuthorInfo)).containsOnly("author_table",
                "best_book", "author_info");
    }

    // TODO wait while Hibernate issue will be fixed
    @Test
    public void discriminatorColumn() {
        Identifier identifier = createAdapter()
                .determineDiscriminatorColumnName(new ImplicitDiscriminatorColumnNameSource() {
                    @Override
                    public MetadataBuildingContext getBuildingContext() {
                        return null;
                    }

                    @Override
                    public EntityNaming getEntityNaming() {
                        return null;
                    }
                });

        assertThat(identifier.getText()).isEqualTo("dtype");
    }

    // TODO wait while Hibernate issue will be fixed
    @Test
    public void orderColumn() {
        Identifier identifier = createAdapter()
                .determineListIndexColumnName(new ImplicitIndexColumnNameSource() {

                    @Override
                    public AttributePath getPluralAttributePath() {
                        return new AttributePath().append("booksOrdered");
                    }

                    @Override
                    public MetadataBuildingContext getBuildingContext() {
                        return null;
                    }
                });

        assertThat(identifier.getText()).isEqualTo("books_ordered_order");
    }

    // TODO wait while Hibernate issue will be fixed
    @Test
    public void mapKeyColumn() {
        Identifier identifier = createAdapter()
                .determineMapKeyColumnName(new ImplicitMapKeyColumnNameSource() {

                    @Override
                    public MetadataBuildingContext getBuildingContext() {
                        return null;
                    }

                    @Override
                    public AttributePath getPluralAttributePath() {
                        return new AttributePath().append("booksMap");
                    }
                });

        assertThat(identifier.getText()).isEqualTo("books_map_key");
    }

    @Test
    public void foreignKey() {
        Table bookTable = getTable(metadata, Book.class);
        assertThat(getForeignKeyConstraintNames(bookTable))
                .containsOnly("FK_rtsneql14vg7ay4erx1r32ddx");
    }

    @Test
    public void uniqueConstrain() {
        Table bookTable = getTable(metadata, AuthorTable.class);
        assertThat(getUniqueConstraintNames(bookTable))
                .containsOnly("UK_6hlrw9c61t7i0x23bij5cd6jc");
    }

    @Test
    public void index() {
        Table authorTable = getTable(metadata, AuthorTable.class);
        assertThat(getIndexNames(authorTable)).containsOnly("UK_adyveyc8inhw5t0pkq279r0w4");
    }

}
