package net.lopymine.betteranvil.resourcepacks.cit.writers;


import net.lopymine.betteranvil.resourcepacks.PackManager;
import net.lopymine.betteranvil.resourcepacks.cit.CITCollection;
import net.lopymine.betteranvil.resourcepacks.cit.CITItem;
import net.minecraft.item.ItemStack;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;
import static net.lopymine.betteranvil.resourcepacks.PackManager.*;
import static net.lopymine.betteranvil.resourcepacks.ConfigManager.*;

public class FavoriteWriter {

    public static CITCollection readConfig() {

        CITCollection citCollection = new CITCollection(new ArrayList<>());

        try (FileReader reader = new FileReader(getPath())) {
            citCollection = gson.fromJson(reader, CITCollection.class);
            reader.close();
            return citCollection;
        } catch (IOException s) {
            return createConfig();
        }
    }

    private static CITCollection createConfig() {
        CITCollection citCollection = new CITCollection(new ArrayList<>());
        MYLOGGER.info("Create favorite config! (Favorite)");
        String json = gson.toJson(citCollection);

        try (FileWriter writer = new FileWriter(getPath())) {
            writer.write(json);
        } catch (IOException e) {
            MYLOGGER.info("Failed to create favorite config! (Favorite)");
            e.printStackTrace();
            return citCollection;
        }
        return citCollection;
    }

    public static Collection<CITItem> getWithItem(Collection<CITItem> citItems, ItemStack itemStack) {

        String itemName = itemStack.getItem().getTranslationKey().replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");

        Collection<CITItem> citItemsArrayList = new ArrayList<>();
        for (CITItem citItem : citItems) {
            for (String rp : getPackNamesWithServer()) {
                if(rp.equals("server") && citItem.getServerResourcePack() != null){
                    if(citItem.getResourcePack().equals("Server") && citItem.getServerResourcePack().equals(PackManager.getServerResourcePack().get()) && citItem.getItem().equals(itemName)){
                        citItemsArrayList.add(citItem);
                    }
                }
                if (citItem.getItem().equals(itemName) && citItem.getResourcePack().equals(rp)) {
                    citItemsArrayList.add(citItem);
                }
            }
        }

        return citItemsArrayList;
    }

    public static ArrayList<CITItem> getWithEnabledPacks(ArrayList<CITItem> citItems){
        ArrayList<CITItem> citItemsArrayList = new ArrayList<>();
        ArrayList<String> packs = PackManager.getPackNamesWithServer();
        for(CITItem item : citItems){
            if(packs.contains(item.getResourcePack())){
                citItemsArrayList.add(item);
            }
        }
        return citItemsArrayList;
    }

    public static ArrayList<CITItem> getPackItems(ArrayList<CITItem> citItems, ArrayList<String> packs){
        ArrayList<CITItem> items = new ArrayList<>();

        for(CITItem item : citItems){
            for(String s : packs){
                if(item.getResourcePack().equals(s)){
                    items.add(item);
                }
            }
        }

        return items;
    }

    public static void removeItem(ArrayList<CITItem> citItems, CITItem s) {
        if(citItems.contains(s)){
            System.out.println(citItems.stream().toList().indexOf(s));
        }
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

        ArrayList<CITItem> citItems = new ArrayList<>(readConfig().getItems());
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