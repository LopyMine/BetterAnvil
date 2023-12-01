package net.lopymine.betteranvil.gui;

import net.minecraft.client.*;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.*;
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
import net.lopymine.betteranvil.gui.description.handler.ResourcePackRenamesGuiHandler;
import net.lopymine.betteranvil.gui.panels.WConfigPanel;
import net.lopymine.betteranvil.gui.widgets.buttons.WRenameButton;
import net.lopymine.betteranvil.gui.widgets.buttons.WRenameButton.Builder;
import net.lopymine.betteranvil.utils.*;

import java.util.*;
import java.util.stream.Stream;
import org.jetbrains.annotations.Nullable;

public class ResourcePackRenamesGui extends CITGuiDescription {
    private final Keyboard keyboard;

    public ResourcePackRenamesGui(Screen parent, @Nullable List<String> resourcePacks) {
        super(new ResourcePackRenamesGuiHandler(), parent);
        this.keyboard = MinecraftClient.getInstance().keyboard;

        CITFavoriteConfigManager favoriteManager = CITFavoriteConfigManager.getInstance();

        mainList = CITConfigParser.getInstance().getResourcePacksItems(resourcePacks, getConfig());

        LinkedHashSet<CITItem> items = favoriteManager.getItems();
        LinkedHashSet<String> packs = new LinkedHashSet<>(mainList.keySet().stream().flatMap(key -> Stream.of(ResourcePackUtils.getResourcePackNameWithZip(key))).toList());
        favoriteList = favoriteManager.getResourcePacksItems(items, packs);

        mainTitle = tabButtons.size() > 1
                ?
                mainTitle.setText(Text.translatable("better_anvil.packs_menu.title"))
                :
                mainTitle.setText(Text.translatable("better_anvil.pack_menu.title"));

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

            ItemStack anvilItem = new ItemStack(ItemUtils.getItemByName(item.getItems().get(0)));
            anvilItem.setCustomName(Text.of(item.getCustomName()));

            if (lore != null) {
                NbtList nbtLore = new NbtList();

                for (String line : lore) {
                    nbtLore.add(NbtString.of(Serializer.toJson(Text.of(line))));
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
                    .setItems(item.getItems().stream().flatMap(s -> {
                        return Stream.of(ItemUtils.getItemByName(s).getDefaultStack());
                    }).toList(), true)
                    .setOnClick(() -> {
                        droppedItem.setItemStack(anvilItem);
                        mob.setArmor(anvilItem);
                        field.setText(item.getCustomName());

                        selectButton.setOnClick(() -> {
                            keyboard.setClipboard(item.getCustomName());
                            selectButton.setLabel(Text.translatable("better_anvil.button.copy.done"));
                        }).setLabel(Text.translatable("better_anvil.button.copy"));
                    })
                    .setOnCtrlClick(() -> {
                        keyboard.setClipboard(item.getCustomName());
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

            ItemStack anvilItem = new ItemStack(ItemUtils.getItemByName(item.getItems().get(0)));
            anvilItem.setCustomName(Text.of(item.getCustomName()));

            if (lore != null) {
                NbtList nbtLore = new NbtList();

                for (String line : lore) {
                    nbtLore.add(NbtString.of(Serializer.toJson(Text.of(line))));
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
                    .setItems(item.getItems().stream().flatMap(s -> {
                        return Stream.of(ItemUtils.getItemByName(s).getDefaultStack());
                    }).toList(), true)
                    .setOnClick(() -> {
                        droppedItem.setItemStack(anvilItem);
                        mob.setArmor(anvilItem);
                        field.setText(item.getCustomName());

                        selectButton.setOnClick(() -> {
                            keyboard.setClipboard(item.getCustomName());
                            selectButton.setLabel(Text.translatable("better_anvil.button.copy.done"));
                        }).setLabel(Text.translatable("better_anvil.button.copy"));
                    })
                    .setOnCtrlClick(() -> {
                        keyboard.setClipboard(item.getCustomName());
                    })
                    .setOnCtrlDown(() -> {
                        droppedItem.setItemStack(anvilItem);
                        mob.setArmor(anvilItem);
                    });

            WRenameButton renameButton = builder.build();
            renameButton.setHost(this);

            panel.addWRenameButton(renameButton);
        };

        selectButton.setLabel(Text.translatable("better_anvil.button.copy"));

        itemPreviewButton.setIcon(new ItemIcon(Items.NAME_TAG));
        itemPreviewButton.setOnClick(() -> {
            root.remove(droppedItem);
            root.remove(mob);
            root.add(droppedItem, droppedItemPosX, droppedItemPosY, 1, 1);
        });

        playerPreviewButton.setOnClick(() -> {
            root.remove(droppedItem);
            root.remove(mob);
            root.add(mob, droppedItemPosX, droppedItemPosY + (entitiesSize / 2) + 30, 1, 1);
        });

        this.init();
    }
}
