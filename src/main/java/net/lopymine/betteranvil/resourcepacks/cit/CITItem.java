package net.lopymine.betteranvil.resourcepacks.cit;

import org.apache.commons.lang3.StringEscapeUtils;

import java.util.*;

public class CITItem {
    private final String items;
    private final String customname;
    private final String damage;
    private final String enchantments;
    private List<String> lore;
    private String resourcePack = null;
    private String serverResourcePack = null;

    public CITItem(String item, String customname, String other) {
        this.items = item;
        this.customname = customname;
        this.damage = other;
        this.enchantments = null;
        lore = null;
    }

    public LinkedHashSet<String> getCustomNames() {
        String[] words = customname.split("\\|"); // split the string by "|" delimiter

        LinkedHashSet<String> names = new LinkedHashSet<>();
        for(String n : Arrays.stream(words).toList()){
            if(!n.equals(" ")){
                String nn = n.strip();
                names.add(StringEscapeUtils.unescapeJava(nn));
            }
        }
        return names;
    }

    public String getCustomName() {
        return customname;
    }

    public String getItem() {

        return items;
    }

    public List<String> getItems() {
        String[] names = items.split(" "); // split the string by "|" delimiter
        return Arrays.stream(names).toList();
    }

    public void setResourcePack(String resourcePack){
        this.resourcePack = resourcePack;
    }

    public String getResourcePack() {
        return resourcePack;
    }

    public void setServerResourcePack(String serverResourcePack) {
        this.serverResourcePack = serverResourcePack;
    }

    public String getServerResourcePack() {
        return serverResourcePack;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public List<String> getLore() {
        return lore;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CITItem item = (CITItem) o;
        return items.equals(item.items) && customname.equals(item.customname) && Objects.equals(damage, item.damage) && Objects.equals(enchantments, item.enchantments) && Objects.equals(lore, item.lore) && Objects.equals(resourcePack, item.resourcePack) && Objects.equals(serverResourcePack, item.serverResourcePack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, customname, damage, enchantments, lore, resourcePack, serverResourcePack);
    }
}
