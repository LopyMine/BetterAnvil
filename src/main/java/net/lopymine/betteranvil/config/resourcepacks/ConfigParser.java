package net.lopymine.betteranvil.config.resourcepacks;

import com.google.gson.*;

import net.lopymine.betteranvil.resourcepacks.*;
import net.lopymine.betteranvil.utils.ResourcePackUtils;

import java.io.*;
import java.util.*;
import org.jetbrains.annotations.Nullable;

import static net.lopymine.betteranvil.BetterAnvil.LOGGER;
import static net.lopymine.betteranvil.config.resourcepacks.ResourcePackConfigsManager.JSON_FORMAT;

public abstract class ConfigParser<I extends ResourcePackItem<I>, K extends ConfigSet<I>> {
    protected static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    protected final ResourcePackType type;
    protected final Class<K> cl;
    protected final K valueIfNew;

    protected ConfigParser(ResourcePackType type, Class<K> cl, K valueIfNew) {
        this.type = type;
        this.cl = cl;
        this.valueIfNew = valueIfNew;
    }

    protected HashMap<String, LinkedHashSet<I>> parseResourcePacksItems(@Nullable List<String> resourcePacks) {
        HashMap<String, LinkedHashSet<I>> map = new HashMap<>();
        LinkedHashSet<I> allItems = new LinkedHashSet<>();

        for (String resourcePack : ResourcePackUtils.getResourcePacksByType(type)) {
            if (resourcePacks != null && !resourcePacks.contains(resourcePack)) {
                continue;
            }
            if (resourcePack.equals("server") && !ServerResourcePackManager.getServer().isEmpty()) {
                LinkedHashSet<I> configList = parseItemsFromConfig(ServerResourcePackManager.getServer(), ResourcePackConfigsManager.getServerConfigPath(type));
                LinkedHashSet<I> list = setResourcePackWithServerToItems(configList);
                allItems.addAll(list);
                map.put(resourcePack, list);
                continue;
            }

            LinkedHashSet<I> configList = parseItemsFromConfig(ResourcePackUtils.getResourcePackName(resourcePack), ResourcePackConfigsManager.getConfigPath(type));
            LinkedHashSet<I> list = setResourcePackToItems(configList, ResourcePackUtils.getResourcePackNameWithZip(resourcePack));

            LinkedHashSet<I> combList = new LinkedHashSet<>(list);
            combList.addAll(allItems);
            allItems = combList;

            map.put(resourcePack, list);
        }

        map.put("all", allItems);
        return map;
    }

    protected LinkedHashSet<I> parseItemsFromConfig(String config, String path) {
        LinkedHashSet<I> list = new LinkedHashSet<>();

        try (FileReader reader = new FileReader(path + config + JSON_FORMAT)) {
            K set = GSON.fromJson(reader, cl);
            return set.getItems();
        } catch (FileNotFoundException e) {
            LOGGER.error("[{} Parser] Failed to find {} config file for resource pack: {}", type, type, config);
        } catch (Exception e) {
            LOGGER.error("[{} Parser] Failed to read {} config file for resource pack: {}", type, type, config);
            e.printStackTrace();
        }

        return list;
    }

    public LinkedHashSet<I> setResourcePackToItems(LinkedHashSet<I> items, String pack) {
        items.forEach(item -> item.setResourcePack(pack));
        return items;
    }

    public LinkedHashSet<I> setResourcePackWithServerToItems(LinkedHashSet<I> items) {
        if (ServerResourcePackManager.getServer().isEmpty()) {
            LOGGER.warn("[{} Parser] Failed to set server resource pack because server resource pack is empty!", type);
            return items;
        }

        items.forEach(item -> {
            item.setServerResourcePack(ServerResourcePackManager.getServer());
            item.setResourcePack("Server");
        });

        return items;
    }
}
