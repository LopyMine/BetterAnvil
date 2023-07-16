package net.lopymine.betteranvil.modmenu.enums;

import net.minecraft.text.Text;

public enum PositionButton {
    RIGHT(Text.translatable("better_anvil.mod_menu.menu_buttons.position.right")),
    LEFT(Text.translatable("better_anvil.mod_menu.menu_buttons.position.left"));

    private final Text text;

    PositionButton(Text text) {
        this.text = text;
    }

    public Text getText() {
        return this.text;
    }
}

