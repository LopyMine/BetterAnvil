package net.lopymine.betteranvil.resourcepacks.cmd;

import net.lopymine.betteranvil.resourcepacks.PackManager;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;
import static net.lopymine.betteranvil.resourcepacks.ConfigManager.*;

public class CMDParser {

    public static ArrayList<CMDItem> parseCMDItems(){
        return getCMDItems();
    }

    private static ArrayList<CMDItem> getCMDItems(){
        ArrayList<CMDItem> customModelDataItems = new ArrayList<>();

        for(String rp : PackManager.getPackNamesWithServer()){
            if(rp.equals("server")){
                try (FileReader reader = new FileReader(pathToCMDServerConfigFolder + PackManager.getServerResourcePack().get() + jsonFormat)) {
                    customModelDataItems.addAll(setCMDItemsServerRP(gson.fromJson(reader, CMDCollection.class)));
                } catch (IOException ignored) {
                    MYLOGGER.warn("Not found server json file: " + pathToCMDServerConfigFolder + rp + "(" + PackManager.getServerResourcePack().get() + ")" + jsonFormat);
                }
            } else {
                try (FileReader reader = new FileReader(pathToCMDConfigFolder + rp + jsonFormat)) {
                    customModelDataItems.addAll(setCMDItemsRP( gson.fromJson(reader, CMDCollection.class), rp));
                } catch (IOException ignored) {
                    MYLOGGER.warn("Not found json file: " + pathToCMDConfigFolder + rp + jsonFormat);
                }
            }

        }

        return customModelDataItems;

    }

    private static ArrayList<CMDItem> setCMDItemsRP(CMDCollection customModelDataCollection, String resourcePackName) {
        ArrayList<CMDItem> customModelDataItems = new ArrayList<>();
        for(CMDItem dataItem : customModelDataCollection.getItems()){
            dataItem.setResourcePack(resourcePackName);
            customModelDataItems.add(dataItem);
        }
        return customModelDataItems;
    }

    private static ArrayList<CMDItem> setCMDItemsServerRP(CMDCollection customModelDataCollection) {
        ArrayList<CMDItem> customModelDataItems = new ArrayList<>();
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
