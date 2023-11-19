package net.lopymine.betteranvil.config.resourcepacks.cit.metadata;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.*;
import org.jetbrains.annotations.Nullable;

public class RegexMetaDataParser {

    private static final Set<Character> REPLACED_CHARS = Set.of('^', '.', '$', '<', '>', '*', '(', ')');

    @Nullable
    public static String getString(String regex) {
        try {
            return convertPropertiesRegex2String(regex);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static String convertPropertiesRegex2String(String propertiesRegex) {
        List<Character> characters = new ArrayList<>();

        propertiesRegex = propertiesRegex.replaceAll("iregex:|regex:|ipattern:|pattern:", "");

        if (propertiesRegex.isEmpty()) return "";

        for (int i = 0; i < propertiesRegex.length(); i++) {
            char ch = propertiesRegex.charAt(i);

            if (ch == '\\' && i + 1 < propertiesRegex.length()) {
                char nextChar = propertiesRegex.charAt(i + 1);
                if (nextChar == 'u') {
                    characters.add(ch);
                    characters.add(nextChar);
                    i++;
                }
            } else {
                characters.add(ch);
            }
        }

        String string = StringEscapeUtils.unescapeJava(new String(convertList2Array(characters)))
                .replaceAll(":|iregex:|regex:|ipattern:|pattern:", "");

        return optimizeString(string);
    }

    public static String optimizeString(String string) {
        List<String> optimizedList = new ArrayList<>();
        String[] split = string.split("\\|");

        for (String str : split) {
            if (str != null) {
                String trim = str.trim();
                if (!trim.isEmpty()) {
                    optimizedList.add(replaceBracketContent(trim));
                }
            }
        }

        return String.join("|", optimizedList);
    }

    public static char[] convertList2Array(List<Character> list) {
        Character[] characters = list.stream().filter(ch -> !REPLACED_CHARS.contains(ch)).toArray(Character[]::new);

        char[] chars = new char[characters.length];

        for (int i = 0; i < characters.length; i++) {
            chars[i] = characters[i];
        }

        return chars;
    }

    public static char findFirstMatchingChar(String regex) {
        int startIndex = regex.indexOf('[');
        int endIndex = regex.indexOf(']');

        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            String charSet = regex.substring(startIndex + 1, endIndex);
            for (int i = 0; i < charSet.length(); i++) {
                char ch = charSet.charAt(i);
                if (Character.isLetterOrDigit(ch)) {
                    return ch;
                }
            }
        }

        return '\0';
    }

    public static String replaceBracketContent(String input) {
        int startIndex = input.indexOf('[');
        int endIndex = input.indexOf(']');

        if (startIndex != -1 && endIndex != -1 && startIndex < endIndex) {
            String charSet = input.substring(startIndex, endIndex + 1);
            char firstChar = findFirstMatchingChar(charSet);
            if (firstChar != '\0') {
                input = input.replace(charSet, String.valueOf(firstChar));
            }
        }

        return input;
    }

}
