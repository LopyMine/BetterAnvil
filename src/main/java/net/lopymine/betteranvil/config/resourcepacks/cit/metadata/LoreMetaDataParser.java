package net.lopymine.betteranvil.config.resourcepacks.cit.metadata;

public class LoreMetaDataParser {
    public static String getLore(String lineContent) {
        return RegexMetaDataParser.getString(lineContent);
    }
}
