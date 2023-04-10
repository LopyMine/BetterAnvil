package net.lopymine.betteranvil.cit.writers;

import com.google.gson.Gson;
import net.lopymine.betteranvil.cit.CitCollection;
import net.lopymine.betteranvil.cit.CitItems;
import net.lopymine.betteranvil.cit.properties.PropHandler;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;

public class FolderWriter {

    private static final String pathToConfigFolder = "config/betteranvil/";
    private static final String jsonFormat = ".json";
    private static final String pathToCitFolder = "/assets/minecraft/optifine/cit/";
    private static final String pathToResourcePacks = "resourcepacks/";

    public static void writeConfig(String rpName, Gson gson) {
        if(getCitFolderFiles(rpName).getCitItemsCollection().isEmpty()){
            return;
        }

        String json = gson.toJson(getCitFolderFiles(rpName));
        File dir = new File(pathToConfigFolder);
        if (!dir.exists()) {
            boolean success = dir.mkdir();
            if (success) {
                MYLOGGER.info("Directory created successfully!");
                try (FileWriter writer = new FileWriter(pathToConfigFolder + rpName + jsonFormat)) {
                    writer.write(json);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                MYLOGGER.info("Failed to create directory");
            }
        } else {
            try (FileWriter writer = new FileWriter(pathToConfigFolder + rpName + jsonFormat)) {
                writer.write(json);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static CitCollection getCitFolderFiles(String resourcePackName) {
        Path resourcePackPath = Paths.get(pathToResourcePacks + resourcePackName + pathToCitFolder);

        Collection<CitItems> citItems1Collection = new ArrayList<>();

        try {
            Files.walk(resourcePackPath).filter(path -> path.toString().endsWith(".properties")).forEach(propertiesFile -> {
                try (InputStream inputStream = Files.newInputStream(propertiesFile);
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    String line;
                    String item = null;
                    String customname = null;
                    String damage = null;
                    String enchantments = null;

                    while ((line = reader.readLine()) != null) {
                        if (PropHandler.isItem(line)) {
                            item = PropHandler.getItem(line);
                        }
                        if (PropHandler.isCustomName(line)) {
                            customname = PropHandler.getCustomName(line);
                        }
                        if(PropHandler.isDamage(line)){
                            damage = PropHandler.getDamageString(line);
                        }
                    }
                    if(item != null && customname != null){
                        CitItems citItems1 = new CitItems(item, customname, damage);
                        citItems1Collection.add(citItems1);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            return new CitCollection(citItems1Collection);
        }


        return new CitCollection(citItems1Collection);
    }

    public static boolean hasCitFolder(String path) {
        File file = new File(path);
        if (file.exists()) {
            return true;
        } else {
            return false;
        }
    }
}
