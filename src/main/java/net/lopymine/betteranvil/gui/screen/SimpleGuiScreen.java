package net.lopymine.betteranvil.gui.screen;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;

public class SimpleGuiScreen extends CottonClientScreen {
    public SimpleGuiScreen(GuiDescription description) {
        super(description);
    }

    @Override
    public boolean keyPressed(int ch, int keyCode, int modifiers) {
        if (super.keyPressed(ch, keyCode, modifiers)) {
            return true;
        }

        return description.getRootPanel().onKeyPressed(ch, keyCode, modifiers) == InputResult.PROCESSED;
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
