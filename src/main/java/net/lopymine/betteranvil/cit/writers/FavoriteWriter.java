package net.lopymine.betteranvil.cit.writers;

import net.lopymine.betteranvil.cit.CitCollection;
import net.lopymine.betteranvil.cit.CitItems;
import net.minecraft.item.ItemStack;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;
import static net.lopymine.betteranvil.cit.ConfigParser.*;
import static net.lopymine.betteranvil.cit.ConfigWriter.gson;

public class FavoriteWriter {

    public static CitCollection readConfig(){

        CitCollection citCollection = new CitCollection(new ArrayList<>());

        try (FileReader reader = new FileReader(getPath())) {
            citCollection = gson.fromJson(reader, CitCollection.class);
            reader.close();
            return citCollection;
        } catch (IOException s) {
            return createConfig();
        }
    }

    private static CitCollection createConfig(){
        CitCollection citCollection = new CitCollection(new ArrayList<>());
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
    public static Collection<CitItems> getWithItem(Collection<CitItems> citItems, ItemStack itemStack){

        String itemName = itemStack.getItem().getTranslationKey().replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");

        Collection<CitItems> citItemsArrayList = new ArrayList<>();
        for(CitItems citItem  : citItems){
            for(String rp : getClearResourcePackNames()){
                if(citItem.getItem().equals(itemName) && citItem.getResourcePack().equals(rp)) {
                    citItemsArrayList.add(citItem);
                }
            }
        }

        return citItemsArrayList;
    }

    public static void removeItem(Collection<CitItems> citItems, CitItems s) {
        citItems.remove(s);
        CitCollection citCollection = new CitCollection(citItems);
        String json = gson.toJson(citCollection);

        try (FileWriter writer = new FileWriter(getPath())) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void addItem(CitItems s) {

        Collection<CitItems> citItems = new ArrayList<>(readConfig().getCitItemsCollection());
        citItems.add(s);
        CitCollection citCollection = new CitCollection(citItems);

        String json = gson.toJson(citCollection);

        try (FileWriter writer = new FileWriter(getPath())) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static String getPath(){
        return pathToConfig + "favorite" + jsonFormat;
    }

}