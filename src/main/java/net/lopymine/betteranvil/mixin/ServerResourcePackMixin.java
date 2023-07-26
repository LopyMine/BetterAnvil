package net.lopymine.betteranvil.mixin;

import net.lopymine.betteranvil.resourcepacks.ConfigManager;
import net.lopymine.betteranvil.resourcepacks.PackManager;
import net.lopymine.betteranvil.resourcepacks.cem.writers.CEMWriter;
import net.lopymine.betteranvil.resourcepacks.cit.writers.CITWriter;
import net.lopymine.betteranvil.resourcepacks.cmd.writers.CMDWriter;
import net.minecraft.client.resource.ClientBuiltinResourcePackProvider;
import net.minecraft.resource.ResourcePackSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.concurrent.CompletableFuture;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;

@Mixin(ClientBuiltinResourcePackProvider.class)
public class ServerResourcePackMixin {

    @Inject(at = @At("RETURN"), method = "loadServerPack(Ljava/io/File;Lnet/minecraft/resource/ResourcePackSource;)Ljava/util/concurrent/CompletableFuture;")
    private void init(File packZip, ResourcePackSource packSource, CallbackInfoReturnable<CompletableFuture<Void>> cir) {
        PackManager.setServerResourcePack(packZip.getName());

        if (ConfigManager.hasZipCITFolder(packZip.toPath())) {
            CITWriter.writeConfig(packZip, true, true);
            return;
        } else {
            PackManager.setServerResourcePack(null);
            MYLOGGER.info("This server resource pack does not have a cit folder");
        }

        if(ConfigManager.hasZipCMDFolder(packZip.toPath())){
            PackManager.setServerResourcePack(packZip.getName());
            CMDWriter.writeConfig(packZip, true, true);
        }  else {
            PackManager.setServerResourcePack(null);
            MYLOGGER.info("This server resource pack does not have a CMD folder");
        }

        //if(ConfigManager.hasZipCEMFolder(packZip.toPath())){
        //    PackManager.setServerResourcePack(packZip.getName());
        //    CEMWriter.writeConfig(packZip, true, true);
        //}  else {
        //    PackManager.setServerResourcePack(null);
        //    MYLOGGER.info("This server resource pack does not have a CEM folder");
        //}

    }
}
