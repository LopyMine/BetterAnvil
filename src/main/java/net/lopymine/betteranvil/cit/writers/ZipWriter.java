package net.lopymine.betteranvil.cit.writers;

import com.google.gson.Gson;
import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.cit.CitCollection;
import net.lopymine.betteranvil.cit.CitItems;
import net.lopymine.betteranvil.cit.properties.PropHandler;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;
import static net.lopymine.betteranvil.cit.ConfigParser.*;

public class ZipWriter {
    public static void writeConfig(String rpName, Gson gson) {
        CitCollection citCollection = new CitCollection(getCitFolderFiles(rpName).getCitItemsCollection());

        if(citCollection.getCitItemsCollection().isEmpty()){
            return;
        }


        String json = gson.toJson(citCollection);
        File dir = new File(pathToConfigFolder);
        if(dir.exists()){
            try (FileWriter writer = new FileWriter(pathToConfigFolder + rpName.replaceAll(".zip", "") + jsonFormat)) {
                writer.write(json);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        if(dir.mkdirs()){
            try (FileWriter writer = new FileWriter(pathToConfigFolder + rpName.replaceAll(".zip", "") + jsonFormat)) {
                writer.write(json);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            MYLOGGER.warn("Failed to create Better Anvil config folder! (Zip)");
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
                    ArrayList<String> lore = new ArrayList<>();
                    while ((line = reader.readLine()) != null) {
                        if (PropHandler.isItem(line)) {
                            item = PropHandler.getItem(line);
                        }
                        if(PropHandler.isLore(line)){
                            lore.add(PropHandler.getLore(line));
                        }
                        if (PropHandler.isCustomName(line)) {
                            customname = PropHandler.getCustomName(line);
                        }
                        if(PropHandler.isDamage(line)){
                            damage = PropHandler.getDamageString(line);
                        }
                    }
                    if(item != null && customname != null){
                        CitItems citItems = new CitItems(item, customname, damage);
                        if(!lore.isEmpty()){
                            citItems.setLore(lore);
                        }
                        citItemsCollection.add(citItems);
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

}//