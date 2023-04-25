package net.lopymine.betteranvil.cit;

import com.google.common.hash.Hashing;
import com.google.gson.Gson;
import net.fabricmc.loader.api.FabricLoader;
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

    public static Collection<CitItems> parseAllItemNames(ItemStack item) {
        Collection<CitItems> itemNames = new ArrayList<>();

        Gson gson = new Gson();

        String itemName = item.getItem().getTranslationKey().replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");

        for (String rpName : getClearResourcePackNames()) {
            try (FileReader reader = new FileReader(pathToConfigFolder + rpName + jsonFormat)) {

                CitCollection citCollection = gson.fromJson(reader, CitCollection.class);
                reader.close();

                itemNames.addAll(transformCitItems(setCitItemsRP(citCollection, rpName), itemName));
            } catch (IOException ignored) {
                MYLOGGER.warn("Not found Better Anvil Folder!");
            }
        }

        return itemNames;
    }

    public static Collection<CitItems> parseItemsFromConfig(String configFileName, ItemStack anvilItem, String path) {
        Collection<CitItems> items = new ArrayList<>();

        Gson gson = new Gson();

        String itemName = anvilItem.getItem().getTranslationKey().replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");

        if(!hasFile((path + configFileName + jsonFormat))){
            return items;
        }
        try (FileReader reader = new FileReader(path + configFileName + jsonFormat)) {

            CitCollection citCollection = gson.fromJson(reader, CitCollection.class);
            reader.close();

            items.addAll(transformCitItems(setCitItemsRP(citCollection, configFileName), itemName));
        } catch (IOException ignored) {
        }
        return items;
    }

    public static Collection<CitItems> setCitItemsRP(CitCollection citCollection, String resourcePackName) {
        Collection<CitItems> citItemsCollection = new ArrayList<>();
        for(CitItems citItem : citCollection.getCitItemsCollection()){
            citItem.setResourcePack(resourcePackName);
            citItemsCollection.add(citItem);
        }
        return citItemsCollection;
    }

    public static Collection<CitItems> transformCitItems(Collection<CitItems> citItemsCollection, String item) {
        Collection<CitItems> citItemsCollectionnew = new ArrayList<>();

        for (CitItems citItem : citItemsCollection) {
            for (String itemm : citItem.getItems()) {
                for (String name : citItem.getCustomNames()){
                    CitItems citItemnew = new CitItems(itemm, name, null);
                    citItemnew.setResourcePack(citItem.getResourcePack());
                    if(citItemnew.getItem().equals(item) && !contains(citItemsCollectionnew, citItemnew)){
                        citItemsCollectionnew.add(citItemnew);
                    }
                }
            }
        }
        //Set<CitItems> noDuplicateCustomNames = new LinkedHashSet<>(citItemsCollectionnew);
        //return new ArrayList<>(noDuplicateCustomNames);
        return citItemsCollectionnew;
    }

    private static boolean contains(Collection<CitItems> citItems, CitItems c){
        for(CitItems s : citItems){
            if(s.getCustomName().equals(c.getCustomName()) && s.getItem().equals(c.getItem())){
                return true;
            }
        }
        return false;
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

    public static boolean hasFile(String path) {
        File file = new File(path);
        return file.exists();
    }

}//