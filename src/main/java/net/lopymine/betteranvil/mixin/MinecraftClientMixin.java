package net.lopymine.betteranvil.mixin;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import net.lopymine.betteranvil.resourcepacks.PackManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(at = @At("HEAD"), method = "disconnect()V")
    private void screen1(CallbackInfo ci) {
        PackManager.setServerResourcePack(null);
    }
    @Inject(at = @At("HEAD"), method = "disconnect(Lnet/minecraft/client/gui/screen/Screen;)V")
    private void screen2(Screen screen, CallbackInfo ci) {
        PackManager.setServerResourcePack(null);
    }
}
