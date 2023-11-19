package net.lopymine.betteranvil.gui.description.interfaces;

import net.lopymine.betteranvil.resourcepacks.ResourcePackType;

import java.util.LinkedHashSet;

public interface GuiHandler<I> {
    LinkedHashSet<I> getSearch(String search, LinkedHashSet<I> list);

    LinkedHashSet<I> getSearchByItem(String search, LinkedHashSet<I> list);

    LinkedHashSet<I> getSearchByPack(String search, LinkedHashSet<I> list);

    LinkedHashSet<I> getSearchByEnchantments(String search, LinkedHashSet<I> list);

    LinkedHashSet<I> getSearchByLore(String search, LinkedHashSet<I> list);

    LinkedHashSet<I> getSearchByCount(String search, LinkedHashSet<I> list);

    LinkedHashSet<I> getSearchByDamage(String search, LinkedHashSet<I> list);

    ResourcePackType getType();
}
