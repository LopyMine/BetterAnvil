package net.lopymine.betteranvil.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

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
