package net.lopymine.betteranvil.resourcepacks.cmd;

import net.lopymine.betteranvil.resourcepacks.PackManager;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;
import static net.lopymine.betteranvil.resourcepacks.ConfigManager.*;

public class CMDParser {

    public static LinkedHashSet<CMDItem> parseCMDItems(){
        return getCMDItems();
    }

    private static LinkedHashSet<CMDItem> getCMDItems(){
        LinkedHashSet<CMDItem> customModelDataItems = new LinkedHashSet<>();

        for(String rp : PackManager.getPackNamesWithServer()){
            if(rp.equals("server")){
                try (FileReader reader = new FileReader(pathToCMDServerConfigFolder + PackManager.getServerResourcePack().get() + jsonFormat)) {
                    customModelDataItems.addAll(setItemsServerPack(gson.fromJson(reader, CMDCollection.class)));
                } catch (IOException ignored) {
                }
            } else {
                try (FileReader reader = new FileReader(pathToCMDConfigFolder + rp + jsonFormat)) {
                    customModelDataItems.addAll(setItemsPack( gson.fromJson(reader, CMDCollection.class), rp));
                } catch (IOException ignored) {
                }
            }

        }

        return customModelDataItems;

    }

    private static LinkedHashSet<CMDItem> setItemsPack(CMDCollection customModelDataCollection, String resourcePackName) {
        LinkedHashSet<CMDItem> customModelDataItems = new LinkedHashSet<>();
        for(CMDItem dataItem : customModelDataCollection.getItems()){
            dataItem.setResourcePack(resourcePackName);
            customModelDataItems.add(dataItem);
        }
        return customModelDataItems;
    }

    private static LinkedHashSet<CMDItem> setItemsServerPack(CMDCollection customModelDataCollection) {
        LinkedHashSet<CMDItem> customModelDataItems = new LinkedHashSet<>();
        if(PackManager.getServerResourcePack().get() == null){
            MYLOGGER.warn("Failed to set server resource pack because server resource pack is null");
            return customModelDataItems;
        }


        for(CMDItem dataItem : customModelDataCollection.getItems()){
            dataItem.setServerResourcePack(PackManager.getServerResourcePack().get());
            dataItem.setResourcePack("Server");
            customModelDataItems.add(dataItem);
        }
        return customModelDataItems;
    }

}
