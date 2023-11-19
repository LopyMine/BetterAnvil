package net.lopymine.betteranvil.config.resourcepacks.cit.metadata;

import java.util.*;
import java.util.stream.Stream;

public class EnchantmentsMetaDataParser {
    public static List<String> getEnchantments(String lineContent) {
        return Arrays.stream(lineContent.split(" ")).flatMap(string -> Stream.of(getClearString(string).toLowerCase())).toList();
    }

    private static String getClearString(String line) {
        return line.replaceAll("[\\^.$<>()*\\\\]", "");
    }
}
