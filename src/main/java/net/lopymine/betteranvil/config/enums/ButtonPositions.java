package net.lopymine.betteranvil.config.enums;

import net.minecraft.text.Text;

public enum ButtonPositions {
    RIGHT(Text.translatable("better_anvil.mod_menu.menu_buttons.position.right")),
    LEFT(Text.translatable("better_anvil.mod_menu.menu_buttons.position.left"));

    private final Text text;

    ButtonPositions(Text text) {
        this.text = text;
    }

    public Text getText() {
        return this.text;
    }
}

