package net.lopymine.betteranvil.utils;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

import io.github.cottonmc.cotton.gui.client.*;

import net.lopymine.betteranvil.BetterAnvil;

import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.createLightDarkVariants;
import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.createNinePatch;

public class Painters {
    public static final BackgroundPainter CONFIG_PAINTER = createLightDarkVariants(
            createNinePatch(BetterAnvil.i("textures/gui/painters/list_background_light.png")),
            createNinePatch(BetterAnvil.i("textures/gui/painters/list_background_dark.png"))
    );

    public static final BackgroundPainter BACKGROUND_PAINTER = createLightDarkVariants(
            createNinePatch(BetterAnvil.i("textures/gui/painters/panel_light.png")),
            createNinePatch(BetterAnvil.i("textures/gui/painters/panel_dark.png"))
    );

    private static final Identifier SEARCH = BetterAnvil.i("textures/gui/buttons/search.png");

    public static void drawSearch(DrawContext context, int x, int y, boolean isHovered) {
        float px = 1 / 24f;

        float buttonLeft = 0;
        float buttonTop = (isHovered ? 12 : 0) * px;
        float buttonWidth = 12 * px;
        ScreenDrawing.texturedRect(context, x, y, 12, 12, SEARCH, buttonLeft, buttonTop, buttonLeft + buttonWidth, buttonTop + buttonWidth, 0xFFFFFFFF);
    }
}
