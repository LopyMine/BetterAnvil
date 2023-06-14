package net.lopymine.betteranvil.resourcepacks;

import com.google.common.hash.Hashing;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.resource.Resource;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import static net.lopymine.betteranvil.BetterAnvil.*;
import static net.lopymine.betteranvil.resourcepacks.ConfigManager.*;

public class PackManager {

    private static final AtomicReference<String> serverResourcePack = new AtomicReference<>();
    private static final Identifier UNKNOWN_PACK = new Identifier("minecraft", "textures/misc/unknown_pack.png");
    private static final ResourcePackManager manager = MinecraftClient.getInstance().getResourcePackManager();

    public static ArrayList<String> getPackNamesWithServer() {
        Collection<String> resourcePackCollection = manager.getEnabledNames();
        ArrayList<String> newNames = new ArrayList<>();

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

    public static ArrayList<ResourcePackProfile> getPacksProfiles() {
        ArrayList<ResourcePackProfile> resourcePackProfiles = new ArrayList<>();

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

    public static ArrayList<String> getPacks() {
        Collection<String> resourcePackCollection = manager.getEnabledNames();
        ArrayList<String> newNames = new ArrayList<>();

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
        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
        try {
            ResourcePack resourcePack = resourcePackProfile.createResourcePack();

            Identifier var5;
            label84: {
                Identifier var8;
                try {
                    InputStream inputStream = resourcePack.openRoot("pack.png");

                    label86: {
                        try {
                            if (inputStream != null) {
                                String string = resourcePackProfile.getName();
                                String var10003 = Util.replaceInvalidChars(string, Identifier::isPathCharacterValid);
                                Identifier identifier = new Identifier("minecraft", "pack/" + var10003 + "/" + Hashing.sha1().hashUnencodedChars(string) + "/icon");
                                NativeImage nativeImage = NativeImage.read(inputStream);
                                textureManager.registerTexture(identifier, new NativeImageBackedTexture(nativeImage));
                                var8 = identifier;
                                break label86;
                            }

                            var5 = UNKNOWN_PACK;
                        } catch (Throwable var11) {
                            if (inputStream != null) {
                                try {
                                    inputStream.close();
                                } catch (Throwable var10) {
                                    var11.addSuppressed(var10);
                                }
                            }

                            throw var11;
                        }

                        if (inputStream != null) {
                            inputStream.close();
                        }
                        break label84;
                    }

                    if (inputStream != null) {
                        inputStream.close();
                    }
                } catch (Throwable var12) {
                    if (resourcePack != null) {
                        try {
                            resourcePack.close();
                        } catch (Throwable var9) {
                            var12.addSuppressed(var9);
                        }
                    }

                    throw var12;
                }

                if (resourcePack != null) {
                    resourcePack.close();
                }

                return var8;
            }

            if (resourcePack != null) {
                resourcePack.close();
            }

            return var5;
        } catch (FileNotFoundException var13) {
        } catch (Exception var14) {
            MYLOGGER.warn("Failed to load icon from pack {}", resourcePackProfile.getName(), var14);
        }

        return UNKNOWN_PACK;
    }

    public static ArrayList<String> getPackNamesWithCITConfig() {
        ArrayList<String> newNames = new ArrayList<>();

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

    public static ArrayList<String> getPackNamesWithCMDConfig() {
        ArrayList<String> newNames = new ArrayList<>();

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
