package net.lopymine.betteranvil.mixin;

import net.minecraft.resource.*;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.lopymine.betteranvil.config.BetterAnvilConfig;
import net.lopymine.betteranvil.config.enums.Overwriting;
import net.lopymine.betteranvil.resourcepacks.ResourcePackManager;

import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Stream;

@Mixin(ReloadableResourceManagerImpl.class)
public class ReloadableResourceManagerImplMixin {
    @Inject(at = @At("HEAD"), method = "reload")
    private void scan(Executor prepareExecutor, Executor applyExecutor, CompletableFuture<Unit> initialStage, List<ResourcePack> packs, CallbackInfoReturnable<ResourceReload> cir) {
        BetterAnvilConfig config = BetterAnvilConfig.getInstance();

        if (config.overwritingEnum != Overwriting.RELOAD) {
            return;
        }

        List<ResourcePack> resourcePacks = packs.stream().filter(resourcePack -> resourcePack.getName().startsWith("file/")).toList();
        ResourcePackManager.startWriting(resourcePacks, config);
    }
}
