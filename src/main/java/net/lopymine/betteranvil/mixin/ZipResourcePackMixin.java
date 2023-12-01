package net.lopymine.betteranvil.mixin;

import net.minecraft.resource.*;
import net.minecraft.resource.ZipResourcePack.ZipFileWrapper;
import org.spongepowered.asm.mixin.*;

import net.lopymine.betteranvil.utils.mixins.ResourcePackAccessor;

import java.io.File;

@Mixin(ZipResourcePack.class)
public class ZipResourcePackMixin implements ResourcePackAccessor {

    @Shadow @Final private ZipFileWrapper zipFile;

    @Override
    public File betterAnvil$getFile() {
        return zipFile.file;
    }

    @Override
    public ResourcePack betterAnvil$getResourcePack() {
        return ((ResourcePack) this);
    }

    @Override
    public boolean betterAnvil$isZip() {
        return true;
    }

    @Override
    public boolean betterAnvil$isServer() {
        return false;
    }
}
