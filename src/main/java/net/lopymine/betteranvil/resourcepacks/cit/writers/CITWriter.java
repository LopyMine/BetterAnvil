package net.lopymine.betteranvil.resourcepacks.cit.writers;

import net.lopymine.betteranvil.resourcepacks.cit.CITCollection;
import net.lopymine.betteranvil.resourcepacks.cit.CITItem;
import net.lopymine.betteranvil.resourcepacks.properties.CITHandler;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Stream;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;
import static net.lopymine.betteranvil.resourcepacks.ConfigManager.*;

public class CITWriter {
    public static void writeConfig(File file, boolean isZip, boolean isServer) {
        CITCollection citCollection = new CITCollection(getCITItems(file, isZip, isServer).getItems());

        if(citCollection.getItems().isEmpty()){
            MYLOGGER.error("Failed to create CIT config for " + file.getName());
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

        LinkedHashSet<CITItem> citItems = new LinkedHashSet<>();

        String p = pathToCITFolder;

        if(!isZip){
            return readFile(Path.of(file.toPath() + p), isServer);
        }

        try (FileSystem zipFS = FileSystems.newFileSystem(file.toPath(), (ClassLoader) null)) {
            Path citFolder = zipFS.getPath(p);

            return readFile(citFolder, isServer);

        } catch (IOException e) {
            e.printStackTrace();
            return new CITCollection(citItems);
        }
    }

    private static CITCollection readFile(Path folder, boolean isServer){
        LinkedHashSet<CITItem> citItems = new LinkedHashSet<>();
        Stream<Path> files;

        try{
            files = Files.walk(folder).filter(path -> path.toString().endsWith(".properties"));
        } catch (IOException e) {
            e.printStackTrace();
            return new CITCollection(citItems);
        }

        files.forEach(propertiesFile -> {
            try (InputStream inputStream = Files.newInputStream(propertiesFile); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                String item = null;
                String customname = null;
                String damage = null;
                String enchantments = null;
                List<String> lore = new ArrayList<>();
                while ((line = reader.readLine()) != null) {
                    if (CITHandler.isItem(line)) {
                        item = CITHandler.getItem(line);
                    }
                    if(CITHandler.isLore(line)){
                        lore.add(CITHandler.getLore(line));
                    }
                    if (CITHandler.isCustomName(line)) {
                        customname = CITHandler.getCustomName(line);
                    }
                    if(CITHandler.isDamage(line)){
                        damage = CITHandler.getDamageString(line);
                    }
                }
                if(item != null && customname != null){
                    CITItem citItem = new CITItem(item, customname, damage);
                    if(!lore.isEmpty()){
                        citItem.setLore(lore);
                    }
                    citItems.add(citItem);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return new CITCollection(citItems);
    }
}