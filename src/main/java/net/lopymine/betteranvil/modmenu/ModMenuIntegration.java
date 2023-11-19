package net.lopymine.betteranvil.modmenu;

import com.terraformersmc.modmenu.api.*;

import net.fabricmc.loader.api.FabricLoader;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;

import net.lopymine.betteranvil.gui.NoClothConfigGui;

public class ModMenuIntegration implements ModMenuApi {

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if (FabricLoader.getInstance().isModLoaded("cloth-config")) {
            return ModMenuIntegrationScreen::createScreen;
        } else {
            return screen -> new CottonClientScreen(new NoClothConfigGui(screen));
        }
    }
}
