package net.lopymine.betteranvil.gui.description;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

import net.fabricmc.fabric.api.util.TriState;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.*;

import net.lopymine.betteranvil.config.BetterAnvilConfig;

public class SimpleGuiDescription extends LightweightGuiDescription {
    protected final WPlainPanel root = new WPlainPanel() {
        @Override
        public InputResult onKeyPressed(int ch, int key, int modifiers) {
            if (ch == 256) {
                MinecraftClient.getInstance().setScreen(SimpleGuiDescription.this.parent);
                return InputResult.PROCESSED;
            }

            return InputResult.IGNORED;
        }
    }.setInsets(Insets.ROOT_PANEL);

    protected final BetterAnvilConfig config = BetterAnvilConfig.getInstance();
    protected final Screen parent;
    protected int width = 0;
    protected int height = 0;

    protected SimpleGuiDescription(Screen parent) {
        this.parent = parent;

        Screen currentScreen = MinecraftClient.getInstance().currentScreen;
        if (currentScreen == null) {
            return;
        }

        this.width = currentScreen.width;
        this.height = currentScreen.height;

        root.setSize(this.width, this.height);
        setRootPanel(root);
    }

    @Override
    public void addPainters() {
    }

    @Override
    public TriState isDarkMode() {
        return TriState.of(config.isDarkMode);
    }
}
