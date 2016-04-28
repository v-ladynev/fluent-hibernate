package com.github.fluent.hibernate.cfg.strategy;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

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
        strategy = new HibernateNamingStrategy(
                StrategyOptions.builder().tablePrefix(TABLE_PREFIX).dontRestrictLength().build());
    }

    @Test
    public void foreignKeyColumnName() {
        assertThat(strategy.foreignKeyColumnName("propertyName", "propertyTableName"))
                .isEqualTo("fk_property_name");
        assertThat(strategy.foreignKeyColumnName(null, "propertyTableName"))
                .isEqualTo("fk_property_table_name");

        strategy.getOptions().setForeignKeyColumnPrefix(null);
        assertThat(strategy.foreignKeyColumnName("propertyName", "propertyTableName"))
                .isEqualTo("property_name");
        assertThat(strategy.foreignKeyColumnName(null, "propertyTableName"))
                .isEqualTo("property_table_name");
    }

    @Test
    public void propertyToColumnName() {
        assertThat(strategy.propertyToColumnName("field")).isEqualTo("f_field");
        assertThat(strategy.propertyToColumnName("camelCaseField")).isEqualTo("f_camel_case_field");

        strategy.getOptions().setColumnPrefix(null);
        assertThat(strategy.propertyToColumnName("camelCaseField")).isEqualTo("camel_case_field");

        // TODO fix
        /*
        assertThat(strategy.propertyToColumnName("camelCF")).isEqualTo("f_camel_cf");
        assertThat(strategy.propertyToColumnName("f_camel_cfx")).isEqualTo("camelCFX");
         */
    }

    @Test
    public void classToTableName() {
        String[] args = new String[] { "Class", "Classes", "Key", "Query", "Answer", //
                "Prefix", "CamelCaseClassName", "User", "Face", "Cliff", "Safe", //
                "Book", "Table", "Dish", "Match", "Diagnosis", "Axis" };
        String[] exps = new String[] { "classes", "classes", "keys", "queries", "answers", //
                "prefixes", "camel_case_class_names", "users", "faces", "cliffs", "safes", //
                "books", "tables", "dishes", "matches", "diagnoses", "axes" };

        // TODO add multiple capitals test

        for (int i = 0; i < args.length; i++) {
            String arg = args[i], exp = TABLE_PREFIX + exps[i];
            assertThat(strategy.classToTableName(arg)).isEqualTo(exp);
        }

        strategy.getOptions().setTablePrefix(null);
        assertThat(strategy.classToTableName("CamelCaseClassName"))
                .isEqualTo("camel_case_class_names");
    }

    @Test
    public void joinKeyColumnName() {
        assertThat(strategy.joinKeyColumnName("joinedColumn", "joinedTable"))
                .isEqualTo("joined_column_id");
    }

    @Test
    public void joinTableName() {
        assertThat(strategy.joinTableName("ownerEntity", "associatedEntity"))
                .isEqualTo("fluent_owner_entities_associated_entities");

        assertThat(strategy.joinTableName("ownerEntity", "associatedEntity", "ownerProperty"))
                .isEqualTo("fluent_owner_entities_associated_entities_owner_property");

        strategy.getOptions().setTablePrefix(null);
        assertThat(strategy.joinTableName("ownerEntity", "associatedEntity"))
                .isEqualTo("owner_entities_associated_entities");
    }

    @Test
    public void embeddedPropertyToColumnName() {
        final boolean dontTouchPrefix = false;

        strategy.getOptions().setMaxLength(19);
        assertThat(strategy.embeddedPropertyToColumnName("preFix", "someProperty", dontTouchPrefix))
                .isEqualTo("f_pre_fx_sme_prprty");

        strategy.getOptions().setMaxLength(22);
        assertThat(strategy.embeddedPropertyToColumnName("preFix", "someProperty", dontTouchPrefix))
                .isEqualTo("f_pre_fx_some_property");

        strategy.getOptions().setMaxLength(17);
        strategy.getOptions().setColumnPrefix(null);
        assertThat(strategy.embeddedPropertyToColumnName("preFix", "someProperty", dontTouchPrefix))
                .isEqualTo("pre_fx_sme_prprty");
    }

    @Test
    public void embeddedPropertyToColumnNameDontTouchPrefix() {
        final boolean dontTouchPrefix = true;

        strategy.getOptions().setMaxLength(20);
        assertThat(
                strategy.embeddedPropertyToColumnName("pre_fix", "someProperty", dontTouchPrefix))
                        .isEqualTo("f_pre_fix_sme_prprty");

        strategy.getOptions().setMaxLength(18);
        strategy.getOptions().setColumnPrefix(null);
        assertThat(
                strategy.embeddedPropertyToColumnName("pre_fix", "someProperty", dontTouchPrefix))
                        .isEqualTo("pre_fix_sme_prprty");
    }

    @Test
    public void foreignKeyConstraintName() {
        assertThat(strategy.foreignKeyConstraintName("some_table", "some_field"))
                .isEqualTo("F_some_table_some_field");

        strategy.getOptions().setForeignKeyConstraintPrefix(null);
        assertThat(strategy.foreignKeyConstraintName("some_table", "some_field"))
                .isEqualTo("some_table_some_field");
    }

    @Test
    public void uniqueKeyConstraintName() {
        assertThat(strategy.uniqueKeyConstraintName("some_table", "some_field"))
                .isEqualTo("U_some_table_some_field");

        strategy.getOptions().setUniqueKeyConstraintPrefix(null);
        assertThat(strategy.uniqueKeyConstraintName("some_table", "some_field"))
                .isEqualTo("some_table_some_field");
    }

}
