package net.lopymine.betteranvil.utils.mixins;

import net.minecraft.client.gui.screen.pack.PackScreen;

@FunctionalInterface
public interface PackListWidgetAccessor {
    PackScreen betterAnvil$getPackScreen();
}
