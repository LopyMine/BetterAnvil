package net.lopymine.betteranvil.resourcepacks.cem.writers;

import net.lopymine.betteranvil.resourcepacks.PackManager;
import net.lopymine.betteranvil.resourcepacks.cem.CEMCollection;
import net.lopymine.betteranvil.resourcepacks.cem.CEMItem;
import net.lopymine.betteranvil.resourcepacks.cmd.CMDCollection;
import net.lopymine.betteranvil.resourcepacks.cmd.CMDItem;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;
import static net.lopymine.betteranvil.resourcepacks.ConfigManager.*;

public class CEMFavoriteWriter {

    private static CEMCollection readConfig() {

        CEMCollection cemCollection;

        try (FileReader reader = new FileReader(getPath())) {
            cemCollection = gson.fromJson(reader, CEMCollection.class);
            reader.close();
            return cemCollection;
        } catch (IOException s) {
            return createConfig();
        }
    }

    public static LinkedHashSet<CEMItem> getFavoriteItems(){
        CEMCollection cemCollection = readConfig();

        LinkedHashSet<CEMItem> cemItems = new LinkedHashSet<>();
        LinkedHashSet<String> packs = PackManager.getPackNamesWithServer();

        for(CEMItem item : cemCollection.getItems()){
            if(packs.contains(item.getResourcePack())){
                cemItems.add(item);
            }
        }

        return cemItems;

    }

    private static CEMCollection createConfig() {
        CEMCollection cemCollection = new CEMCollection(new LinkedHashSet<>());
        String json = gson.toJson(cemCollection);

        try (FileWriter writer = new FileWriter(getPath())) {
            writer.write(json);
            MYLOGGER.info("Created favorite config! (CEM Favorite)");
        } catch (IOException e) {
            e.printStackTrace();
            MYLOGGER.info("Failed to create favorite config! (CEM Favorite)");
            return cemCollection;
        }
        return cemCollection;
    }

    public static void removeItem(CEMItem s) {
        LinkedHashSet<CEMItem> cemItems = readConfig().getItems();
        cemItems.remove(s);

        CEMCollection cemCollection = new CEMCollection(cemItems);
        String json = gson.toJson(cemCollection);

        try (FileWriter writer = new FileWriter(getPath())) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addItem(CEMItem s) {

        LinkedHashSet<CEMItem> cemItems = new LinkedHashSet<>(readConfig().getItems());
        if(s.getResourcePack().equals("Server")){
            s.setServerResourcePack(PackManager.getServerResourcePack().get());
        }
        cemItems.add(s);
        CEMCollection cemCollection = new CEMCollection(cemItems);


        String json = gson.toJson(cemCollection);

        try (FileWriter writer = new FileWriter(getPath())) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getPath() {
        return pathToConfig + "cem_favorite" + jsonFormat;
    }
}
