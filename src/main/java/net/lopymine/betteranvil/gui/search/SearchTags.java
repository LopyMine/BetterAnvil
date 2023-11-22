package net.lopymine.betteranvil.gui.search;

import net.minecraft.text.Text;

import net.lopymine.betteranvil.utils.StringUtils;

import org.jetbrains.annotations.Nullable;

public enum SearchTags {
    ITEM(Text.translatable("better_anvil.search.aliases.item")),
    RESOURCE_PACK(Text.translatable("better_anvil.search.aliases.resource_pack")),
    ENCHANTMENTS(Text.translatable("better_anvil.search.aliases.enchantment")),
    LORE(Text.translatable("better_anvil.search.aliases.lore")),
    COUNT(Text.translatable("better_anvil.search.aliases.count")),
    DAMAGE(Text.translatable("better_anvil.search.aliases.damage"));

    private final String[] aliases;

    SearchTags(Text text) {
        this.aliases = text.getString().split(", ");
    }

    @Nullable
    public static SearchTags getTag(String text) {
        for (SearchTags tag : SearchTags.values()) {
            for (String id : tag.aliases) {
                if (StringUtils.s(text, id)) {
                    return tag;
                }
            }
        }

        return null;
    }

    public String getTextContent(String textWithTag) {
        if (textWithTag.startsWith("#")) {
            int index = textWithTag.indexOf(" ");
            if (index != -1) {
                return textWithTag.substring(index + 1);
            }
        }
        return "";
    }

    public String getAlias(String text) {
        if (text.startsWith("#")) {
            int index = text.indexOf(" ");
            if (index != -1) {
                return text.substring(0, index);
            }
        }
        return aliases[0];
    }
}
