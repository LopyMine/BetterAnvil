package net.lopymine.betteranvil.resourcepacks;

import com.google.common.hash.Hashing;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.InputSupplier;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.concurrent.atomic.AtomicReference;

import static net.lopymine.betteranvil.BetterAnvil.*;
import static net.lopymine.betteranvil.resourcepacks.ConfigManager.*;

public class PackManager {

    private static final AtomicReference<String> serverResourcePack = new AtomicReference<>();
    private static final Identifier UNKNOWN_PACK = new Identifier("minecraft", "textures/misc/unknown_pack.png");
    private static final ResourcePackManager manager = MinecraftClient.getInstance().getResourcePackManager();

    public static LinkedHashSet<String> getPackNamesWithServer() {
        Collection<String> resourcePackCollection = manager.getEnabledNames();
        LinkedHashSet<String> newNames = new LinkedHashSet<>();

        for (String name : resourcePackCollection) {
            if (name.equals("server")) {
                newNames.add(name);
            }
            if (name.startsWith("file/")) {
                newNames.add(name.replaceAll("file/", "").replaceAll(".zip", ""));
                continue;
            }
        }
        return newNames;
    }

    public static LinkedHashSet<ResourcePackProfile> getPacksProfiles() {
        LinkedHashSet<ResourcePackProfile> resourcePackProfiles = new LinkedHashSet<>();

        for (ResourcePackProfile rp : manager.getEnabledProfiles()) {
            if (rp.getName().startsWith("file/")) {
                resourcePackProfiles.add(rp);
            }
            if (rp.getName().equals("server")) {
                resourcePackProfiles.add(rp);
            }
        }
        return resourcePackProfiles;
    }

    public static LinkedHashSet<String> getPacks() {
        Collection<String> resourcePackCollection = manager.getEnabledNames();
        LinkedHashSet<String> newNames = new LinkedHashSet<>();

        for (String name : resourcePackCollection) {
            if (name.startsWith("file/")) {
                newNames.add(name.replaceAll("file/", ""));
            }
        }
        return newNames;
    }


    public static AtomicReference<String> getServerResourcePack() {
        return serverResourcePack;
    }

    public static void setServerResourcePack(String serverResourcePackk) {
        serverResourcePack.set(serverResourcePackk);
    }


    public static Identifier getPackIcon(ResourcePackProfile resourcePackProfile) {
        TextureManager textureManager = getMC().getTextureManager();
        try {
            ResourcePack resourcePack = resourcePackProfile.createResourcePack();

            Identifier var15;
            label70:
            {
                Identifier var9;
                try {
                    InputSupplier<InputStream> inputSupplier = resourcePack.openRoot("pack.png");
                    if (inputSupplier == null) {
                        var15 = UNKNOWN_PACK;
                        break label70;
                    }

                    String string = resourcePackProfile.getName();
                    String var10003 = Util.replaceInvalidChars(string, Identifier::isPathCharacterValid);
                    Identifier identifier = new Identifier("minecraft", "pack/" + var10003 + "/" + Hashing.sha1().hashUnencodedChars(string) + "/icon");
                    InputStream inputStream = (InputStream) inputSupplier.get();

                    try {
                        NativeImage nativeImage = NativeImage.read(inputStream);
                        textureManager.registerTexture(identifier, new NativeImageBackedTexture(nativeImage));
                        var9 = identifier;
                    } catch (Throwable var12) {
                        if (inputStream != null) {
                            try {
                                inputStream.close();
                            } catch (Throwable var11) {
                                var12.addSuppressed(var11);
                            }
                        }

                        throw var12;
                    }

                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Throwable var13) {
                    if (resourcePack != null) {
                        try {
                            resourcePack.close();
                        } catch (Throwable var10) {
                            var13.addSuppressed(var10);
                        }
                    }

                    throw var13;
                }

                if (resourcePack != null) {
                    resourcePack.close();
                }

                return var9;
            }

            if (resourcePack != null) {
                resourcePack.close();
            }

            return var15;
        } catch (Exception var14) {
            MYLOGGER.warn("Failed to load icon from pack {}", resourcePackProfile.getName(), var14);
            return UNKNOWN_PACK;
        }
    }

    public static LinkedHashSet<String> getPackNamesWithCEMConfig() {
        LinkedHashSet<String> newNames = new LinkedHashSet<>();

        for (String name : getPackNamesWithServer()) {
            if (name.equals("server")) {
                newNames.add(name);
            }
            if (hasConfig(pathToCEMConfigFolder + name + jsonFormat)) {
                newNames.add(name);
            }
        }
        return newNames;
    }

    public static LinkedHashSet<String> getPackNamesWithCITConfig() {
        LinkedHashSet<String> newNames = new LinkedHashSet<>();

        for (String name : getPackNamesWithServer()) {
            if (name.equals("server")) {
                newNames.add(name);
            }
            if (hasConfig(pathToCITConfigFolder + name + jsonFormat)) {
                newNames.add(name);
            }
        }
        return newNames;
    }

    public static LinkedHashSet<String> getPackNamesWithCMDConfig() {
        LinkedHashSet<String> newNames = new LinkedHashSet<>();

        for (String name : getPackNamesWithServer()) {
            if (name.equals("server")) {
                newNames.add(name);
            }
            if (hasConfig(pathToCMDConfigFolder + name + jsonFormat)) {
                newNames.add(name);
            }
        }
        return newNames;
    }

}
