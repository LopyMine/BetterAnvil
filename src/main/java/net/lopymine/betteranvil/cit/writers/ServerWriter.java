package net.lopymine.betteranvil.cit.writers;

import net.lopymine.betteranvil.cit.CitCollection;
import net.lopymine.betteranvil.cit.CitItems;
import net.lopymine.betteranvil.cit.ConfigParser;
import net.lopymine.betteranvil.cit.properties.PropHandler;
import net.minecraft.client.MinecraftClient;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;
import static net.lopymine.betteranvil.cit.ConfigParser.*;
import static net.lopymine.betteranvil.cit.ConfigWriter.gson;
public class ServerWriter {
    public static void writeConfig(File serverResourcePackFile){
        CitCollection citCollection = new CitCollection(getCitFolderFiles(serverResourcePackFile).getCitItemsCollection());

        if(citCollection.getCitItemsCollection().isEmpty()){
            return;
        }

        String json = gson.toJson(citCollection);
        File dir = new File(pathToServerConfigFolder);
        if(dir.exists()){
            try (FileWriter writer = new FileWriter(pathToServerConfigFolder + serverResourcePackFile.getName() + jsonFormat)) {
                writer.write(json);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        if(dir.mkdirs()){
            try (FileWriter writer = new FileWriter(pathToServerConfigFolder + serverResourcePackFile.getName() + jsonFormat)) {
                writer.write(json);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            MYLOGGER.warn("Failed to create Better Anvil config folder! (Server)");
        }
    }

    private static CitCollection getCitFolderFiles(File resourcePack) {
        Collection<CitItems> citItemsCollection = new ArrayList<>();

        try (FileSystem zipFS = FileSystems.newFileSystem(resourcePack.toPath())) {
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

    public static boolean hasCitZipFolder(File resourcePack) {
        try (FileSystem zipFS = FileSystems.newFileSystem(resourcePack.toPath())) {
            Path citFolder = zipFS.getPath(pathToCitFolder);
            return Files.isDirectory(citFolder);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

}
