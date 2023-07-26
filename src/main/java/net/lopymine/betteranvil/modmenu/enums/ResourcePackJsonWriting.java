package net.lopymine.betteranvil.modmenu.enums;

import net.minecraft.text.Text;

public enum ResourcePackJsonWriting {
    LAUNCH(Text.translatable("better_anvil.mod_menu.rename_writer.launch")),
    CHECKBOX(Text.translatable("better_anvil.mod_menu.rename_writer.checkbox")),
    OFF(Text.translatable("better_anvil.mod_menu.rename_writer.off"));
    private final Text text;

    ResourcePackJsonWriting(Text text) {
        this.text = text;
    }

    public Text getText() {
        return text;
    }
}
