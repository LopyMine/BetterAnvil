package net.lopymine.betteranvil;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;


public class BetterAnvil implements ModInitializer {
    public static final String MOD_ID = "betteranvil";
    public static final Logger MYLOGGER = LogUtils.getLogger();
    /**
     * Runs the mod initializer.
     */
    @Override
    public void onInitialize() {
        MYLOGGER.info("Better Anvil Initialize");
    }
}
