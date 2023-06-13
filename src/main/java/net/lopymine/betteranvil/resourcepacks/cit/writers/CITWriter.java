package net.lopymine.betteranvil.resourcepacks.cit.writers;

import net.lopymine.betteranvil.resourcepacks.cit.CITCollection;
import net.lopymine.betteranvil.resourcepacks.cit.CITItem;
import net.lopymine.betteranvil.resourcepacks.properties.PropHandler;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.ArrayList;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;
import static net.lopymine.betteranvil.resourcepacks.ConfigManager.*;

public class CITWriter {
    public static void writeConfig(File file, boolean isZip, boolean isServer) {
        CITCollection citCollection = new CITCollection(getCITItems(file, isZip, isServer).getItems());

        if(citCollection.getItems().isEmpty()){
            return;
        }
        String path = pathToCITConfigFolder;

        if(isServer){
            path = pathToCITServerConfigFolder;
        }

        String json = gson.toJson(citCollection);
        File dir = new File(path);
        if(dir.exists()){
            try (FileWriter writer = new FileWriter(path + file.getName().replace(".zip", "") + jsonFormat)) {
                writer.write(json);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        if(dir.mkdirs()){
            try (FileWriter writer = new FileWriter(path + file.getName().replace(".zip", "") + jsonFormat)) {
                writer.write(json);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            MYLOGGER.warn("Failed to create Better Anvil config folder!");
        }
    }

    public static CITCollection getCITItems(File file, boolean isZip, boolean isServer) {

        ArrayList<CITItem> citItemsCollection = new ArrayList<>();

        String p = pathToCITFolder;

        if(!isZip){
            return readFile(Path.of(file.toPath() + p));
        }

        try (FileSystem zipFS = FileSystems.newFileSystem(file.toPath(), (ClassLoader) null)) {
            Path citFolder = zipFS.getPath(p);

            return readFile(citFolder);

        } catch (IOException e) {
            e.printStackTrace();
            return new CITCollection(citItemsCollection);
        }
    }

    private static CITCollection readFile(Path folder){
        ArrayList<CITItem> citItemsCollection = new ArrayList<>();

        try{
            Files.walk(folder).filter(path -> path.toString().endsWith(".properties")).forEach(propertiesFile -> {
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
                        CITItem citItems = new CITItem(item, customname, damage);
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
        }
        return new CITCollection(citItemsCollection);
    }
}