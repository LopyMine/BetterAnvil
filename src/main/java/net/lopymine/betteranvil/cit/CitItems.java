package net.lopymine.betteranvil.cit;

import java.util.ArrayList;
import java.util.Arrays;

public class CitItems {
    private final String items;
    private final String customname;
    private final String damage;
    private final String enchantments;

    public CitItems(String item, String customname, String other) {
        this.items = item;
        this.customname = customname;
        this.damage = other;
        this.enchantments = null;
    }

    public ArrayList<String> getCustomNames() {
        String[] words = customname.split("\\|"); // split the string by "|" delimiter
        return new ArrayList<>(Arrays.asList(words));
    }

    public String getCustomName() {
        return customname;
    }

    public String getItem() {

        return items;
    }

    public ArrayList<String> getItems() {
        String[] names = items.split(" "); // split the string by "|" delimiter
        return new ArrayList<String>(Arrays.asList(names));
    }

    public ArrayList<Integer> getDamage() {
        ArrayList<Integer> damageList = new ArrayList<>();
        if(damage == null){
            return null;
        }
        if(damage.endsWith("%")){
            String[] damages = damage.split("-");
            for(String dmg : damages){
                damageList.add(Integer.parseInt(dmg.replace("%","")));
            }
        }
        return damageList;
    }

    //public ArrayList<String> getEnchantments(){
//
    //}
}
