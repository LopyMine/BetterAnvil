package net.lopymine.betteranvil.cit;

import com.google.gson.Gson;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import io.github.cottonmc.cotton.gui.widget.icon.Icon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourcePack;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import org.apache.commons.lang3.StringEscapeUtils;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ConfigParser {

    private static final String pathToConfigFolder = "config/betteranvil/";
    private static final String jsonFormat = ".json";

    public static ArrayList<String> parseItemNames(ItemStack item) {
        ArrayList<String> itemNames = new ArrayList<>();

        Gson gson = new Gson();

        MinecraftClient mc = MinecraftClient.getInstance();

        String itemName = item.getItem().getTranslationKey().replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");

        ArrayList<String> rpNames = getClearResourcePackNames(mc);

        for (String rpName : rpNames) {
            try (FileReader reader = new FileReader(pathToConfigFolder + rpName + jsonFormat)) {

                CitCollection citCollection = gson.fromJson(reader, CitCollection.class);
                reader.close();
                //System.out.println(getNames(myOb.getCitItemsCollection(), itemName));
                itemNames.addAll(getItemNames(citCollection.getCitItemsCollection(), itemName, item));
                } catch (IOException e) {
            }
        }

        return itemNames;
    }


    private static ArrayList<String> getItemNames(Collection<CitItems> citItemsCollection, String item, ItemStack anvilItem) {
        ArrayList<String> customNames = new ArrayList<>();

        for (CitItems citItem : citItemsCollection) {
           //if(!(citItem.getDamage() == null)){
           //    int min = (int) anvilItem.getItem().getMaxDamage() * (citItem.getDamage().get(0) / 100);
           //    int max = (int) anvilItem.getItem().getMaxDamage() * (citItem.getDamage().get(1) / 100);

           //    if(!(anvilItem.getDamage() < max && anvilItem.getDamage() > min)){
           //        break;
           //    }
           //}
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

    public static ArrayList<String> getClearResourcePackNames(MinecraftClient mc) {
        ResourcePackManager resourcePackManager = mc.getResourcePackManager();
        Collection<String> resourcePackCollection = resourcePackManager.getEnabledNames();

        ArrayList<String> newNames = new ArrayList<>();
        for (String name : resourcePackCollection.stream().toList()) {
            if (name.startsWith("file/")) {
                newNames.add(name.replaceAll("file/", "").replaceAll(".zip", ""));
                continue;
            }
        }
        return newNames;
    }

    public static ArrayList<String> getResourcePackNames(MinecraftClient mc) {
        ResourcePackManager resourcePackManager = mc.getResourcePackManager();
        Collection<String> resourcePackCollection = resourcePackManager.getEnabledNames();

        ArrayList<String> newNames = new ArrayList<>();
        for (String name : resourcePackCollection.stream().toList()) {
            if (name.startsWith("file/")) {
                newNames.add(name.replaceAll("file/", "").replaceAll(".zip", ""));
                continue;
            }
        }
        return newNames;
    }

    public static ArrayList<String> parseItemFromResourcePack(String rpName, ItemStack item) {
        ArrayList<String> itemNames = new ArrayList<>();

        Gson gson = new Gson();

        String itemName = item.getItem().getTranslationKey().replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");

        try (FileReader reader = new FileReader(pathToConfigFolder + rpName + jsonFormat)) {

            CitCollection citCollection = gson.fromJson(reader, CitCollection.class);
            reader.close();
            //System.out.println(getNames(myOb.getCitItemsCollection(), itemName));
            itemNames.addAll(getItemNames(citCollection.getCitItemsCollection(), itemName, item));
        } catch (IOException e) {
            e.printStackTrace();
            return itemNames;
        }
        return itemNames;
    }

}
