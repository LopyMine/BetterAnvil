package net.lopymine.betteranvil.gui.description.handler;

import net.minecraft.enchantment.Enchantment;

import net.lopymine.betteranvil.config.resourcepacks.cit.CITItem;
import net.lopymine.betteranvil.config.resourcepacks.cit.metadata.CountMetaDataParser.CountMetaData;
import net.lopymine.betteranvil.config.resourcepacks.cit.metadata.DamageMetaDataParser.DamageMetaData;
import net.lopymine.betteranvil.config.resourcepacks.cit.metadata.EnchantmentLevelsMetaDataParser.EnchantmentLevelsMetaData;
import net.lopymine.betteranvil.gui.description.interfaces.GuiHandler;
import net.lopymine.betteranvil.resourcepacks.ResourcePackType;
import net.lopymine.betteranvil.utils.*;

import java.util.*;

public class CITGuiHandler implements GuiHandler<CITItem> {
    @Override
    public LinkedHashSet<CITItem> getSearch(String search, LinkedHashSet<CITItem> list) {
        return new LinkedHashSet<>(list.stream().filter(item -> {
            return StringUtils.c(item.getCustomName(), search);
        }).toList());
    }

    @Override
    public LinkedHashSet<CITItem> getSearchByItem(String search, LinkedHashSet<CITItem> list) {
        if (search.isEmpty()) {
            return list;
        }
        return new LinkedHashSet<>(list.stream().filter(item -> {
            String string = ItemUtils.getItemByName(item.getItem()).asItem().getName().getString();
            return StringUtils.c(string, search);
        }).toList());
    }

    @Override
    public LinkedHashSet<CITItem> getSearchByPack(String search, LinkedHashSet<CITItem> list) {
        if (search.isEmpty()) {
            return new LinkedHashSet<>(list.stream().filter(item -> item.getResourcePack() != null).toList());
        }
        return new LinkedHashSet<>(list.stream().filter(item -> {
            String resourcePack = item.getResourcePack();
            if (resourcePack == null) {
                return false;
            }

            return StringUtils.c(resourcePack, search);
        }).toList());
    }

    @Override
    public LinkedHashSet<CITItem> getSearchByEnchantments(String search, LinkedHashSet<CITItem> list) {
        if (search.isEmpty()) {
            return new LinkedHashSet<>(list.stream().filter(item -> item.getEnchantments() != null).toList());
        }
        return new LinkedHashSet<>(list.stream().filter(item -> {
            List<String> enchantments = item.getEnchantments();
            if (enchantments == null) {
                return false;
            }
            EnchantmentLevelsMetaData levels = item.getEnchantmentLevels();
            for (String e : enchantments) {
                Enchantment enchantment = ItemUtils.getEnchantmentByName(e);
                if (enchantment == null) {
                    continue;
                }
                int level = (levels == null ? enchantment.getMaxLevel() : (levels.maxLevel() + levels.minLevel()) / 2);

                String string = enchantment.getName(level).getString();
                if (StringUtils.c(string, search)) {
                    return true;
                }
            }

            return false;
        }).toList());
    }

    @Override
    public LinkedHashSet<CITItem> getSearchByLore(String search, LinkedHashSet<CITItem> list) {
        if (search.isEmpty()) {
            return new LinkedHashSet<>(list.stream().filter(item -> item.getLore() != null).toList());
        }
        return new LinkedHashSet<>(list.stream().filter(item -> {
            List<String> lore = item.getLore();
            if (lore == null) {
                return false;
            }

            for (String line : lore) {
                if (StringUtils.c(line, search)) {
                    return true;
                }
            }

            return false;
        }).toList());
    }

    @Override
    public LinkedHashSet<CITItem> getSearchByCount(String search, LinkedHashSet<CITItem> list) {
        if (search.isEmpty()) {
            return new LinkedHashSet<>(list.stream().filter(item -> item.getCountMetaData() != null).toList());
        }
        return new LinkedHashSet<>(list.stream().filter(item -> {
            CountMetaData countMetaData = item.getCountMetaData();
            if (countMetaData == null) {
                return false;
            }

            return StringUtils.c(countMetaData.line(), search);
        }).toList());
    }

    @Override
    public LinkedHashSet<CITItem> getSearchByDamage(String search, LinkedHashSet<CITItem> list) {
        if (search.isEmpty()) {
            return new LinkedHashSet<>(list.stream().filter(item -> item.getDamageMetaData() != null).toList());
        }
        return new LinkedHashSet<>(list.stream().filter(item -> {
            DamageMetaData damageMetaData = item.getDamageMetaData();
            if (damageMetaData == null) {
                return false;
            }

            return StringUtils.c(damageMetaData.line(), search);
        }).toList());
    }


    @Override
    public ResourcePackType getType() {
        return ResourcePackType.CIT;
    }
}
