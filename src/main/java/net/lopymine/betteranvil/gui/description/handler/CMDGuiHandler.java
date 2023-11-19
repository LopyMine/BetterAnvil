package net.lopymine.betteranvil.gui.description.handler;

import net.lopymine.betteranvil.gui.description.interfaces.GuiHandler;
import net.lopymine.betteranvil.resourcepacks.*;
import net.lopymine.betteranvil.config.resourcepacks.cmd.CMDItem;
import net.lopymine.betteranvil.utils.ItemUtils;

import java.util.*;

public class CMDGuiHandler implements GuiHandler<CMDItem> {
    @Override
    public LinkedHashSet<CMDItem> getSearch(String search, LinkedHashSet<CMDItem> list) {
        return new LinkedHashSet<>(list.stream().filter(item -> {
            return String.valueOf(item.getId()).contains(search);
        }).toList());
    }

    @Override
    public LinkedHashSet<CMDItem> getSearchByItem(String input, LinkedHashSet<CMDItem> list) {
        return new LinkedHashSet<>(list.stream().filter(item -> {
            String search = input.replace("#item", "").toLowerCase().replace('ё', 'е');
            String itemName = ItemUtils.getItemByName(item.getItem()).asItem().getName().getString().toLowerCase().replace('ё', 'е');

            return itemName.contains(search);
        }).toList());
    }

    @Override
    public LinkedHashSet<CMDItem> getSearchByPack(String input, LinkedHashSet<CMDItem> list) {
        return new LinkedHashSet<>(list.stream().filter(item -> {
            String resourcePack = item.getResourcePack();
            if (resourcePack == null) {
                return false;
            }
            String search = input.replace("#pack", "").replace("#resourcepack", "").toLowerCase().replace("ё", "е");
            String pack = resourcePack.toLowerCase().replace("ё", "е");

            return pack.contains(search);
        }).toList());
    }

    @Override
    public LinkedHashSet<CMDItem> getSearchByEnchantments(String search, LinkedHashSet<CMDItem> list) {
        return list;
    }

    @Override
    public LinkedHashSet<CMDItem> getSearchByLore(String search, LinkedHashSet<CMDItem> list) {
        return list;
    }

    @Override
    public LinkedHashSet<CMDItem> getSearchByCount(String search, LinkedHashSet<CMDItem> list) {
        return list;
    }

    @Override
    public LinkedHashSet<CMDItem> getSearchByDamage(String search, LinkedHashSet<CMDItem> list) {
        return list;
    }

    @Override
    public ResourcePackType getType() {
        return ResourcePackType.CMD;
    }
}
