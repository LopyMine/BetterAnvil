package net.lopymine.betteranvil.gui.description.handler;

import net.lopymine.betteranvil.config.resourcepacks.cmd.CMDItem;
import net.lopymine.betteranvil.gui.description.interfaces.GuiHandler;
import net.lopymine.betteranvil.resourcepacks.ResourcePackType;
import net.lopymine.betteranvil.utils.*;

import java.util.LinkedHashSet;

public class CMDGuiHandler implements GuiHandler<CMDItem> {
    @Override
    public LinkedHashSet<CMDItem> getSearch(String search, LinkedHashSet<CMDItem> list) {
        return new LinkedHashSet<>(list.stream().filter(item -> {
            return String.valueOf(item.getId()).contains(search);
        }).toList());
    }

    @Override
    public LinkedHashSet<CMDItem> getSearchByItem(String search, LinkedHashSet<CMDItem> list) {
        if (search.isEmpty()) {
            return list;
        }
        return new LinkedHashSet<>(list.stream().filter(item -> {
            String string = ItemUtils.getItemByName(item.getItem()).asItem().getName().getString();
            return StringUtils.c(string, search);
        }).toList());
    }

    @Override
    public LinkedHashSet<CMDItem> getSearchByPack(String search, LinkedHashSet<CMDItem> list) {
        return new LinkedHashSet<>(list.stream().filter(item -> {
            String resourcePack = item.getResourcePack();
            if (resourcePack == null) {
                return false;
            }

            return StringUtils.c(resourcePack, search);
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
