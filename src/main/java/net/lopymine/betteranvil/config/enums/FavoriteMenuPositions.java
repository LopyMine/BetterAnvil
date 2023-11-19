package net.lopymine.betteranvil.config.enums;

import net.minecraft.text.*;

public enum FavoriteMenuPositions {
    TAB(Text.translatable("better_anvil.mod_menu.gui.favorite_menu.position.tab")),
    PANEL(Text.translatable("better_anvil.mod_menu.gui.favorite_menu.position.panel")),
    AUTO(Text.translatable("better_anvil.mod_menu.gui.favorite_menu.position.auto"));

    private final Text text;

    FavoriteMenuPositions(Text text) {
        this.text = text;
    }

    public Text getText() {
        return text;
    }
}
