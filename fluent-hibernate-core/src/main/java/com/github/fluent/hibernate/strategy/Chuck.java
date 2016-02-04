package com.github.fluent.hibernate.strategy;

/**
 *
 * @author V.Ladynev
 */
public class Chuck {

    private final int maxLength;

    private int currentToRemove;

    public Chuck(int maxLength) {
        this.maxLength = maxLength;
    }

    public String tableName(String name) {
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
        for (int i = parts.length - 1; i >= 0 && currentToRemove > 0; i--) {
            parts[i] = removeVowels(parts[i]);
        }
    }

    private String removeVowels(String part) {
        boolean dontRemoveFirst = true;
        boolean dontRemoveLast = true;
        String result = NamingStrategyUtils.removeVowelsFromTheRight(part, currentToRemove,
                dontRemoveFirst, dontRemoveLast);

        currentToRemove -= part.length() - result.length();

        return result;
    }

}
