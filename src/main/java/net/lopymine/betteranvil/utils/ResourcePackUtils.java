package net.lopymine.betteranvil.utils;

import com.google.common.hash.Hashing;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.*;
import net.minecraft.resource.*;
import net.minecraft.util.*;

import net.lopymine.betteranvil.config.BetterAnvilConfig;
import net.lopymine.betteranvil.config.resourcepacks.ResourcePackConfigsManager;
import net.lopymine.betteranvil.resourcepacks.*;
import net.lopymine.betteranvil.utils.mixins.ResourcePackAccessor;

import java.io.*;
import java.util.*;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;

import static net.lopymine.betteranvil.BetterAnvil.LOGGER;
import static net.lopymine.betteranvil.config.resourcepacks.ResourcePackConfigsManager.*;

public class ResourcePackUtils {

    private static final Identifier UNKNOWN_PACK = new Identifier("minecraft", "textures/misc/unknown_pack.png");

    public static Set<ResourcePackType> getTypes(ResourcePackAccessor accessor) {
        Set<ResourcePackType> types = new HashSet<>();

        for (ResourcePackType type : ResourcePackType.values()) {
            boolean bl;
            boolean isServer = accessor.betterAnvil$isServer();

            String path = getConfigFolderPath(type);
            File file = accessor.betterAnvil$getFile();

            if (isServer) {
                bl = ResourcePackConfigsManager.hasZipPath(file, path);
            } else {
                bl = ResourcePackConfigsManager.hasPath(file, path);
            }

            if (bl) {
                types.add(type);
            }
        }

        return types;
    }

    public static List<ParsableResourcePack> convertList(List<ResourcePack> packs, BetterAnvilConfig config) {
        List<ParsableResourcePack> parsableResourcePacks = new ArrayList<>();

        for (ResourcePack resourcePack : packs) {
            ParsableResourcePack parsableResourcePack = convertResourcePack(resourcePack, config);
            if (parsableResourcePack == null) continue;

            parsableResourcePacks.add(parsableResourcePack);
        }

        return parsableResourcePacks;
    }

    @Nullable
    public static ParsableResourcePack convertResourcePack(ResourcePack resourcePack, BetterAnvilConfig config) {
        if (!(resourcePack instanceof ResourcePackAccessor accessor)) return null;
        Set<ResourcePackType> types = getTypes(accessor);

        if (types.isEmpty()) return null;
        if (!config.enabledCMD) {
            types.remove(ResourcePackType.CMD);
        }
        if (!config.enabledCIT) {
            types.remove(ResourcePackType.CIT);
        }
        return new ParsableResourcePack(accessor, types);
    }

    @Nullable
    public static ParsableResourcePack convertResourcePack(ResourcePackAccessor accessor, BetterAnvilConfig config) {
        Set<ResourcePackType> types = getTypes(accessor);

        if (types.isEmpty()) {
            return null;
        }
        if (!config.enabledCMD) {
            types.remove(ResourcePackType.CMD);
        }
        if (!config.enabledCIT) {
            types.remove(ResourcePackType.CIT);
        }
        return new ParsableResourcePack(accessor, types);
    }

    public static LinkedHashSet<String> getStringResourcePacksWithServer() {
        return new LinkedHashSet<>(MinecraftClient.getInstance()
                .getResourcePackManager()
                .getEnabledNames()
                .stream()
                .filter(pack -> {
                    if (pack.equals("server")) {
                        return true;
                    }
                    return pack.startsWith("file/");
                })
                .flatMap(s -> Stream.of(s.replaceAll("file/", "").replaceAll(".zip", "")))
                .toList());
    }

    public static LinkedHashSet<String> getResourcePacksByType(ResourcePackType resourcePackType) {
        return new LinkedHashSet<>(MinecraftClient.getInstance()
                .getResourcePackManager()
                .getEnabledNames()
                .stream()
                .filter(pack -> {
                    if (pack.equals("server")) {
                        return true;
                    }
                    if (!pack.startsWith("file/")) {
                        return false;
                    }
                    String path = ResourcePackConfigsManager.getConfigPath(resourcePackType) + getResourcePackName(pack) + JSON_FORMAT;
                    return hasConfig(path);
                })
                .toList());
    }

    public static Identifier getPackIcon(ResourcePackProfile resourcePackProfile) {
        TextureManager textureManager = MinecraftClient.getInstance().getTextureManager();
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
                    InputStream inputStream = inputSupplier.get();

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
            LOGGER.warn("Failed to load icon from pack {}", resourcePackProfile.getName(), var14);
            return UNKNOWN_PACK;
        }
    }

    public static String getResourcePackName(ResourcePackProfile profile) {
        return profile.getName().replaceAll(".zip", "").replaceAll("file/", "");
    }

    public static String getResourcePackNameWithZip(ResourcePackProfile profile) {
        return profile.getName().replaceAll("file/", "");
    }

    public static String getResourcePackName(String name) {
        return name.replaceAll(".zip", "").replaceAll("file/", "");
    }

    public static String getResourcePackNameWithZip(String name) {
        return name.replaceAll("file/", "");
    }
}


