package com.github.fluent.hibernate.strategy;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Utils for naming strategies.
 *
 * @author V.Ladynev
 */
final class NamingStrategyUtils {

    /** Ending of a plural. */
    private static final String ES = "es";

    /** Ending with [sx]is. */
    private static final Pattern END_SIS = Pattern.compile("^.*[sx]is$");

    private static final Pattern END_SH = Pattern.compile("^.*[sc]h$");

    /** Ending [vowel]y. */
    private static final Pattern END_AY = Pattern.compile("^.*[aeijouy]y$");

    private NamingStrategyUtils() {

    }

    public static String tableName(String tableName) {
        return addUnderscores(tableName);
    }

    public static String classToTableName(String className) {
        return pluralize(addUnderscores(unqualify(className)));
    }

    public static String collectionTableName(String ownerEntityTable, String associatedEntityTable,
            String ownerProperty) {
        String ownerTable = pluralize(tableName(ownerEntityTable));
        String associatedTable = pluralize(tableName(associatedEntityTable));
        return ownerProperty == null ? concat(ownerTable, associatedTable) : concat(
                concat(ownerTable, propertyToColumnName(ownerProperty)), associatedTable);
    }

    public static String propertyToColumnName(String propertyName) {
        return addUnderscores(unqualify(propertyName));
    }

    public static String addUnderscores(String name) {
        StringBuilder result = new StringBuilder(name.replace('.', '_'));
        for (int i = 1; i < result.length() - 1; i++) {
            if (Character.isLowerCase(result.charAt(i - 1))
                    && Character.isUpperCase(result.charAt(i))
                    && Character.isLowerCase(result.charAt(i + 1))) {
                result.insert(i++, '_');
            }
        }
        return result.toString().toLowerCase(Locale.ROOT);
    }

    public static String unqualify(String qualifiedName) {
        int loc = qualifiedName.lastIndexOf(".");
        return loc < 0 ? qualifiedName : qualifiedName.substring(loc + 1);
    }

    public static String concat(String left, String right) {
        return left + "_" + right;
    }

    /**
     * Make a word plural.
     *
     * TODO cache results?
     */
    public static String pluralize(final String name) {
        if (name.endsWith(ES)) { // plural already
            return name;
        }

        StringBuilder plural = new StringBuilder(name);

        if (name.endsWith("s")) {
            // ([sx])is -> \1es
            // s -> ses
            if (matches(name, END_SIS)) {
                plural.setCharAt(plural.length() - 2, 'e');
            } else {
                plural.append(ES);
            }
        } else if (name.endsWith("z") || name.endsWith("x") || matches(name, END_SH)) {
            // ([zx]) -> \1es
            // ([sc]h) -> \1es
            plural.append(ES);
        } else if (name.endsWith("y")) {
            // ([aeijouy])y -> \1ys
            // ([bcdfghklmnpqrstvwxz])y -> \1ies
            if (!matches(name, END_AY)) {
                plural.setCharAt(plural.length() - 1, 'i');
                plural.append('e');
            }
            plural.append('s');
        } else {
            plural.append('s');
        }

        return plural.toString();
    }

    private static boolean matches(CharSequence string, Pattern pattern) {
        return pattern.matcher(string).matches();
    }

}
