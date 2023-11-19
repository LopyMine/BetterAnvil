package net.lopymine.betteranvil.mixin;

import net.minecraft.client.resource.ServerResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.config.BetterAnvilConfig;
import net.lopymine.betteranvil.resourcepacks.*;
import net.lopymine.betteranvil.tasks.*;
import net.lopymine.betteranvil.utils.ResourcePackUtils;
import net.lopymine.betteranvil.utils.mixins.ResourcePackAccessor;

import java.io.File;
import java.util.concurrent.CompletableFuture;

@Mixin(ServerResourcePackProvider.class)
public class LoadServerResourcePackMixin {

    @Inject(at = @At("TAIL"), method = "loadServerPack(Ljava/io/File;Lnet/minecraft/resource/ResourcePackSource;)Ljava/util/concurrent/CompletableFuture;")
    private void loadServerPack(File packZip, ResourcePackSource packSource, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        BetterAnvil.LOGGER.info("Start writing server resource pack [{}]", packZip.getName());
        ServerResourcePackManager.setServer(packZip.getName());

        ResourcePackAccessor resourcePackAccessor = ResourcePackAccessor.of("server", packZip, true, true);
        ParsableResourcePack parsableResourcePack = ResourcePackUtils.convertResourcePack(resourcePackAccessor, BetterAnvilConfig.getInstance());
        if (parsableResourcePack == null) {
            return;
        }

        ResourcePackTaskManager.runTask(new ResourcePackTask(parsableResourcePack));
    }
}
