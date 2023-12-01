package net.lopymine.betteranvil.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

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
    private void onInitFinished(CallbackInfoReturnable<Runnable> cir) {
        Thread thread = new Thread(() -> {
            FakeWorld.onInitialize();
            FakeClientPlayerEntity.onInitialize();
            BetterAnvil.LOGGER.info("FakeClientPlayNetworkHandler, FakeWorld,  FakeClientPlayerEntity Initialized");
        }, "FakeLoader");
        thread.start();
    }
}
