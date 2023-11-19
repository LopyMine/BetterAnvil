package net.lopymine.betteranvil.config.resourcepacks.cit.metadata;

import net.lopymine.betteranvil.config.resourcepacks.cit.CITItem;
import net.lopymine.betteranvil.config.resourcepacks.cit.CITItem.Builder;

import java.util.*;
import org.jetbrains.annotations.Nullable;

public class CITMetaDataParser {
    @Nullable
    public static CITItem parseMeta(List<String> lines) {
        CITItem.Builder builder = new Builder();
        List<String> lore = new ArrayList<>();

        for (String line : lines) {
            LineType type = getType(line);
            String lineContent = getLineContent(line);
            if (type == null) continue;

            switch (type) {
                case ITEM -> builder.items(ItemMetaDataParser.getItems(lineContent));
                case NAME -> builder.customName(RegexMetaDataParser.getString(lineContent));
                case DAMAGE -> builder.damage(DamageMetaDataParser.getDamage(lineContent));
                case LORE -> lore.add(LoreMetaDataParser.getLore(lineContent));
                case COUNT -> builder.count(CountMetaDataParser.getCount(lineContent));
                case ENCHANTMENTS -> builder.enchantments(EnchantmentsMetaDataParser.getEnchantments(lineContent));
                case ENCHANTMENT_LEVELS -> builder.enchantmentLevels(EnchantmentLevelsMetaDataParser.getEnchantmentLevels(lineContent));
                case HAND -> builder.hand(HandMetaDataParser.getHand(lineContent));
            }
        }
        if (!lore.isEmpty()) {
            builder.lore(lore);
        }
        return builder.build();
    }

    private static String getLineContent(String line) {
        int index = line.indexOf('=');
        if(index == -1) {
            return "";
        }
        return line.substring(index + 1);
    }

    @Nullable
    private static LineType getType(String line) {
        for (LineType type : LineType.values()) {
            if (type.is(line)) return type;
        }

        return null;
    }


    enum LineType {
        ITEM("matchItems", "items"),
        NAME("nbt.display.Name"),
        DAMAGE("damage"),
        COUNT("stackSize", "count"),
        ENCHANTMENTS("enchantments", "enchantmentIDs"),
        ENCHANTMENT_LEVELS("enchantmentLevels"),
        LORE("nbt.display.Lore"),
        HAND("hand");

        private final String[] aliases;

        LineType(String... aliases) {
            this.aliases = aliases;
        }

        public boolean is(String line) {
            for (String id : aliases) {
                if (line.startsWith(id)) return true;
            }

            return false;
        }
    }
}
