package net.lopymine.betteranvil.resourcepacks.cmd.writers;


import net.lopymine.betteranvil.resourcepacks.PackManager;
import net.lopymine.betteranvil.resourcepacks.cmd.CMDCollection;
import net.lopymine.betteranvil.resourcepacks.cmd.CMDItem;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;
import static net.lopymine.betteranvil.resourcepacks.ConfigManager.*;

public class CMDFavoriteWriter {

    private static CMDCollection readConfig() {

        CMDCollection cmdCollection = new CMDCollection(new LinkedHashSet<>());

        try (FileReader reader = new FileReader(getPath())) {
            cmdCollection = gson.fromJson(reader, CMDCollection.class);
            reader.close();
            return cmdCollection;
        } catch (IOException s) {
            return createConfig();
        }
    }

    public static LinkedHashSet<CMDItem> getFavoriteItems(){
        CMDCollection cmdCollection = readConfig();
        LinkedHashSet<CMDItem> cmdItems = new LinkedHashSet<>();
        LinkedHashSet<String> packs = PackManager.getPackNamesWithServer();


        for(CMDItem item : cmdCollection.getItems()){

            if(packs.contains(item.getResourcePack())){
                cmdItems.add(item);
            }
        }

        return cmdItems;

    }

    private static CMDCollection createConfig() {
        CMDCollection cmdCollection = new CMDCollection(new LinkedHashSet<>());
        String json = gson.toJson(cmdCollection);

        try (FileWriter writer = new FileWriter(getPath())) {
            writer.write(json);
            MYLOGGER.info("Created favorite config! (CMD Favorite)");
        } catch (IOException e) {
            e.printStackTrace();
            MYLOGGER.info("Failed to create favorite config! (CMD Favorite)");
            return cmdCollection;
        }
        return cmdCollection;
    }

    public static void removeItem(CMDItem s) {
        LinkedHashSet<CMDItem> cmdItems = readConfig().getItems();
        cmdItems.remove(s);

        CMDCollection cmdCollection = new CMDCollection(cmdItems);
        String json = gson.toJson(cmdCollection);

        try (FileWriter writer = new FileWriter(getPath())) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addItem(CMDItem s) {

        LinkedHashSet<CMDItem> cmdItems = new LinkedHashSet<>(readConfig().getItems());
        if(s.getResourcePack().equals("Server")){
            s.setServerResourcePack(PackManager.getServerResourcePack().get());
        }
        cmdItems.add(s);
        CMDCollection cmdCollection = new CMDCollection(cmdItems);


        String json = gson.toJson(cmdCollection);

        try (FileWriter writer = new FileWriter(getPath())) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPath() {
        return pathToConfig + "cmd_favorite" + jsonFormat;
    }

}