package com.github.fluent.hibernate.strategy;

/**
 *
 * @author V.Ladynev
 */
public class Chuck {

    private final int maxLength;

    private int currentLength;

    public Chuck(int maxLength) {
        this.maxLength = maxLength;
    }

    public String tableName(String name) {
        return makeShorter(name);
    }

    private String makeShorter(String name) {
        int length = name.length();
        if (length <= maxLength) {
            return name;
        }

        currentLength = length;

        String[] parts = name.split("_");
        makeShorter(parts, name.length());

        return NamingStrategyUtils.concat(parts);
    }

    private void makeShorter(String[] parts, int initialLength) {
        for (int i = parts.length - 1; i >= 0 && currentLength > maxLength; i--) {
            parts[i] = removeVowels(parts[i]);
        }
    }

    private String removeVowels(String part) {
        boolean dontRemoveFirst = true;
        boolean dontRemoveLast = true;
        String result = NamingStrategyUtils.removeVowelsFromTheRight(part, currentLength,
                dontRemoveFirst, dontRemoveLast);

        currentLength -= part.length() - result.length();

        return result;
    }

}
