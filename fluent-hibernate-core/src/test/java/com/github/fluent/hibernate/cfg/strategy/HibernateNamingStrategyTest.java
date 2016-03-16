package com.github.fluent.hibernate.cfg.strategy;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.github.fluent.hibernate.cfg.strategy.HibernateNamingStrategy;

/**
 *
 * @author homyakov
 * @author V.Ladynev
 */
public class HibernateNamingStrategyTest {

    private static final String TABLE_PREFIX = "fluent_";

    private HibernateNamingStrategy strategy;

    @Before
    public void setUp() {
        strategy = new HibernateNamingStrategy();
        strategy.setTablePrefix(TABLE_PREFIX);
    }

    @Test
    public void testForeignKeyColumnName() {
        assertEquals("Foreign key: propertyName -> fk_property_name", "fk_property_name",
                strategy.foreignKeyColumnName("propertyName", "propertyTableName"));
    }

    @Test
    public void testPropertyToColumnName() {
        assertEquals("Simple field name: field -> f_field", "f_field",
                strategy.propertyToColumnName("field"));
        assertEquals("Field name: camelCaseField -> f_camel_case_field", "f_camel_case_field",
                strategy.propertyToColumnName("camelCaseField"));

        // TODO fix
        /*
        assertEquals("Field name: camelCF -> f_camel_cf", "f_camel_cf",
                strategy.propertyToColumnName("camelCF"));

        assertEquals("Field name: camelCFX -> f_camel_cfx", "f_camel_cfx",
                strategy.propertyToColumnName("camelCFX"));
         */
    }

    @Test
    public void testClassToTableName() {
        String[] args = new String[] { "Class", "Classes", "Key", "Query", "Answer", //
                "Prefix", "CamelCaseClassName", "User", "Face", "Cliff", "Safe", //
                "Book", "Table", "Dish", "Match", "Diagnosis", "Axis" };
        String[] exps = new String[] { "classes", "classes", "keys", "queries", "answers", //
                "prefixes", "camel_case_class_names", "users", "faces", "cliffs", "safes", //
                "books", "tables", "dishes", "matches", "diagnoses", "axes" };

        // TODO add multiple capitals test

        for (int i = 0; i < args.length; i++) {
            String arg = args[i], exp = TABLE_PREFIX + exps[i];

            assertEquals("Pluralize table name: " + arg + " -> " + exp, exp,
                    strategy.classToTableName(arg));
        }
    }

    @Test
    public void testJoinKeyColumnName() {
        assertEquals("Join key: joinedColumn -> joined_column_id", "joined_column_id",
                strategy.joinKeyColumnName("joinedColumn", "joinedTable"));
    }

    @Test
    public void testCollectionTableName() {
        assertEquals("Collection table name: "
                + "ownerEntity,associatedEntity -> fluent_owner_entities_associated_entities",
                "fluent_owner_entities_associated_entities",
                strategy.joinTableName("ownerEntity", "associatedEntity"));
    }

}
