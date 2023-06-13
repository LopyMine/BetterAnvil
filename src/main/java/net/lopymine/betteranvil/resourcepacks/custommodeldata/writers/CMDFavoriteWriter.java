package net.lopymine.betteranvil.resourcepacks.custommodeldata.writers;


import net.lopymine.betteranvil.resourcepacks.PackManager;
import net.lopymine.betteranvil.resourcepacks.custommodeldata.CMDCollection;
import net.lopymine.betteranvil.resourcepacks.custommodeldata.CMDItem;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;
import static net.lopymine.betteranvil.resourcepacks.ConfigManager.*;

public class CMDFavoriteWriter {

    private static CMDCollection readConfig() {

        CMDCollection cmdCollection = new CMDCollection(new ArrayList<>());

        try (FileReader reader = new FileReader(getPath())) {
            cmdCollection = gson.fromJson(reader, CMDCollection.class);
            reader.close();
            return cmdCollection;
        } catch (IOException s) {
            return createConfig();
        }
    }

    public static ArrayList<CMDItem> getFavoriteItems(){
        CMDCollection cmdCollection = readConfig();
        ArrayList<CMDItem> cmdItems = new ArrayList<>();
        ArrayList<String> packs = PackManager.getPacks();

        for(CMDItem item : cmdCollection.getItems()){
            if(packs.contains(item.getResourcePack())){
                cmdItems.add(item);
            }
        }

        return cmdItems;

    }

    private static CMDCollection createConfig() {
        CMDCollection cmdCollection = new CMDCollection(new ArrayList<>());
        MYLOGGER.info("Create favorite config! (Favorite)");
        String json = gson.toJson(cmdCollection);

        try (FileWriter writer = new FileWriter(getPath())) {
            writer.write(json);
        } catch (IOException e) {
            MYLOGGER.info("Failed to create favorite config! (Favorite)");
            e.printStackTrace();
            return cmdCollection;
        }
        return cmdCollection;
    }

    public static void removeItem(Collection<CMDItem> cmdItems, CMDItem s) {
        if(cmdItems.contains(s)){
            System.out.println(cmdItems.stream().toList().indexOf(s));
        }
        cmdItems.remove(s);


        CMDCollection cmdCollection = new CMDCollection(new ArrayList<>(cmdItems));
        String json = gson.toJson(cmdCollection);

        try (FileWriter writer = new FileWriter(getPath())) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addItem(CMDItem s) {

        Collection<CMDItem> cmdItems = new ArrayList<>(readConfig().getItems());
        if(s.getResourcePack().equals("Server")){
            s.setServerResourcePack(PackManager.getServerResourcePack().get());
        }
        cmdItems.add(s);
        CMDCollection cmdCollection = new CMDCollection(new ArrayList<>(cmdItems));


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