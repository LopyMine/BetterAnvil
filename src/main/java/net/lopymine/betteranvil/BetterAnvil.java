package net.lopymine.betteranvil;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackManager;
import org.slf4j.Logger;


public class BetterAnvil implements ModInitializer {
    public static final String MOD_ID = "betteranvil";
    public static final Logger MYLOGGER = LogUtils.getLogger();
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    private static final ResourcePackManager rpManager = mc.getResourcePackManager();
    private String server = "";
    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        MYLOGGER.info("Better Anvil Initialize");

    }

    public static MinecraftClient getMC() {
        return mc;
    }

    public static ResourcePackManager getRM() {
        return rpManager;
    }

    public void setServer(String server) {
        this.server = server;
    }

    public String getServer() {
        return server;
    }
}
