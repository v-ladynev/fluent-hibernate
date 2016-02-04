package com.github.fluent.hibernate.strategy;

import java.util.StringTokenizer;

/**
 *
 * @author V.Ladynev
 */
public class Chuck {

    private final int maxLength;

    public Chuck(int maxLength) {
        this.maxLength = maxLength;
    }

    public String tableName(String name) {
        return makeShorter(name);
    }

    private String makeShorter(String name) {
        if (name.length() <= maxLength) {
            return name;
        }

        String[] tokens = splitName(name);
        shortenName(name, tokens);

        return assembleResults(tokens);
    }

    private String[] splitName(String someName) {
        StringTokenizer toki = new StringTokenizer(someName, "_");
        String[] tokens = new String[toki.countTokens()];
        int i = 0;
        while (toki.hasMoreTokens()) {
            tokens[i++] = toki.nextToken();
        }
        return tokens;
    }

    private void shortenName(String someName, String[] tokens) {
        int currentLength = someName.length();
        while (currentLength > maxLength) {
            int tokenIndex = getIndexOfLongest(tokens);
            String oldToken = tokens[tokenIndex];
            tokens[tokenIndex] = NamingStrategyUtils.removeVowelsFromTheRight(oldToken, 10, true,
                    true);
            currentLength -= oldToken.length() - tokens[tokenIndex].length();
        }
    }

    private String assembleResults(String[] tokens) {
        StringBuilder result = new StringBuilder(tokens[0]);
        for (int j = 1; j < tokens.length; j++) {
            result.append("_").append(tokens[j]);
        }
        return result.toString();
    }

    private int getIndexOfLongest(String[] tokens) {
        int maxLength = 0;
        int index = -1;
        for (int i = 0; i < tokens.length; i++) {
            String string = tokens[i];
            if (maxLength < string.length()) {
                maxLength = string.length();
                index = i;
            }
        }
        return index;
    }

}
