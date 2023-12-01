package net.lopymine.betteranvil.gui.widgets.fields;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;

import net.lopymine.betteranvil.BetterAnvil;

public class WField extends WWidget {
    private Text text = Text.of("");
    private String string = "";

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    @Override
    public void setSize(int x, int y) {
        super.setSize(164, 24);
    }

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
        int yOffset = 24 / 2 - 9 / 2;

        State state = State.getState(shouldRenderInDarkMode());

        float px = 1 / 128f;

        float buttonLeft = 0;
        float buttonTop = (string.isEmpty() ? 16 : 0) * px;
        float buttonHeight = 16 * px;
        float buttonWidth = 110 * px;
        ScreenDrawing.texturedRect(context, x, y, 164, 24, state.getTexture(), buttonLeft, buttonTop, buttonLeft + buttonWidth, buttonTop + buttonHeight, 0xFFFFFFFF);

        ScreenDrawing.drawString(context, text.asOrderedText(), HorizontalAlignment.CENTER, x, y + yOffset, 164, 0xE0E0E0);
    }

    public String getText() {
        return string;
    }

    public WField setText(String s) {
        this.text = cutString(s);
        this.string = s;
        return this;
    }

    private Text cutString(String text) {
        String trimmedText = MinecraftClient.getInstance().textRenderer.trimToWidth(text, 150);
        if (!text.equals(trimmedText)) trimmedText = trimmedText + "...";

        return Text.of(trimmedText);
    }

    @Override
    public int getWidth() {
        return 164;
    }

    @Override
    public int getHeight() {
        return 24;
    }

    private enum State {
        LIGHT(BetterAnvil.i("textures/gui/field/field_light.png")),
        DARK(BetterAnvil.i("textures/gui/field/field_dark.png"));

        private final Identifier texture;

        State(Identifier texture) {
            this.texture = texture;
        }

        public static State getState(boolean isDarkMode) {
            return (isDarkMode ? DARK : LIGHT);
        }

        public Identifier getTexture() {
            return texture;
        }
    }
}
