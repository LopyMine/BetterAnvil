package net.lopymine.betteranvil.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs.QuickPlay;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.realms.RealmsClient;
import net.minecraft.resource.ResourceReload;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fabricmc.fabric.api.entity.event.v1.ServerPlayerEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.fake.*;
import net.lopymine.betteranvil.resourcepacks.ServerResourcePackManager;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(at = @At("HEAD"), method = "disconnect()V")
    private void disconnect(CallbackInfo ci) {
        ServerResourcePackManager.setServer("");
    }

    @Inject(at = @At("HEAD"), method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V")
    private void disconnect1(Screen screen, CallbackInfo ci) {
        ServerResourcePackManager.setServer("");
    }

    @Inject(at = @At("HEAD"), method = "onInitFinished")
    private void onInitFinished(RealmsClient realms, ResourceReload reload, QuickPlay quickPlay, CallbackInfo ci) {
        Thread thread = new Thread(()-> {
            FakeClientPlayNetworkHandler.onInitialize();
            FakeWorld.onInitialize();
            FakeClientPlayerEntity.onInitialize();
            BetterAnvil.LOGGER.info("FakeClientPlayNetworkHandler, FakeWorld,  FakeClientPlayerEntity Initialized");
        }, "FakeLoader");
        thread.start();
    }
}
