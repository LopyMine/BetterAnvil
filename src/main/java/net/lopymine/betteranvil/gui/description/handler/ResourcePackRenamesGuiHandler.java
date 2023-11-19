package net.lopymine.betteranvil.gui.description.handler;

import net.lopymine.betteranvil.config.resourcepacks.cit.CITItem;
import net.lopymine.betteranvil.utils.*;

import java.util.LinkedHashSet;

public class ResourcePackRenamesGuiHandler extends CITGuiHandler {
    @Override
    public LinkedHashSet<CITItem> getSearchByItem(String search, LinkedHashSet<CITItem> list) {
        return new LinkedHashSet<>(list.stream().filter(item -> {
            for (String i : item.getItems()) {
                String itemName = ItemUtils.getItemByName(i).asItem().getName().getString();
                if (StringUtils.e(itemName, search)) {
                    return true;
                }
            }

            return false;
        }).toList());
    }
}
