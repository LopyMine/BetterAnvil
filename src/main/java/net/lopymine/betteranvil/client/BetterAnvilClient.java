package net.lopymine.betteranvil.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.TooltipComponentCallback;

import net.lopymine.betteranvil.gui.tooltip.utils.*;

public class BetterAnvilClient implements ClientModInitializer {

    /**
     * Runs the mod initializer on the client environment.
     */

    @Override
    public void onInitializeClient() {
        TooltipComponentCallback.EVENT.register(data -> {
            if (data instanceof TooltipComponentsData tooltipData) {
                return new TooltipComponents(tooltipData.list());
            }
            return null;
        });
    }
}
