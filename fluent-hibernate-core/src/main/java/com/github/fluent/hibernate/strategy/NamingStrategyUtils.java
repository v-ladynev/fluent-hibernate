package com.github.fluent.hibernate.strategy;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Utils for naming strategies.
 *
 * @author V.Ladynev
 */
public final class NamingStrategyUtils {

    /** Ending of a plural. */
    private static final String ES = "es";

    /** Ending with [sx]is. */
    private static final Pattern END_SIS = Pattern.compile("^.*[sx]is$");

    private static final Pattern END_SH = Pattern.compile("^.*[sc]h$");

    /** Ending [vowel]y. */
    private static final Pattern END_AY = Pattern.compile("^.*[aeijouy]y$");

    private NamingStrategyUtils() {

    }

    /**
     * Unqualify propety, add underscores and pluralize.
     *
     * com.github.fluent.SomeName -> some_names
     */
    public static String propertyToPluralizedName(String property) {
        return pluralize(propertyToName(property));
    }

    /**
     * Unqualify propety and add underscores.
     *
     * com.github.fluent.SomeName -> some_name
     */
    public static String propertyToName(String property) {
        return addUnderscores(unqualify(property));
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

    public static String removeVowelsFromTheRight(String name, int maxCountToRemove) {
        return removeVowelsFromTheRight(name, maxCountToRemove, false, false);
    }

    public static String removeVowelsFromTheRight(String name, int maxCountToRemove,
            boolean dontRemoveFirst, boolean dontRemoveLast) {
        StringBuilder result = new StringBuilder(name);

        int firstIndex = dontRemoveFirst ? 1 : 0;
        int lastIndex = dontRemoveLast ? result.length() - 2 : result.length() - 1;
        for (int i = lastIndex, removed = 0; i >= firstIndex && removed < maxCountToRemove; i--) {
            if (isVowel(result.charAt(i))) {
                result.deleteCharAt(i);
                removed++;
            }
        }

        return result.toString();
    }

    public static boolean isVowel(char value) {
        switch (value) {
        case 'a':
        case 'e':
        case 'i':
        case 'o':
        case 'u':
        case 'A':
        case 'E':
        case 'I':
        case 'O':
        case 'U':
            return true;
        default:
            return false;
        }
    }

}
