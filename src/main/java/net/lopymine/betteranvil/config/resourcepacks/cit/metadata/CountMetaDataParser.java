package net.lopymine.betteranvil.config.resourcepacks.cit.metadata;

import org.jetbrains.annotations.Nullable;

public class CountMetaDataParser {
    @Nullable
    public static CountMetaData getCount(String lineContent) {
        try {
            String clearString = getClearString(lineContent);
            String[] split = clearString.split("-");
            if(split.length == 0) {
                return null;
            }

            String s = split[0];
            if (s.isEmpty()) {
                s = split[1];
            }
            int minCount = Integer.parseInt(s.trim());
            int maxCount = split.length > 1 ? Integer.parseInt(split[1]) : minCount;
            return new CountMetaData(clearString, minCount, maxCount);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getClearString(String line) {
        return line.replaceAll("[\\^.$<>()*\\\\]", "");
    }

    public record CountMetaData(String line, int minCount, int maxCount) {
        public boolean hasMaxCount() {
            return minCount != maxCount;
        }
    }
}
