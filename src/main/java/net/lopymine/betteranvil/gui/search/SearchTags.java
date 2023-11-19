package net.lopymine.betteranvil.gui.search;

import org.jetbrains.annotations.Nullable;

public enum SearchTags {
    ITEM("#item"),
    RESOURCE_PACK("#resource_pack", "#resourcepack", "#pack", "#resource-pack"),
    ENCHANTMENTS("#enchantment", "#enchantments"),
    LORE("#lore", "#description"),
    COUNT("#count"),
    DAMAGE("#damage", "#durability");

    private final String[] aliases;

    SearchTags(String... aliases) {
        this.aliases = aliases;
    }

    @Nullable
    public static SearchTags getTag(String text) {
        for (SearchTags tag : SearchTags.values()) {
            for (String id : tag.aliases) {
                if (text.startsWith(id + " ")) {
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
        return textWithTag;
    }

    public String getAlias(String text) {
        for (String id : aliases) {
            if (text.startsWith(id + " ")) {
                return id;
            }
        }
        return aliases[0];
    }
}
