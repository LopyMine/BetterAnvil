package net.lopymine.betteranvil.config.enums;

import net.minecraft.text.Text;

public enum Renames {
    ALL(Text.translatable("better_anvil.mod_menu.cit_menu.renames_enum.all")),
    CUSTOM(Text.translatable("better_anvil.mod_menu.cit_menu.renames_enum.custom")),
    ONE(Text.translatable("better_anvil.mod_menu.cit_menu.renames_enum.one"));

    private final Text text;

    Renames(Text text) {
        this.text = text;
    }

    public Text getText() {
        return text;
    }
}
