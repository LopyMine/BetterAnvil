package net.lopymine.betteranvil.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.*;
import net.minecraft.text.Text;
import net.minecraft.text.Text.Serializer;

import io.github.cottonmc.cotton.gui.widget.WItem;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;

import net.lopymine.betteranvil.config.resourcepacks.cit.*;
import net.lopymine.betteranvil.config.resourcepacks.cit.metadata.CountMetaDataParser.CountMetaData;
import net.lopymine.betteranvil.config.resourcepacks.cit.metadata.DamageMetaDataParser.DamageMetaData;
import net.lopymine.betteranvil.config.resourcepacks.cit.metadata.EnchantmentLevelsMetaDataParser.EnchantmentLevelsMetaData;
import net.lopymine.betteranvil.gui.description.CITGuiDescription;
import net.lopymine.betteranvil.gui.description.handler.CITGuiHandler;
import net.lopymine.betteranvil.gui.panels.WConfigPanel;
import net.lopymine.betteranvil.gui.widgets.buttons.WRenameButton;
import net.lopymine.betteranvil.gui.widgets.buttons.WRenameButton.Builder;
import net.lopymine.betteranvil.utils.ItemUtils;

import java.util.List;

public abstract class ItemRenamesGui extends CITGuiDescription {
    public ItemRenamesGui(Screen parent, ItemStack itemStack) {
        super(new CITGuiHandler(), parent);

        CITFavoriteConfigManager favoriteManager = CITFavoriteConfigManager.getInstance();

        favoriteConsumer = (CITItem item, WConfigPanel panel) -> {
            panel.starButton.setToggle(true);
            panel.starButton.setOnToggle(on -> {
                if (!on) {
                    favoriteManager.removeItem(item);
                    favoriteList.remove(item);
                    createFavoriteListPanel(favoritePanel, favoriteList, favoriteConsumer);
                    if (!hasFavoriteTab && favoriteTab == null) {
                        createMainListPanel(mainPanel, handler.getSearch(mainTextField.getText(), mainList.get(active_pack)), mainConsumer);
                    }
                }
            });

            List<String> lore = item.getLore();
            CountMetaData countMetaData = item.getCountMetaData();
            DamageMetaData damageMetaData = item.getDamageMetaData();
            List<String> enchantments = item.getEnchantments();
            EnchantmentLevelsMetaData enchantmentLevels = item.getEnchantmentLevels();

            ItemStack anvilItem = new ItemStack(itemStack.getItem().asItem());
            anvilItem.setCustomName(Text.of(item.getCustomName()));

            if (lore != null) {
                NbtList nbtLore = new NbtList();

                for (String line : lore) {
                    String l = line;

                    if (line.contains("|")) {
                        String[] split = line.split("\\|");
                        if (split.length > 0) {
                            l = split[0];
                        }
                    }

                    nbtLore.add(NbtString.of(Serializer.toJson(Text.of(l))));
                }

                NbtCompound compound = anvilItem.getOrCreateNbt();

                NbtCompound compoundLore = anvilItem.getOrCreateSubNbt(ItemStack.DISPLAY_KEY);
                compoundLore.put(ItemStack.LORE_KEY, nbtLore);
                compound.put(ItemStack.DISPLAY_KEY, compoundLore);

                anvilItem.setNbt(compound);
            }

            if (countMetaData != null) {
                anvilItem.setCount((countMetaData.maxCount() + countMetaData.minCount()) / 2);
            }
            if (damageMetaData != null) {
                anvilItem.setDamage((int) ((damageMetaData.maxDamage() + damageMetaData.minDamage()) / 2));
            }

            if (enchantments != null) {
                for (String e : enchantments) {
                    Enchantment enchantment = ItemUtils.getEnchantmentByName(e);
                    if (enchantment != null) {
                        int enchantmentLevel = (enchantmentLevels != null ? ((enchantmentLevels.maxLevel() + enchantmentLevels.minLevel()) / 2) : enchantment.getMaxLevel());
                        anvilItem.addEnchantment(enchantment, enchantmentLevel);
                    }
                }
            }

            Builder builder = WRenameButton.builder(item.getCustomName())
                    .setIcon(new WItem(anvilItem))
                    .setResourcePack(item.getResourcePack())
                    .setLore(lore)
                    .setDamageMetaData(damageMetaData)
                    .setCountMetaData(countMetaData)
                    .setEnchantments(enchantments)
                    .setEnchantmentLevels(enchantmentLevels)
                    .setItems(List.of(anvilItem.getItem().getDefaultStack()), false)
                    .setOnClick(() -> {
                        field.setText(item.getCustomName());
                        droppedItem.setItemStack(anvilItem);
                        mob.setArmor(anvilItem);
                        selectButton.setOnClick(() -> {
                            client.setScreen(parent);
                            renameItem(item.getCustomName());
                        });
                    })
                    .setOnCtrlClick(() -> {
                        client.setScreen(parent);
                        renameItem(item.getCustomName());
                    })
                    .setOnCtrlDown(() -> {
                        droppedItem.setItemStack(anvilItem);
                        mob.setArmor(anvilItem);
                    });

            WRenameButton renameButton = builder.build();
            renameButton.setHost(this);

            panel.addWRenameButton(renameButton);
        };

        mainConsumer = (CITItem item, WConfigPanel panel) -> {
            for (CITItem citItem : favoriteList) {
                if (citItem.equals(item)) {
                    panel.starButton.setToggle(true);
                }
            }

            panel.starButton.setOnToggle(on -> {
                if (on) {
                    favoriteManager.addItem(item);
                    favoriteList.add(item);
                } else {
                    favoriteManager.removeItem(item);
                    favoriteList.remove(item);
                }
                createFavoriteListPanel(favoritePanel, favoriteList, favoriteConsumer);
            });

            List<String> lore = item.getLore();
            CountMetaData countMetaData = item.getCountMetaData();
            DamageMetaData damageMetaData = item.getDamageMetaData();
            List<String> enchantments = item.getEnchantments();
            EnchantmentLevelsMetaData enchantmentLevels = item.getEnchantmentLevels();

            ItemStack anvilItem = new ItemStack(itemStack.getItem().asItem());
            anvilItem.setCustomName(Text.of(item.getCustomName()));

            if (lore != null) {
                NbtList nbtLore = new NbtList();

                for (String line : lore) {
                    String l = line;

                    if (line.contains("|")) {
                        String[] split = line.split("\\|");
                        if (split.length > 0) {
                            l = split[0];
                        }
                    }

                    nbtLore.add(NbtString.of(Serializer.toJson(Text.of(l))));
                }

                NbtCompound compound = anvilItem.getOrCreateNbt();

                NbtCompound compoundLore = anvilItem.getOrCreateSubNbt(ItemStack.DISPLAY_KEY);
                compoundLore.put(ItemStack.LORE_KEY, nbtLore);
                compound.put(ItemStack.DISPLAY_KEY, compoundLore);

                anvilItem.setNbt(compound);
            }

            if (countMetaData != null) {
                anvilItem.setCount((countMetaData.maxCount() + countMetaData.minCount()) / 2);
            }
            if (damageMetaData != null) {
                anvilItem.setDamage((int) ((damageMetaData.maxDamage() + damageMetaData.minDamage()) / 2));
            }

            if (enchantments != null) {
                for (String e : enchantments) {
                    Enchantment enchantment = ItemUtils.getEnchantmentByName(e);
                    if (enchantment != null) {
                        int enchantmentLevel = (enchantmentLevels != null ? ((enchantmentLevels.maxLevel() + enchantmentLevels.minLevel()) / 2) : enchantment.getMaxLevel());
                        anvilItem.addEnchantment(enchantment, enchantmentLevel);
                    }
                }
            }

            Builder builder = WRenameButton.builder(item.getCustomName())
                    .setIcon(new WItem(anvilItem))
                    .setResourcePack(item.getResourcePack())
                    .setLore(lore)
                    .setDamageMetaData(damageMetaData)
                    .setCountMetaData(countMetaData)
                    .setEnchantments(enchantments)
                    .setEnchantmentLevels(enchantmentLevels)
                    .setItems(List.of(anvilItem.getItem().getDefaultStack()), false)
                    .setOnClick(() -> {
                        field.setText(item.getCustomName());
                        droppedItem.setItemStack(anvilItem);
                        mob.setArmor(anvilItem);
                        selectButton.setOnClick(() -> {
                            client.setScreen(parent);
                            renameItem(item.getCustomName());
                        });
                    })
                    .setOnCtrlClick(() -> {
                        client.setScreen(parent);
                        renameItem(item.getCustomName());
                    })
                    .setOnCtrlDown(() -> {
                        droppedItem.setItemStack(anvilItem);
                        mob.setArmor(anvilItem);
                    });

            WRenameButton renameButton = builder.build();
            renameButton.setHost(this);

            panel.addWRenameButton(renameButton);
        };

        itemPreviewButton.setIcon(new ItemIcon(itemStack));
        itemPreviewButton.setOnClick(() -> {
            root.remove(mob);
            root.remove(droppedItem);
            root.add(droppedItem, droppedItemPosX, droppedItemPosY, 1, 1);
        });

        playerPreviewButton.setOnClick(() -> {
            root.remove(mob);
            root.remove(droppedItem);
            root.add(mob, droppedItemPosX, droppedItemPosY + (entitiesSize / 2) + 30, 1, 1);
        });

        mainList = CITConfigParser.getInstance().getResourcePacksItems(itemStack, null, getConfig());
        favoriteList = favoriteManager.getWithItem(itemStack);

        this.init();
    }

    protected abstract void renameItem(String name);
}
