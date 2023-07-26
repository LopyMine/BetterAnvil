package net.lopymine.betteranvil.resourcepacks.cem;

import net.lopymine.betteranvil.resourcepacks.PackManager;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;
import static net.lopymine.betteranvil.resourcepacks.ConfigManager.*;
import static net.lopymine.betteranvil.resourcepacks.ConfigManager.jsonFormat;

public class CEMParser {
    public static LinkedHashSet<CEMItem> parseCEMItems(){
        return getCEMItems();
    }

    private static LinkedHashSet<CEMItem> getCEMItems(){
        LinkedHashSet<CEMItem> cemItems = new LinkedHashSet<>();

        for(String rp : PackManager.getPackNamesWithServer()){
            if(rp.equals("server")){
                try (FileReader reader = new FileReader(pathToCEMServerConfigFolder + PackManager.getServerResourcePack().get() + jsonFormat)) {
                    cemItems.addAll(setItemsServerPack(gson.fromJson(reader, CEMCollection.class)));
                } catch (IOException ignored) {
                }
            } else {
                try (FileReader reader = new FileReader(pathToCEMConfigFolder + rp + jsonFormat)) {
                    cemItems.addAll(setItemsPack( gson.fromJson(reader, CEMCollection.class), rp));
                } catch (IOException ignored) {
                }
            }

        }

        return cemItems;

    }
    private static LinkedHashSet<CEMItem> setItemsPack(CEMCollection customModelDataCollection, String resourcePackName) {
        LinkedHashSet<CEMItem> cemItems = new LinkedHashSet<>();
        for(CEMItem dataItem : customModelDataCollection.getItems()){
            dataItem.setResourcePack(resourcePackName);
            cemItems.add(dataItem);
        }
        return cemItems;
    }

    private static LinkedHashSet<CEMItem> setItemsServerPack(CEMCollection items) {
        LinkedHashSet<CEMItem> customModelDataItems = new LinkedHashSet<>();
        if(PackManager.getServerResourcePack().get() == null){
            MYLOGGER.warn("Failed to set server resource pack because server resource pack is null");
            return customModelDataItems;
        }


        for(CEMItem dataItem : items.getItems()){
            dataItem.setServerResourcePack(PackManager.getServerResourcePack().get());
            dataItem.setResourcePack("Server");
            customModelDataItems.add(dataItem);
        }
        return customModelDataItems;
    }
}
