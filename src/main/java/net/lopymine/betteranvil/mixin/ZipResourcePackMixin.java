package net.lopymine.betteranvil.mixin;

import net.minecraft.resource.*;
import org.spongepowered.asm.mixin.*;

import net.lopymine.betteranvil.utils.mixins.ResourcePackAccessor;

import java.io.File;

@Mixin(ZipResourcePack.class)
public class ZipResourcePackMixin implements ResourcePackAccessor {

    @Shadow
    @Final
    private File backingZipFile;

    @Override
    public File betterAnvil$getFile() {
        return backingZipFile;
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
