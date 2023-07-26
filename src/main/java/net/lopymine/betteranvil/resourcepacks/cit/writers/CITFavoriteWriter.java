package net.lopymine.betteranvil.resourcepacks.cit.writers;


import net.lopymine.betteranvil.resourcepacks.PackManager;
import net.lopymine.betteranvil.resourcepacks.cit.CITCollection;
import net.lopymine.betteranvil.resourcepacks.cit.CITItem;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;
import static net.lopymine.betteranvil.resourcepacks.PackManager.*;
import static net.lopymine.betteranvil.resourcepacks.ConfigManager.*;

public class CITFavoriteWriter {

    public static CITCollection readConfig() {

        CITCollection citCollection;

        try (FileReader reader = new FileReader(getPath())) {
            citCollection = gson.fromJson(reader, CITCollection.class);
            reader.close();
            return citCollection;
        } catch (IOException s) {
            return createConfig();
        }
    }

    private static CITCollection createConfig() {
        CITCollection citCollection = new CITCollection(new LinkedHashSet<>());
        String json = gson.toJson(citCollection);

        try (FileWriter writer = new FileWriter(getPath())) {
            writer.write(json);
            MYLOGGER.info("Created favorite config! (Favorite)");
        } catch (IOException e) {
            e.printStackTrace();
            MYLOGGER.info("Failed to create favorite config! (CIT Favorite)");
            return citCollection;
        }
        return citCollection;
    }

    public static LinkedHashSet<CITItem> getWithItem(LinkedHashSet<CITItem> citItems, ItemStack itemStack) {

        String itemName = itemStack.getItem().getTranslationKey().replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");

        LinkedHashSet<CITItem> citItemsArrayList = new LinkedHashSet<>();
        for (CITItem citItem : citItems) {
            if (citItem.getItems().contains(itemName)) {
                citItemsArrayList.add(citItem);
            }
        }

        return getPackItems(citItemsArrayList,getPackNamesWithServer());
    }

    public static LinkedHashSet<CITItem> getPackItems(LinkedHashSet<CITItem> citItems, LinkedHashSet<String> packs){
        LinkedHashSet<CITItem> items = new LinkedHashSet<>();

        for(CITItem item : citItems){
            if(packs.contains(item.getResourcePack())){
                items.add(item);
            }
            if(packs.contains("server") && getServerResourcePack() != null){
                if(item.getResourcePack().equals("Server") && item.getServerResourcePack().equals(PackManager.getServerResourcePack().get())){
                    items.add(item);
                }
            }
        }

        return items;
    }

    public static void removeItem(CITItem s) {
        LinkedHashSet<CITItem> citItems = readConfig().getItems();
        citItems.remove(s);

        CITCollection citCollection = new CITCollection(citItems);
        String json = gson.toJson(citCollection);

        try (FileWriter writer = new FileWriter(getPath())) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addItem(CITItem s) {

        LinkedHashSet<CITItem> citItems = new LinkedHashSet<>(readConfig().getItems());
        if(s.getResourcePack().equals("Server")){
            s.setServerResourcePack(PackManager.getServerResourcePack().get());
        }
        citItems.add(s);
        CITCollection citCollection = new CITCollection(citItems);

        String json = gson.toJson(citCollection);

        try (FileWriter writer = new FileWriter(getPath())) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPath() {
        return pathToConfig + "favorite" + jsonFormat;
    }

}