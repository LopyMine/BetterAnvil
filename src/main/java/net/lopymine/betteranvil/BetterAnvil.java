package net.lopymine.betteranvil;

import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;

import net.lopymine.betteranvil.config.resourcepacks.cit.CITFavoriteConfigManager;

public class BetterAnvil implements ModInitializer {
    public static final String MOD_ID = "betteranvil";
    public static final Logger LOGGER = LogUtils.getLogger();

    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        LOGGER.info("Better Anvil Initialize");
    }
}
