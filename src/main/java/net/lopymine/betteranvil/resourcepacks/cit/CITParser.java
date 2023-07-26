package net.lopymine.betteranvil.resourcepacks.cit;

import com.google.gson.Gson;
import net.lopymine.betteranvil.resourcepacks.PackManager;
import net.minecraft.item.ItemStack;

import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedHashSet;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;
import static net.lopymine.betteranvil.resourcepacks.ConfigManager.*;
import static net.lopymine.betteranvil.resourcepacks.PackManager.getPackNamesWithServer;
import static net.lopymine.betteranvil.resourcepacks.PackManager.getServerResourcePack;

public class CITParser {

    public static LinkedHashSet<CITItem> parseItemsFromConfig(String configFileName, String path) {
        return transformCitItems(getItemsFromConfig(configFileName,path));
    }
    public static LinkedHashSet<CITItem> parseItemsFromConfig(String configFileName, String path, ItemStack item) {
        String itemName = item.getItem().getTranslationKey().replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");
        return transformCitItems(getItemsFromConfig(configFileName,path), itemName);
    }

    public static LinkedHashSet<CITItem> parseAllItems() {
        return transformCitItems(getAllItems());
    }

    public static LinkedHashSet<CITItem> parseAllItems(ItemStack item) {
        String itemName = item.getItem().getTranslationKey().replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");
        return transformCitItems(getAllItems(), itemName);
    }

    private static LinkedHashSet<CITItem> getAllItems() {

        LinkedHashSet<CITItem> citItems = new LinkedHashSet<>();

        for (String rp : getPackNamesWithServer()) {
            if (rp.equals("server")) {
                if (PackManager.getServerResourcePack().get() != null) {
                    LinkedHashSet<CITItem> items = parseItemsFromConfig(PackManager.getServerResourcePack().get(), pathToCITServerConfigFolder);
                    if (!items.isEmpty()) {
                        citItems.addAll(setCitItemsServerRP(items));
                    } else {
                        MYLOGGER.warn("Cit collection is empty (Server)");
                    }
                } else {
                    MYLOGGER.warn("Server resource pack getter is null (Server)");
                }
            } else {
                try (FileReader reader = new FileReader(pathToCITConfigFolder + rp + jsonFormat)) {

                    citItems.addAll(setCitItemsRP(gson.fromJson(reader, CITCollection.class).getItems(), rp));
                    reader.close();
                } catch (IOException ignored) {
                }
            }
        }
        return citItems;
    }

    private static LinkedHashSet<CITItem> getItemsFromConfig(String configFileName, String path) {
        LinkedHashSet<CITItem> citItems = new LinkedHashSet<>();

        Gson gson = new Gson();

        if (!hasConfig(path + configFileName + jsonFormat)) {
            return new LinkedHashSet<>();
        }

        try (FileReader reader = new FileReader(path + configFileName + jsonFormat)) {

            if (path.equals(pathToCITServerConfigFolder)) {
                citItems.addAll(setCitItemsServerRP(gson.fromJson(reader, CITCollection.class).getItems()));
            } else {
                citItems.addAll(setCitItemsRP(gson.fromJson(reader, CITCollection.class).getItems(), configFileName));
            }

        } catch (IOException ignored) {
        }

        return citItems;
    }

    public static LinkedHashSet<CITItem> setCitItemsRP(LinkedHashSet<CITItem> items, String pack) {
        LinkedHashSet<CITItem> citItems = new LinkedHashSet<>();

        items.forEach(item ->{
            item.setResourcePack(pack);
            citItems.add(item);
        });


        return citItems;
    }

    private static LinkedHashSet<CITItem> setCitItemsServerRP(LinkedHashSet<CITItem> items) {
        LinkedHashSet<CITItem> citItems = new LinkedHashSet<>();

        if (getServerResourcePack().get() == null) {
            MYLOGGER.warn("Failed to set server resource pack because server resource pack is null");
            return citItems;
        }

        items.forEach(item ->{
            item.setServerResourcePack(getServerResourcePack().get());
            item.setResourcePack("Server");
            citItems.add(item);
        });

        return citItems;
    }

    private static LinkedHashSet<CITItem> transformCitItems(LinkedHashSet<CITItem> items, String item) {
        LinkedHashSet<CITItem> citItems = new LinkedHashSet<>();

        for (CITItem citItem : items) {

            for (String name : citItem.getCustomNames()) {

                CITItem newItem = new CITItem(citItem.getItem(), name, null);
                newItem.setResourcePack(citItem.getResourcePack());

                if (citItem.getLore() != null) newItem.setLore(citItem.getLore());

                if (newItem.getItems().contains(item)) citItems.add(newItem);

            }
        }

        return citItems;
    }

    public static LinkedHashSet<CITItem> transformCitItems(LinkedHashSet<CITItem> items) {
        LinkedHashSet<CITItem> citItems = new LinkedHashSet<>();

        for (CITItem citItem : items) {

            for (String name : citItem.getCustomNames()) {

                CITItem newItem = new CITItem(citItem.getItem(), name, null);
                newItem.setResourcePack(citItem.getResourcePack());

                if (citItem.getLore() != null) newItem.setLore(citItem.getLore());

                citItems.add(newItem);
            }
        }

        return citItems;
    }

}