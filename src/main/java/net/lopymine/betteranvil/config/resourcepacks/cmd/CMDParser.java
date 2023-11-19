package net.lopymine.betteranvil.config.resourcepacks.cmd;

import net.lopymine.betteranvil.config.resourcepacks.*;
import net.lopymine.betteranvil.resourcepacks.*;
import net.lopymine.betteranvil.utils.ResourcePackUtils;

import java.io.FileReader;
import java.util.*;

import static net.lopymine.betteranvil.BetterAnvil.LOGGER;
import static net.lopymine.betteranvil.config.resourcepacks.ResourcePackConfigsManager.*;

public class CMDParser {

    public static HashMap<String, LinkedHashSet<CMDItem>> getAllItems() {
        return getCMDItems();
    }

    private static HashMap<String, LinkedHashSet<CMDItem>> getCMDItems() {
        HashMap<String, LinkedHashSet<CMDItem>> map = new HashMap<>();

        for (String resourcePack : ResourcePackUtils.getResourcePacksByType(ResourcePackType.CMD)) {
            if (resourcePack.equals("server")) {
                try (FileReader reader = new FileReader(PATH_TO_CMD_SERVER_CONFIG_FOLDER + ServerResourcePackManager.getServer() + JSON_FORMAT)) {
                    CMDConfigSet set = GSON.fromJson(reader, CMDConfigSet.class);
                    LinkedHashSet<CMDItem> list = setResourcePackWithServerToItems(set.getItems());
                    map.put(resourcePack, list);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                continue;
            }

            String name = ResourcePackUtils.getResourcePackName(resourcePack);
            String nameWithZip = ResourcePackUtils.getResourcePackNameWithZip(resourcePack);

            try (FileReader reader = new FileReader(PATH_TO_CMD_CONFIG_FOLDER + name + JSON_FORMAT)) {
                CMDConfigSet set = ConfigWriter.GSON.fromJson(reader, CMDConfigSet.class);
                LinkedHashSet<CMDItem> list = setResourcePackToItems(set.getItems(), nameWithZip);
                map.put(resourcePack, list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return map;
    }

    private static LinkedHashSet<CMDItem> setResourcePackToItems(LinkedHashSet<CMDItem> items, String resourcePackName) {
        items.forEach(item -> item.setResourcePack(resourcePackName));
        return items;
    }

    private static LinkedHashSet<CMDItem> setResourcePackWithServerToItems(LinkedHashSet<CMDItem> items) {
        if (ServerResourcePackManager.getServer().isEmpty()) {
            LOGGER.warn("Failed to set server resource pack because server resource pack is null");
            return items;
        }

        items.forEach(item -> {
            item.setServerResourcePack(ServerResourcePackManager.getServer());
            item.setResourcePack("Server");
        });

        return items;
    }
}
