package net.lopymine.betteranvil.resourcepacks.cit;

import com.google.gson.Gson;
import net.lopymine.betteranvil.resourcepacks.PackManager;
import net.minecraft.item.ItemStack;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;
import static net.lopymine.betteranvil.resourcepacks.ConfigManager.gson;
import static net.lopymine.betteranvil.resourcepacks.PackManager.*;
import static net.lopymine.betteranvil.resourcepacks.ConfigManager.*;

public class CITParser {

    public static ArrayList<CITItem> parseItemsFromConfig(String configFileName, String path) {
        return transformCitItems(getItemsFromConfig(configFileName,path));
    }
    public static ArrayList<CITItem> parseItemsFromConfig(String configFileName, String path, ItemStack item) {
        String itemName = item.getItem().getTranslationKey().replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");
        return transformCitItems(getItemsFromConfig(configFileName,path), itemName);
    }

    public static ArrayList<CITItem> parseAllItems() {
        return transformCitItems(getAllItems());
    }

    public static ArrayList<CITItem> parseAllItems(ItemStack item) {
        String itemName = item.getItem().getTranslationKey().replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");
        return transformCitItems(getAllItems(), itemName);
    }

    private static ArrayList<CITItem> getAllItems() {

        ArrayList<CITItem> citItems = new ArrayList<>();

        for (String rp : getPackNamesWithServer()) {
            if (rp.equals("server")) {
                if (PackManager.getServerResourcePack().get() != null) {
                    ArrayList<CITItem> items = parseItemsFromConfig(PackManager.getServerResourcePack().get(), pathToCITServerConfigFolder);
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
                    MYLOGGER.warn("Not found json file: " + pathToCITConfigFolder + rp + jsonFormat);
                }
            }
        }
        return citItems;
    }

    private static ArrayList<CITItem> getItemsFromConfig(String configFileName, String path) {
        ArrayList<CITItem> citItems = new ArrayList<>();

        Gson gson = new Gson();

        if (!hasConfig(path + configFileName + jsonFormat)) {
            return new ArrayList<>();
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

    public static ArrayList<CITItem> setCitItemsRP(ArrayList<CITItem> citItems, String resourcePackName) {
        ArrayList<CITItem> citItemsCollection = new ArrayList<>();

        for (CITItem citItem : citItems) {
            citItem.setResourcePack(resourcePackName);
            citItemsCollection.add(citItem);
        }

        return citItemsCollection;
    }

    private static ArrayList<CITItem> setCitItemsServerRP(ArrayList<CITItem> citItems) {
        ArrayList<CITItem> citItemsCollection = new ArrayList<>();

        if (PackManager.getServerResourcePack().get() == null) {
            MYLOGGER.warn("Failed to set server resource pack because server resource pack is null");
            return citItemsCollection;
        }

        for (CITItem citItem : citItems) {
            citItem.setServerResourcePack(PackManager.getServerResourcePack().get());
            citItem.setResourcePack("Server");
            citItemsCollection.add(citItem);
        }

        return citItemsCollection;
    }

    private static ArrayList<CITItem> transformCitItems(ArrayList<CITItem> citItemsCollection, String item) {
        ArrayList<CITItem> citItemsCollectionnew = new ArrayList<>();

        for (CITItem citItem : citItemsCollection) {

            for (String itemm : citItem.getItems()) {

                for (String name : citItem.getCustomNames()) {

                    CITItem citItemnew = new CITItem(itemm, name, null);

                    citItemnew.setResourcePack(citItem.getResourcePack());
                    if (citItem.getLore() != null) {
                        citItemnew.setLore(citItem.getLore());
                    }

                    if (citItemnew.getItem().equals(item) && !citItemsCollectionnew.contains(citItemnew)) {
                        citItemsCollectionnew.add(citItemnew);
                    }

                }
            }
        }

        return citItemsCollectionnew;
    }

    public static ArrayList<CITItem> transformCitItems(ArrayList<CITItem> citItemsCollection) {
        ArrayList<CITItem> citItemsCollectionnew = new ArrayList<>();

        for (CITItem citItem : citItemsCollection) {

            for (String itemm : citItem.getItems()) {

                for (String name : citItem.getCustomNames()) {

                    CITItem citItemnew = new CITItem(itemm, name, null);

                    citItemnew.setResourcePack(citItem.getResourcePack());
                    if (citItem.getLore() != null) {
                        citItemnew.setLore(citItem.getLore());
                    }

                    if (!citItemsCollectionnew.contains(citItemnew)) {
                        citItemsCollectionnew.add(citItemnew);
                    }

                }
            }
        }

        return citItemsCollectionnew;
    }

}