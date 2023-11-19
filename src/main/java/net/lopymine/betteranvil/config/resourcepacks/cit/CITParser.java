package net.lopymine.betteranvil.config.resourcepacks.cit;

import net.minecraft.item.ItemStack;

import net.lopymine.betteranvil.config.BetterAnvilConfig;
import net.lopymine.betteranvil.config.resourcepacks.cit.CITItem.Builder;
import net.lopymine.betteranvil.resourcepacks.*;
import net.lopymine.betteranvil.utils.*;

import java.io.FileReader;
import java.util.*;
import org.jetbrains.annotations.Nullable;

import static net.lopymine.betteranvil.BetterAnvil.LOGGER;
import static net.lopymine.betteranvil.config.resourcepacks.ResourcePackConfigsManager.*;

public class CITParser {

    public static LinkedHashSet<CITItem> parseItemsFromConfig(String configFileName, String path, BetterAnvilConfig config) {
        return transformList(getItemsFromConfig(configFileName, path), null, config);
    }

    public static LinkedHashSet<CITItem> parseItemsFromConfig(String configFileName, String path, ItemStack itemStack, BetterAnvilConfig config) {
        return transformList(getItemsFromConfig(configFileName, path), ItemUtils.getID(itemStack.getItem()), config);
    }

    public static HashMap<String, LinkedHashSet<CITItem>> parseAllItems(BetterAnvilConfig config) {
        return transformList(getAllItems(config), null, config);
    }

    public static HashMap<String, LinkedHashSet<CITItem>> parseAllItems(ItemStack itemStack, BetterAnvilConfig config) {
        return transformList(getAllItems(config), ItemUtils.getID(itemStack.getItem()), config);
    }

    private static HashMap<String, LinkedHashSet<CITItem>> getAllItems(BetterAnvilConfig config) {
        HashMap<String, LinkedHashSet<CITItem>> map = new HashMap<>();
        LinkedHashSet<CITItem> allItems = new LinkedHashSet<>();

        for (String resourcePack : ResourcePackUtils.getResourcePacksByType(ResourcePackType.CIT)) {
            if (resourcePack.equals("server")) {
                if (!ServerResourcePackManager.getServer().isEmpty()) {
                    LinkedHashSet<CITItem> items = parseItemsFromConfig(ServerResourcePackManager.getServer(), PATH_TO_CIT_SERVER_CONFIG_FOLDER, config);

                    if (!items.isEmpty()) {
                        LinkedHashSet<CITItem> list = setResourcePackWithServerToItems(items);
                        allItems.addAll(list);
                        map.put(resourcePack, list);
                    } else {
                        LOGGER.warn("[Parser/CIT] Failed to find CIT items for server resource pack!");
                    }
                } else {
                    LOGGER.error("[Parser/CIT] Server resource pack is null!");
                }
                continue;
            }

            String name = ResourcePackUtils.getResourcePackName(resourcePack);
            String nameWithZip = ResourcePackUtils.getResourcePackNameWithZip(resourcePack);

            try (FileReader reader = new FileReader(PATH_TO_CIT_CONFIG_FOLDER + name + JSON_FORMAT)) {
                CITConfigSet set = GSON.fromJson(reader, CITConfigSet.class);
                LinkedHashSet<CITItem> list = setResourcePackToItems(set.getItems(), nameWithZip);
                allItems.addAll(list);
                map.put(resourcePack, list);
            } catch (Exception e) {
                LOGGER.error("[Parser/CIT] Failed to read CIT config file for resource pack: " + resourcePack, e);
            }
        }

        map.put("all", allItems);
        return map;
    }

