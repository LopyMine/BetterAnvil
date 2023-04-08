package net.lopymine.betteranvil.cit.writers;

import com.google.gson.Gson;
import net.lopymine.betteranvil.cit.CitCollection;
import net.lopymine.betteranvil.cit.CitItems;
import net.lopymine.betteranvil.cit.properties.PropHandler;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;

public class ZipWriter {

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
                try (FileWriter writer = new FileWriter(pathToConfigFolder + rpName.replaceAll(".zip", "") + jsonFormat)) {
                    writer.write(json);
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                MYLOGGER.info("Failed to create directory");
            }
        } else {
            try (FileWriter writer = new FileWriter(pathToConfigFolder + rpName.replaceAll(".zip", "") + jsonFormat)) {
                writer.write(json);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private static CitCollection getCitFolderFiles(String resourcePackName) {
        Path resourcePackPath = Paths.get(pathToResourcePacks + resourcePackName);

        Collection<CitItems> citItemsCollection = new ArrayList<>();

        try (FileSystem zipFS = FileSystems.newFileSystem(resourcePackPath, (ClassLoader) null)) {
            Path citFolder = zipFS.getPath(pathToCitFolder);

            Files.walk(citFolder).filter(path -> path.toString().endsWith(".properties")).forEach(propertiesFile -> {
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
                        //if(PropHandler.isEnchantment(line)){
//                      //      enchantments = PropHandler.get
                        //}
                    }

                    if(item != null && customname != null){
                        CitItems citItems1 = new CitItems(item, customname, damage);
                        citItemsCollection.add(citItems1);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
            return new CitCollection(citItemsCollection);
        }
        return new CitCollection(citItemsCollection);
    }

    public static boolean hasCitZipFolder(Path resourcePackPath) {
        try (FileSystem zipFS = FileSystems.newFileSystem(resourcePackPath, (ClassLoader) null)) {
            Path citFolder = zipFS.getPath(pathToCitFolder);
            return Files.isDirectory(citFolder);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}