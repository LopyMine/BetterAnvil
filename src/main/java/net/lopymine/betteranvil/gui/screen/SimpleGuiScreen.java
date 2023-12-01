package net.lopymine.betteranvil.gui.screen;

import net.minecraft.client.gui.DrawContext;
import org.lwjgl.opengl.GL11;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.*;
import io.github.cottonmc.cotton.gui.impl.VisualLogger;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;

import net.lopymine.betteranvil.gui.widgets.buttons.*;

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
