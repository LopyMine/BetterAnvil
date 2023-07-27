package net.lopymine.betteranvil.modmenu.enums;

import net.minecraft.text.Text;

public enum ButtonTexture {
    THEME(Text.translatable("better_anvil.mod_menu.gui.rename_button_texture.theme")),
    RENAME(Text.translatable("better_anvil.mod_menu.gui.rename_button_texture.rename"));
    private final Text text;

    ButtonTexture(Text text) {
        this.text = text;
    }

    public Text getText() {
        return text;
    }
}
