package net.lopymine.betteranvil.mixin;

import net.minecraft.resource.*;
import org.spongepowered.asm.mixin.*;

import net.lopymine.betteranvil.utils.mixins.ResourcePackAccessor;

import java.io.File;
import java.nio.file.Path;

@Mixin(DirectoryResourcePack.class)
public class ResourcePackMixin implements ResourcePackAccessor {

    @Shadow
    @Final
    private Path root;

    @Override
    public File betterAnvil$getFile() {
        return root.toFile();
    }

    @Override
    public ResourcePack betterAnvil$getResourcePack() {
        return ((ResourcePack) this);
    }

    @Override
    public boolean betterAnvil$isZip() {
        return false;
    }

    @Override
    public boolean betterAnvil$isServer() {
        return false;
    }
}
