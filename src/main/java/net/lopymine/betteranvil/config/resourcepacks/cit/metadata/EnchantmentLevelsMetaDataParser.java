package net.lopymine.betteranvil.config.resourcepacks.cit.metadata;

import org.jetbrains.annotations.Nullable;

public class EnchantmentLevelsMetaDataParser {
    @Nullable
    public static EnchantmentLevelsMetaData getEnchantmentLevels(String lineContent) {
        try {
            String clearString = getClearString(lineContent);
            String[] split = clearString.split("-");
            if (split.length == 0) {
                return null;
            }

            String s = split[0];
            if (s.isEmpty()) {
                s = split[1];
            }
            int minCount = Integer.parseInt(s.trim());
            int maxCount = split.length > 1 ? Integer.parseInt(split[1]) : minCount;
            return new EnchantmentLevelsMetaData(clearString, minCount, maxCount);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private static String getClearString(String line) {
        return line.replaceAll("[\\^.$<>()*\\\\]", "");
    }

    public record EnchantmentLevelsMetaData(String line, int minLevel, int maxLevel) {
        public boolean hasMaxLevel() {
            return minLevel != maxLevel;
        }

        @Override
        public String toString() {
            return "EnchantmentLevelsMetaData{" +
                    "line='" + line + '\'' +
                    ", minLevel=" + minLevel +
                    ", maxLevel=" + maxLevel +
                    '}';
        }
    }
}
