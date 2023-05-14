package net.lopymine.betteranvil;

import com.mojang.logging.LogUtils;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.GameMenuScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.screen.option.OptionsScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.report.ReporterEnvironment;
import net.minecraft.client.resource.ServerResourcePackProvider;
import net.minecraft.command.CommandSource;
import net.minecraft.command.argument.NbtCompoundArgumentType;
import net.minecraft.datafixer.fix.ItemNbtFix;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.visitor.NbtElementVisitor;
import net.minecraft.network.listener.ServerPacketListener;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerNetworkIo;
import net.minecraft.server.command.ItemCommand;
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
