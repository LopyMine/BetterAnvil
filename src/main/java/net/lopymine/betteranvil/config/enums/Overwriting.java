package net.lopymine.betteranvil.config.enums;

import net.minecraft.text.Text;

public enum Overwriting {
    RELOAD(Text.translatable("better_anvil.mod_menu.overwriting.reload")),
    CHECKBOX(Text.translatable("better_anvil.mod_menu.overwriting.checkbox")),
    OFF(Text.translatable("better_anvil.mod_menu.overwriting.off"));

    private final Text text;

    Overwriting(Text text) {
        this.text = text;
    }

    public Text getText() {
        return text;
    }
}
