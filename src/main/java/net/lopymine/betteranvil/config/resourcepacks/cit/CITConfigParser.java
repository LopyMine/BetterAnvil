package net.lopymine.betteranvil.config.resourcepacks.cit;

import net.minecraft.item.ItemStack;

import net.lopymine.betteranvil.config.BetterAnvilConfig;
import net.lopymine.betteranvil.config.resourcepacks.ConfigParser;
import net.lopymine.betteranvil.config.resourcepacks.cit.CITItem.Builder;
import net.lopymine.betteranvil.resourcepacks.ResourcePackType;
import net.lopymine.betteranvil.utils.ItemUtils;

import java.util.*;
import org.jetbrains.annotations.Nullable;

public class CITConfigParser extends ConfigParser<CITItem, CITConfigSet> {
    private static final CITConfigParser INSTANCE = new CITConfigParser();

    private CITConfigParser() {
        super(ResourcePackType.CIT, CITConfigSet.class, new CITConfigSet(new LinkedHashSet<>()));
    }

    public static CITConfigParser getInstance() {
        return INSTANCE;
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

    public HashMap<String, LinkedHashSet<CITItem>> getResourcePacksItems(@Nullable List<String> resourcePacks, BetterAnvilConfig config) {
        return transformList(parseResourcePacksItems(resourcePacks), null, config);
    }

    public HashMap<String, LinkedHashSet<CITItem>> getResourcePacksItems(ItemStack itemStack, @Nullable List<String> resourcePacks, BetterAnvilConfig config) {
        return transformList(parseResourcePacksItems(resourcePacks), ItemUtils.getID(itemStack.getItem()), config);
    }

    public LinkedHashSet<CITItem> parseItemsFromConfig(String configFileName, String path, BetterAnvilConfig config) {
        return transformList(parseItemsFromConfig(configFileName, path), null, config);
    }

    public LinkedHashSet<CITItem> parseItemsFromConfig(String configFileName, String path, ItemStack itemStack, BetterAnvilConfig config) {
        return transformList(parseItemsFromConfig(configFileName, path), ItemUtils.getID(itemStack.getItem()), config);
    }

    private LinkedHashSet<CITItem> transformList(LinkedHashSet<CITItem> items, @Nullable String itemId, BetterAnvilConfig config) {
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

    private HashMap<String, LinkedHashSet<CITItem>> transformList(HashMap<String, LinkedHashSet<CITItem>> map, @Nullable String itemId, BetterAnvilConfig config) {
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
}
