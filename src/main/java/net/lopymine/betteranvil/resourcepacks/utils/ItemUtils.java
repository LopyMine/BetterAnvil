package net.lopymine.betteranvil.resourcepacks.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class ItemUtils {
    private static final HashMap<String,Item> items = new HashMap<>();
    public static LinkedHashSet<Item> getItems(){
        LinkedHashSet<Item> items = new LinkedHashSet<>();
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
        return items;
    }

    public static Item getItemById(String s){
        return items.get(s);
    }

    public static String getItemId(Item i){
        return i.getTranslationKey().replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");
    }

    static {
        getItems().forEach(item -> items.put(getItemId(item),item));
    }

}
