package net.lopymine.betteranvil.client;

import net.fabricmc.api.ClientModInitializer;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;

public class BetterAnvilClient implements ClientModInitializer {

    /**
     * Runs the mod initializer on the client environment.
     */
    @Override
    public void onInitializeClient() {
        MYLOGGER.info("Better Anvil Client Initialize");
    }
}