package net.lopymine.betteranvil.mixin;

import net.lopymine.betteranvil.cit.ConfigParser;
import net.lopymine.betteranvil.cit.writers.ServerWriter;
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
        ConfigParser.setServerResourcePack(packZip.getName());
        if(ServerWriter.hasCitZipFolder(packZip)){
            ServerWriter.writeConfig(packZip);
        } else {
            ConfigParser.setServerResourcePack(null);
            MYLOGGER.info("This server resource pack does not have a cit folder");
        }
    }
}