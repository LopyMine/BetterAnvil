package net.lopymine.betteranvil.cit.writers;

import net.lopymine.betteranvil.cit.CitCollection;
import net.lopymine.betteranvil.cit.CitItems;
import net.lopymine.betteranvil.cit.ConfigParser;
import net.minecraft.item.ItemStack;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static net.lopymine.betteranvil.cit.ConfigParser.*;
import static net.lopymine.betteranvil.cit.ConfigWriter.gson;

public class FavoriteWriter {

    public static CitCollection readConfig(){

        CitCollection citCollection = new CitCollection(new ArrayList<>());

        try (FileReader reader = new FileReader(pathToConfig + "favorite" + jsonFormat)) {
            citCollection = gson.fromJson(reader, CitCollection.class);
            reader.close();
            return citCollection;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return citCollection;
    }

    public static ArrayList<String> getItemNames(ItemStack itemStack){
        return ConfigParser.parseItemsFromConfig("favorite", itemStack, pathToConfig);
    }

    public static void removeItem(String s){

        Collection<CitItems> citItems = readConfig().getCitItemsCollection();

        if(citItems.isEmpty()){
            return;
        }

        Collection<CitItems> citNewItems = new ArrayList<>();

        for(CitItems citItem : citItems){
            for(String rp : citItem.getCustomNames()){
                if(!rp.equals(s)){
                    citNewItems.add(citItem);
                    break;
                }
            }
        }

        CitCollection citCollection = new CitCollection(citNewItems);
        String json = gson.toJson(citCollection);

        try (FileWriter writer = new FileWriter(pathToConfig + "favorite" + jsonFormat)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addItem(String s, ItemStack itemStack){

        Collection<CitItems> citItems = readConfig().getCitItemsCollection();

        String itemName = itemStack.getItem().getTranslationKey().replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");

        citItems.add(new CitItems(itemName, s, null));

        CitCollection citCollection = new CitCollection(citItems);
        String json = gson.toJson(citCollection);

        try (FileWriter writer = new FileWriter(pathToConfig + "favorite" + jsonFormat)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