    private static LinkedHashSet<CITItem> getItemsFromConfig(String config, String path) {
        LinkedHashSet<CITItem> citItems = new LinkedHashSet<>();

        if (!hasConfig(path + config + JSON_FORMAT)) {
            return new LinkedHashSet<>();
        }

        try (FileReader reader = new FileReader(path + config + JSON_FORMAT)) {
            CITConfigSet set = GSON.fromJson(reader, CITConfigSet.class);

            if (path.equals(PATH_TO_CIT_SERVER_CONFIG_FOLDER)) {
                citItems.addAll(setResourcePackWithServerToItems(set.getItems()));
            } else {
                citItems.addAll(setResourcePackToItems(set.getItems(), config));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return citItems;
    }

    public static LinkedHashSet<CITItem> setResourcePackToItems(LinkedHashSet<CITItem> items, String pack) {
        items.forEach(item -> item.setResourcePack(pack));
        return items;
    }

    public static LinkedHashSet<CITItem> setResourcePackWithServerToItems(LinkedHashSet<CITItem> items) {
        if (ServerResourcePackManager.getServer().isEmpty()) {
            LOGGER.warn("[Parser/CIT] Failed to set server resource pack because server resource pack is empty!");
            return items;
        }

        items.forEach(item -> {
            item.setServerResourcePack(ServerResourcePackManager.getServer());
            item.setResourcePack("Server");
        });

        return items;
    }

    public static LinkedHashSet<CITItem> transformList(LinkedHashSet<CITItem> items, @Nullable String itemId, BetterAnvilConfig config) {
        LinkedHashSet<CITItem> list = new LinkedHashSet<>();

        for (CITItem citItem : items) {
            if (itemId != null) {
                if (!citItem.getItems().contains(itemId)) {
                    continue;
                }
            }

            LinkedHashSet<CITItem> item = transformItem(citItem, config);

            if (!item.isEmpty()) {
                list.addAll(item);
            }
        }

        return list;
    }

    public static HashMap<String, LinkedHashSet<CITItem>> transformList(HashMap<String, LinkedHashSet<CITItem>> map, @Nullable String itemId, BetterAnvilConfig config) {
        HashMap<String, LinkedHashSet<CITItem>> transformedMap = new HashMap<>();

        for (String resourcePack : map.keySet()) {
            LinkedHashSet<CITItem> items = map.get(resourcePack);
            if (items == null) {
                continue;
            }

            LinkedHashSet<CITItem> transformedItems = transformList(items, itemId, config);
            transformedMap.put(resourcePack, transformedItems);
        }

        return transformedMap;
    }

    private static LinkedHashSet<CITItem> transformItem(CITItem citItem, BetterAnvilConfig config) {
        switch (config.renamesCountEnum) {
            case ALL -> {
                return transformItemByAllRenames(citItem);
            }
            case ONE -> {
                return transformItemByCustomCount(citItem, 1);
            }
            case CUSTOM -> {
                if (config.customRenamesCount == 100) {
                    return transformItemByAllRenames(citItem);
                }
                return transformItemByCustomCount(citItem, config.customRenamesCount);
            }
        }

        return new LinkedHashSet<>();
    }

    private static LinkedHashSet<CITItem> transformItemByAllRenames(CITItem citItem) {
        LinkedHashSet<CITItem> list = new LinkedHashSet<>();

        for (String name : citItem.getCustomNames()) {
            CITItem.Builder builder = new Builder()
                    .items(citItem.getItem())
                    .customName(name)
                    .count(citItem.getCountMetaData())
                    .damage(citItem.getDamageMetaData())
                    .enchantments(citItem.getEnchantments())
                    .enchantmentLevels(citItem.getEnchantmentLevels())
                    .lore(citItem.getLore())
                    .hand(citItem.getHand())
                    .resourcePack(citItem.getResourcePack())
                    .serverResourcePack(citItem.getServerResourcePack());

            CITItem item = builder.build();

            if (item != null) {
                list.add(item);
            }
        }

        return list;
    }

    private static LinkedHashSet<CITItem> transformItemByCustomCount(CITItem citItem, int count) {
        LinkedHashSet<CITItem> list = new LinkedHashSet<>();

        for (String name : citItem.getCustomNames()) {
            if (list.size() == count) {
                return list;
            }

            CITItem.Builder builder = new Builder()
                    .items(citItem.getItem())
                    .customName(name)
                    .count(citItem.getCountMetaData())
                    .damage(citItem.getDamageMetaData())
                    .enchantments(citItem.getEnchantments())
                    .enchantmentLevels(citItem.getEnchantmentLevels())
                    .lore(citItem.getLore())
                    .hand(citItem.getHand())
                    .resourcePack(citItem.getResourcePack())
                    .serverResourcePack(citItem.getServerResourcePack());

            CITItem item = builder.build();

            if (item != null) {
                list.add(item);
            }
        }

        return list;
    }
}