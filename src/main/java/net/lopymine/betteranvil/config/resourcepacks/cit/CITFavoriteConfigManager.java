package net.lopymine.betteranvil.config.resourcepacks.cit;

import net.minecraft.item.ItemStack;

import net.lopymine.betteranvil.config.resourcepacks.*;
import net.lopymine.betteranvil.resourcepacks.*;
import net.lopymine.betteranvil.utils.ItemUtils;

import java.util.*;

public class CITFavoriteConfigManager extends FavoriteConfigManager<CITItem, CITConfigSet> {
    private static final CITFavoriteConfigManager INSTANCE = new CITFavoriteConfigManager();

    private CITFavoriteConfigManager() {
        super(ResourcePackType.CIT, CITConfigSet.class, new CITConfigSet(new LinkedHashSet<>()));
    }

    public static CITFavoriteConfigManager getInstance() {
        return INSTANCE;
    }

    public LinkedHashSet<CITItem> getWithItem(LinkedHashSet<CITItem> list, ItemStack itemStack) {
        return new LinkedHashSet<>(list.stream().filter(item -> {
            String itemId = ItemUtils.getID(itemStack.getItem());
            return item.getItems().contains(itemId);
        }).toList());
    }

    public LinkedHashSet<CITItem> getResourcePacksItems(LinkedHashSet<CITItem> list, LinkedHashSet<String> resourcePacks) {
        return new LinkedHashSet<>(list.stream().filter(item -> {
            String resourcePack = item.getResourcePack();
            String serverResourcePack = item.getServerResourcePack();

            if (resourcePacks.contains("server") && !ServerResourcePackManager.getServer().isEmpty()) {
                if (Objects.equals(resourcePack, "Server") && Objects.equals(serverResourcePack, ServerResourcePackManager.getServer())) {
                    return true;
                }
            }

            return resourcePacks.contains(resourcePack);
        }).toList());
    }
}
