package net.lopymine.betteranvil.utils;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

import org.jetbrains.annotations.Nullable;

public class ItemUtils {
    @Nullable
    public static Enchantment getEnchantmentByName(String name) {
        if (!name.contains(":")) {
            return getEnchantmentByName("minecraft", name);
        }

        String mod = name.substring(0, name.indexOf(':'));
        String path = name.substring(name.lastIndexOf(':') + 1);

        return getEnchantmentByName(mod, path);
    }

    @Nullable
    public static Enchantment getEnchantmentByName(String mod, String path) {
        try {
            return Registries.ENCHANTMENT.get(new Identifier(mod, path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Item getItemByName(String name) {
        if (!name.contains(":")) {
            return getItemByName("minecraft", name);
        }

        String mod = name.substring(0, name.indexOf(':'));
        String path = name.substring(name.lastIndexOf(':') + 1);

        return getItemByName(mod, path);
    }

    public static Item getItemByName(String mod, String path) {
        try {
            return Registries.ITEM.get(new Identifier(mod, path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Items.AIR;
    }

    public static String getID(Item item) {
        String s = Registries.ITEM.getId(item).toString();
        if (s.contains(":")) s = s.substring(s.lastIndexOf(':') + 1);
        return s;
    }
}
