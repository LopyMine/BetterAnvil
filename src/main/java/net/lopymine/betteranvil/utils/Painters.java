package net.lopymine.betteranvil.utils;

import net.minecraft.util.Identifier;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;

import net.lopymine.betteranvil.BetterAnvil;

import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.createLightDarkVariants;
import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.createNinePatch;

public class Painters {
    public static final BackgroundPainter CONFIG_PAINTER = createLightDarkVariants(
            createNinePatch(new Identifier(BetterAnvil.MOD_ID, "gui/background_painters/list_background_light.png")),
            createNinePatch(new Identifier(BetterAnvil.MOD_ID, "gui/background_painters/list_background_dark.png"))
    );

    public static final BackgroundPainter BACKGROUND_PAINTER = createLightDarkVariants(
            createNinePatch(new Identifier(BetterAnvil.MOD_ID, "gui/background_painters/panel_light.png")),
            createNinePatch(new Identifier(BetterAnvil.MOD_ID, "gui/background_painters/panel_dark.png"))
    );

    public static final Identifier SEARCH = new Identifier(BetterAnvil.MOD_ID, "gui/sprites/search.png");
    public static final Identifier SEARCH_HOVERED = new Identifier(BetterAnvil.MOD_ID, "gui/sprites/search_hovered.png");
}
