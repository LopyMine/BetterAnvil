package net.lopymine.betteranvil;

import net.minecraft.util.Identifier;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;

public class BetterAnvil implements ModInitializer {
    public static final String MOD_ID = "betteranvil";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static Identifier i(String path) {
        return new Identifier(MOD_ID, path);
    }

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        LOGGER.info("Better Anvil Initialized");
    }
}
