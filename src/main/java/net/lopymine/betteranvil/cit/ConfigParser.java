package net.lopymine.betteranvil.cit;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;
import static net.lopymine.betteranvil.BetterAnvil.getResourcePackManager;

public class ConfigParser {

    public static final String pathToConfigFolder = "config/betteranvil/resourcepacks/";

    public static final String pathToConfig = "config/betteranvil/";
    public static final String jsonFormat = ".json";
    public static final String pathToCitFolder = "/assets/minecraft/optifine/cit/";
    public static final String pathToResourcePacks = "resourcepacks/";

    private static final Identifier UNKNOWN_PACK = new Identifier("minecraft:textures/misc/unknown_pack.png");

    public static ArrayList<String> parseItemNames(ItemStack item) {
        ArrayList<String> itemNames = new ArrayList<>();

        Gson gson = new Gson();

        String itemName = item.getItem().getTranslationKey().replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");

        ArrayList<String> rpNames = getClearResourcePackNames();

        for (String rpName : rpNames) {
            try (FileReader reader = new FileReader(pathToConfigFolder + rpName + jsonFormat)) {

                CitCollection citCollection = gson.fromJson(reader, CitCollection.class);
                reader.close();
                //System.out.println(getNames(myOb.getCitItemsCollection(), itemName));
                itemNames.addAll(getItemNames(citCollection.getCitItemsCollection(), itemName));
                } catch (IOException ignored) {
            }
        }

        return itemNames;
    }

    public static ArrayList<String> getClearResourcePackNames() {
        Collection<String> resourcePackCollection = getResourcePackManager().getEnabledNames();
        ArrayList<String> newNames = new ArrayList<>();
        for (String name : resourcePackCollection.stream().toList()) {
            if (name.startsWith("file/")) {
                newNames.add(name.replaceAll("file/", "").replaceAll(".zip", ""));
                continue;
            }
        }
        return newNames;
    }

    public static ArrayList<String> getResourcePackNames() {
        Collection<String> resourcePackCollection = getResourcePackManager().getEnabledNames();
        ArrayList<String> newNames = new ArrayList<>();
        for (String name : resourcePackCollection.stream().toList()) {
            if (name.startsWith("file/")) {
                newNames.add(name.replaceAll("file/", ""));
                continue;
            }
        }
        return newNames;
    }

    public static ArrayList<ResourcePackProfile> getResourcePackProfiles(MinecraftClient mc) {
        ResourcePackManager resourcePackManager = mc.getResourcePackManager();
        Collection<ResourcePackProfile> resourcePackCollection = resourcePackManager.getEnabledProfiles();

        ArrayList<ResourcePackProfile> resourcePackProfiles = new ArrayList<>();

        for (ResourcePackProfile rp : resourcePackCollection.stream().toList()) {
            if (rp.getName().startsWith("file/")) {
                resourcePackProfiles.add(rp);
                continue;
            }
        }
        return resourcePackProfiles;
    }

    public static ArrayList<String> parseItemsFromConfig(String configFileName, ItemStack anvilItem, String path) {
        ArrayList<String> items = new ArrayList<>();

        Gson gson = new Gson();

        String itemName = anvilItem.getItem().getTranslationKey().replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");

        if(!hasFile((path + configFileName + jsonFormat))){
            return items;
        }
        try (FileReader reader = new FileReader(path + configFileName + jsonFormat)) {

            CitCollection citCollection = gson.fromJson(reader, CitCollection.class);
            reader.close();

            items.addAll(getItemNames(citCollection.getCitItemsCollection(), itemName));
        } catch (IOException e) {
            e.printStackTrace();
            return items;
        }
        return items;
    }

    private static ArrayList<String> getItemNames(Collection<CitItems> citItemsCollection, String item) {
        ArrayList<String> customNames = new ArrayList<>();

        for (CitItems citItem : citItemsCollection) {
            if (!(citItem.getCustomName() == null) && !(citItem.getItems() == null)) {
                for (String name : citItem.getCustomNames()) {
                    if (citItem.getItems().contains(item)) {
                        customNames.add(name);
                    }
                }
            }
        }
        Set<String> noDuplicateCustomNames = new LinkedHashSet<>(customNames);
        return new ArrayList<>(noDuplicateCustomNames);
    }

    public static Identifier loadPackIcon(TextureManager textureManager, ResourcePackProfile resourcePackProfile) {
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
                            try {
                                inputStream.close();
                            } catch (Throwable var10) {
                                var11.addSuppressed(var10);
                            }

                            throw var11;
                        }

                        break label84;
                    }

                    inputStream.close();
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

                resourcePack.close();

                return var8;
            }

            resourcePack.close();

            return var5;
        } catch (FileNotFoundException ignored) {
        } catch (Exception var14) {
            MYLOGGER.warn("Failed to load icon from pack {}", resourcePackProfile.getName(), var14);
        }

        return UNKNOWN_PACK;
    }

    public static boolean hasFile(String path) {
        File file = new File(path);
        return file.exists();
    }

}
