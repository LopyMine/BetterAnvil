package net.lopymine.betteranvil.cit;

import com.google.gson.Gson;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.resource.ResourcePackManager;
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

        ResourcePackManager resourcePackManager = mc.getResourcePackManager();
        Collection<String> resourcePackCollection = resourcePackManager.getEnabledNames();

        String itemName = item.getItem().getTranslationKey().replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");

        ArrayList<String> rpNames = getClearResourcePackNames(resourcePackCollection.stream().toList());

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

    private static ArrayList<String> getClearResourcePackNames(List<String> names) {
        ArrayList<String> newNames = new ArrayList<>();
        for (String name : names) {
            if (name.startsWith("file/")) {
                newNames.add(name.replaceAll("file/", "").replaceAll(".zip", ""));
                continue;
            }
        }
        return newNames;
    }

    private static List<String> getClearCustomName(String citItemName) {
        String clearName = citItemName.replaceAll("iregex:", "").replaceAll("ipattern:", "").replace("(", "").replace(")", "");
        String[] names = clearName.split("\\|"); // split the string by "|" delimiter
        List<String> words = new ArrayList<>();
        for (String word : names) {
            words.add(StringEscapeUtils.unescapeJava(word));
        }
        return words;
    }
}
