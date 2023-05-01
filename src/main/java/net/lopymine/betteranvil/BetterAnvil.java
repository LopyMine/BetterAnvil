package net.lopymine.betteranvil;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.resource.ResourcePackManager;
import org.slf4j.Logger;


public class BetterAnvil implements ModInitializer {
    public static final String MOD_ID = "betteranvil";
    public static final Logger MYLOGGER = LogUtils.getLogger();

    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static final ResourcePackManager rpManager = mc.getResourcePackManager();
    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        MYLOGGER.info("Better Anvil Initialize");
    }

    public static MinecraftClient getMinecraftInstance() {
        return mc;
    }

    public static ResourcePackManager getResourcePackManager() {
        return rpManager;
    }
}
