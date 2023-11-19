package net.lopymine.betteranvil.utils.mixins;

import net.minecraft.resource.*;

import java.io.File;

public interface ResourcePackAccessor {
    static ResourcePackAccessor of(String name, File file, boolean isZip, boolean isServer) {
        return new ResourcePackAccessor() {
            @Override
            public File betterAnvil$getFile() {
                return file;
            }

            @Override
            public ResourcePack betterAnvil$getResourcePack() {
                return new ZipResourcePack(name, file, false);
            }

            @Override
            public boolean betterAnvil$isZip() {
                return isZip;
            }

            @Override
            public boolean betterAnvil$isServer() {
                return isServer;
            }
        };
    }

    File betterAnvil$getFile();

    ResourcePack betterAnvil$getResourcePack();

    boolean betterAnvil$isZip();

    boolean betterAnvil$isServer();
}
