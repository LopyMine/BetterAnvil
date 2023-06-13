package net.lopymine.betteranvil.resourcepacks.utils;

import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class ItemList {
    public static ArrayList<Item> getItems(){
        ArrayList<Item> items = new ArrayList<>();
        Class<Items> itemsClass = Items.class;
        Field[] fields = itemsClass.getFields();

        for (Field field : fields) {
            if (Item.class.isAssignableFrom(field.getType())) {
                try {
                    items.add((Item) field.get(null));
                } catch (IllegalAccessException e) {
                    // Handle exception
                }
            }
        }
        items.remove(0);
        return items;
    }

    public static Item getItemById(String s){
        for(Item item : getItems()){
            if(getItemId(item).equals(s)){
                return item;
            }
        }
        return Items.AIR;
    }

    public static String getItemId(Item i){
        return i.getTranslationKey().replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");
    }
}
