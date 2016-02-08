package com.github.fluent.hibernate.strategy;

/**
 *
 * @author V.Ladynev
 */
public class Chuck {

    private final int maxLength;

    private final boolean dontTouchFirst;

    private int currentToRemove;

    public Chuck(int maxLength, boolean dontTouchFirst) {
        this.maxLength = maxLength;
        this.dontTouchFirst = dontTouchFirst;
    }

    public String tableName(String name) {
        return makeShorter(name);
    }

    public String columnName(String name) {
        return makeShorter(name);
    }

    public String embeddedColumnName(String name) {
        return makeShorter(name);
    }

    public String joinTableName(String name) {
        return makeShorter(name);
    }

    public String constraintName(String name) {
        return makeShorter(name);
    }

    private String makeShorter(String name) {
        currentToRemove = name.length() - maxLength;

        if (currentToRemove <= 0) {
            return name;
        }

        String[] parts = name.split("_");
        makeShorter(parts, name.length());

        return NamingStrategyUtils.concat(parts);
    }

    private void makeShorter(String[] parts, int initialLength) {
        int firstIndex = dontTouchFirst ? 1 : 0;

        for (int i = parts.length - 1; i >= firstIndex && currentToRemove > 0; i--) {
            parts[i] = removeVowels(parts[i]);
        }
    }

    private String removeVowels(String part) {
        String result = NamingStrategyUtils.removeVowelsSmart(part, currentToRemove);

        currentToRemove -= part.length() - result.length();

        return result;
    }

}
