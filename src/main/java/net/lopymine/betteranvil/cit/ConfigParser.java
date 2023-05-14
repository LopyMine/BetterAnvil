package net.lopymine.betteranvil.cit;

import com.google.gson.Gson;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicReference;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;
import static net.lopymine.betteranvil.BetterAnvil.getResourcePackManager;

public class ConfigParser {
    public static final String pathToConfigFolder = "config/betteranvil/resourcepacks/";
    public static final String pathToServerConfigFolder = "config/betteranvil/resourcepacks/servers/";
    public static final String pathToConfig = "config/betteranvil/";
    public static final String jsonFormat = ".json";
    public static final String pathToCitFolder = "/assets/minecraft/optifine/cit/";
    public static final String pathToResourcePacks = "resourcepacks/";
    private static final AtomicReference<String> serverResourcePack = new AtomicReference<>();

    public static Collection<CitItems> parseAllItemNames(ItemStack item) {
        Collection<CitItems> itemNames = new ArrayList<>();

        Gson gson = new Gson();

        String itemName = item.getItem().getTranslationKey().replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");

        for (String rpName : getClearResourcePackNames()) {
            if(rpName.equals("server")){
                if(serverResourcePack.get() != null){
                    CitCollection citCollection = parseItemsFromConfig(serverResourcePack.get(),item,pathToServerConfigFolder);
                    if(!citCollection.getCitItemsCollection().isEmpty()){
                        itemNames.addAll(transformCitItems(setCitItemsServerRP(citCollection), itemName));
                    } else {
                        MYLOGGER.warn("Cit collection is empty (Server)");
                    }
                } else {
                    MYLOGGER.warn("Server resource pack getter is null (Server)");
                }
            } else {
                try (FileReader reader = new FileReader(pathToConfigFolder + rpName + jsonFormat)) {

                    CitCollection citCollection = gson.fromJson(reader, CitCollection.class);
                    reader.close();

                    itemNames.addAll(transformCitItems(setCitItemsRP(citCollection, rpName), itemName));
                } catch (IOException ignored) {
                    MYLOGGER.warn("Not found json file: " + pathToConfigFolder + rpName + jsonFormat);
                }
            }

        }

        return itemNames;
    }

    public static CitCollection parseItemsFromConfig(String configFileName, ItemStack anvilItem, String path) {
        CitCollection citCollection = new CitCollection(new ArrayList<>());

        Gson gson = new Gson();

        String itemName = anvilItem.getItem().getTranslationKey().replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");

        if(!hasFile((path + configFileName + jsonFormat))){
            return citCollection;
        }
        try (FileReader reader = new FileReader(path + configFileName + jsonFormat)) {

            return gson.fromJson(reader, CitCollection.class);

        } catch (IOException ignored) {
        }
        return citCollection;
    }

    public static Collection<CitItems> setCitItemsRP(CitCollection citCollection, String resourcePackName) {
        Collection<CitItems> citItemsCollection = new ArrayList<>();
        for(CitItems citItem : citCollection.getCitItemsCollection()){
            citItem.setResourcePack(resourcePackName);
            citItemsCollection.add(citItem);
        }
        return citItemsCollection;
    }

    public static Collection<CitItems> setCitItemsServerRP(CitCollection citCollection) {
        Collection<CitItems> citItemsCollection = new ArrayList<>();
        if(serverResourcePack.get() == null){
            MYLOGGER.warn("Failed to set server resource pack because server resource pack is null");
            return citItemsCollection;
        }


        for(CitItems citItem : citCollection.getCitItemsCollection()){
            citItem.setServerResourcePack(serverResourcePack.get());
            citItem.setResourcePack("Server");
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
                    if(citItem.getLore() != null){
                        citItemnew.setLore(citItem.getLore());
                    }

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
            if(name.equals("server")){
                newNames.add(name);
            }
            if (name.startsWith("file/")) {
                newNames.add(name.replaceAll("file/", "").replaceAll(".zip", ""));
                continue;
            }
        }
        return newNames;
    }

    public static ArrayList<String> getResourcePacksWithCITFolder() {

        ArrayList<String> newNames = new ArrayList<>();
        for (String name : getClearResourcePackNames()) {
            if(name.equals("server")){
                newNames.add(name);
            }
            if(hasFile(pathToConfigFolder + name + jsonFormat)){
                newNames.add(name);
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
            //if (name.equals("server")){
            //    MinecraftClient.getInstance().getCurrentServerEntry().getResourcePackPolicy().getName()
            //}
        }
        return newNames;
    }

    public static boolean hasFile(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static AtomicReference<String> getServerResourcePack() {
        return serverResourcePack;
    }

    public static void setServerResourcePack(String serverResourcePackk) {
        serverResourcePack.set(serverResourcePackk);
    }
}