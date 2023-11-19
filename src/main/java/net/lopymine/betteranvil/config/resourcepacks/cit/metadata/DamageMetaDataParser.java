package net.lopymine.betteranvil.config.resourcepacks.cit.metadata;

import net.minecraft.item.Item;

import org.jetbrains.annotations.Nullable;

public class DamageMetaDataParser {
    @Nullable
    public static DamageMetaData getDamage(String lineContent) {
        try {
            return getDamageFromContent(lineContent);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Nullable
    private static DamageMetaData getDamageFromContent(String lineContent) {
        String damage = getClearString(lineContent);
        boolean hasRange = damage.contains("-");
        boolean hasPercentage = damage.contains("%");

        if (!hasRange && !hasPercentage) {
            int value = Integer.parseInt(damage);
            return new DamageMetaData(damage, (float) value, (float) value);
        }

        if (hasRange && !hasPercentage) {
            String[] split = damage.split("-");

            int min = Integer.parseInt(split[0]);
            int max = split[1].isEmpty() ? min : Integer.parseInt(split[1]);

            return new DamageMetaData(damage, min, max);
        }

        if (hasRange) {
            String string = damage.replaceAll("%", "");
            String[] split = string.split("-");

            int min = Integer.parseInt(split[0]);
            int max = split[1].isEmpty() ? min : Integer.parseInt(split[1]);

            return new DamageMetaData(damage, (float) min / 100, (float) max / 100);
        }

        return null;
    }

    private static String getClearString(String line) {
        return line.replaceAll("[\\^.$<>()*\\\\]", "");
    }

    public record DamageMetaData(String line, float minDamage, float maxDamage) {

        public int getDamageByMinDamage(Item item) {
            return (int) (item.getMaxDamage() * minDamage);
        }

        public boolean hasMaxDamage() {
            return minDamage != maxDamage;
        }
    }
}
