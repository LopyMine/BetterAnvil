package net.lopymine.betteranvil.mixin;

import net.lopymine.betteranvil.modmenu.BetterAnvilConfigManager;
import net.lopymine.betteranvil.modmenu.enums.ResourcePackJsonWriting;
import net.lopymine.betteranvil.resourcepacks.ConfigManager;
import net.minecraft.resource.ReloadableResourceManagerImpl;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourceReload;
import net.minecraft.util.Unit;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;

@Mixin(ReloadableResourceManagerImpl.class)
public class ReloadableResourceManagerImplMixin {

    @Inject(at = @At("HEAD"), method = "reload")
    private void scan(Executor prepareExecutor, Executor applyExecutor, CompletableFuture<Unit> initialStage, List<ResourcePack> packs, CallbackInfoReturnable<ResourceReload> cir){
        if (Objects.requireNonNull(BetterAnvilConfigManager.INSTANCE.WHEN_OVERWRITE) == ResourcePackJsonWriting.LAUNCH) {
            MYLOGGER.info("Writing resource packs to json config...");
            ConfigManager.writeCITItems();
            ConfigManager.writeCEMItems();
            if(BetterAnvilConfigManager.INSTANCE.CUSTOM_MODEL_DATA_SUPPORT){
                ConfigManager.writeCMDItems();
            }
        }
    }

}
