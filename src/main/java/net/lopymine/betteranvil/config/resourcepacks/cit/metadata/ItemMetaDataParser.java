package net.lopymine.betteranvil.config.resourcepacks.cit.metadata;

public class ItemMetaDataParser {
    public static String getItems(String lineContent) {
        return getClearString(lineContent);
    }

    private static String getClearString(String line) {
        return line.replaceAll("[\\^.$<>()*\\\\]", "").toLowerCase();
    }
}
