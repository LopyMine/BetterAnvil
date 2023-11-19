package net.lopymine.betteranvil.config.enums;

import net.minecraft.text.Text;

public enum ButtonTextures {
    THEME(Text.translatable("better_anvil.mod_menu.gui.rename_button_texture.theme")),
    RENAME(Text.translatable("better_anvil.mod_menu.gui.rename_button_texture.rename"));

    private final Text text;

    ButtonTextures(Text text) {
        this.text = text;
    }

    public Text getText() {
        return text;
    }
}
