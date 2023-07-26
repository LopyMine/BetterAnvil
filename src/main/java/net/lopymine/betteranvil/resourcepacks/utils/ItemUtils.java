package net.lopymine.betteranvil.resourcepacks.utils;

import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.LinkedHashSet;

public class ItemUtils {

    public static Item getItemById(String id) {
        return Registry.ITEM.get(new Identifier(id));
    }

    public static String getItemId(Item i){
        return i.getTranslationKey().replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");
    }

}
