package net.lopymine.betteranvil.config.resourcepacks;

import java.util.LinkedHashSet;

public class ConfigSet<I> {
    private final LinkedHashSet<I> items;

    public ConfigSet(LinkedHashSet<I> items) {
        this.items = items;
    }

    public LinkedHashSet<I> getItems() {
        return items;
    }
}
