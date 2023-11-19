package net.lopymine.betteranvil.config.resourcepacks.cmd;

import net.minecraft.item.ItemStack;

import net.lopymine.betteranvil.config.resourcepacks.FavoriteConfigManager;
import net.lopymine.betteranvil.resourcepacks.*;
import net.lopymine.betteranvil.utils.ItemUtils;

import java.util.*;

public class CMDFavoriteConfigManager extends FavoriteConfigManager<CMDItem, CMDConfigSet> {
    private static final CMDFavoriteConfigManager INSTANCE = new CMDFavoriteConfigManager();

    public CMDFavoriteConfigManager() {
        super(ResourcePackType.CMD, CMDConfigSet.class, new CMDConfigSet(new LinkedHashSet<>()));
    }

    public static CMDFavoriteConfigManager getInstance() {
        return INSTANCE;
    }

    public LinkedHashSet<CMDItem> getWithItem(LinkedHashSet<CMDItem> list, ItemStack itemStack) {
        return new LinkedHashSet<>(list.stream().filter(item -> {
            String itemId = ItemUtils.getID(itemStack.getItem());
            return item.getItem().equals(itemId);
        }).toList());
    }

    public LinkedHashSet<CMDItem> getResourcePacksItems(LinkedHashSet<CMDItem> list, LinkedHashSet<String> resourcePacks) {
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
