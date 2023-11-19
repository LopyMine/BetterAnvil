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
    public static final Identifier FIELD_NAME_FOCUS_DARK = new Identifier(BetterAnvil.MOD_ID, "gui/name_field/name_field_focus_dark.png");
    public static final Identifier FIELD_NAME_DARK = new Identifier(BetterAnvil.MOD_ID, "gui/name_field/name_field_dark.png");
    public static final Identifier FIELD_NAME_FOCUS = new Identifier(BetterAnvil.MOD_ID, "gui/name_field/name_field_focus_light.png");
    public static final Identifier FIELD_NAME = new Identifier(BetterAnvil.MOD_ID, "gui/name_field/name_field_light.png");

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
        boolean bl = shouldRenderInDarkMode();

        int yOffset = height / 2 - 9 / 2;

        if (!string.isEmpty()) {
            ScreenDrawing.texturedRect(context, x, y, 164, 24, bl ? FIELD_NAME_FOCUS_DARK : FIELD_NAME_FOCUS, 0xFFFFFFFF);
        } else {
            ScreenDrawing.texturedRect(context, x, y, 164, 24, bl ? FIELD_NAME_DARK : FIELD_NAME, 0xFFFFFFFF);
        }

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
}
