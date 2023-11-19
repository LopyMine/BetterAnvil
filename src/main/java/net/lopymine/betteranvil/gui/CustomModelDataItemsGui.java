package net.lopymine.betteranvil.gui;

import net.minecraft.client.Keyboard;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

import io.github.cottonmc.cotton.gui.widget.WItem;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;

import net.lopymine.betteranvil.config.resourcepacks.cmd.*;
import net.lopymine.betteranvil.gui.description.CMDGuiDescription;
import net.lopymine.betteranvil.gui.description.handler.CMDGuiHandler;
import net.lopymine.betteranvil.gui.panels.WConfigPanel;
import net.lopymine.betteranvil.gui.widgets.buttons.WRenameButton;
import net.lopymine.betteranvil.gui.widgets.buttons.WRenameButton.Builder;
import net.lopymine.betteranvil.utils.ItemUtils;

import java.util.List;

public abstract class CustomModelDataItemsGui extends CMDGuiDescription {
    protected CustomModelDataItemsGui(Screen parent, boolean shouldCopyCommand) {
        super(new CMDGuiHandler(), parent);

        CMDFavoriteConfigManager favoriteManager = CMDFavoriteConfigManager.getInstance();

        favoriteConsumer = (CMDItem cmdItem, WConfigPanel panel) -> {
            panel.starButton.setToggle(true);
            panel.starButton.setOnToggle(on -> {
                if (!on) {
                    favoriteManager.removeItem(cmdItem);
                    favoriteList.remove(cmdItem);
                    createFavoriteListPanel(favoritePanel, favoriteList, favoriteConsumer);
                    if (!hasFavoriteTab && favoriteTab == null) {
                        createMainListPanel(mainPanel, handler.getSearch(mainTextField.getText(), mainList.get(active_pack)), mainConsumer);
                    }
                }
            });

            Item item = ItemUtils.getItemByName(cmdItem.getItem());
            ItemStack itemStack = new ItemStack(item);
            NbtCompound compound = new NbtCompound();
            compound.putInt("CustomModelData", cmdItem.getId());
            itemStack.setNbt(compound);

            Runnable onClick;
            if (shouldCopyCommand) {
                onClick = () -> {
                    Keyboard keyboard = client.keyboard;
                    keyboard.setClipboard("/give @s minecraft:" + cmdItem.getItem() + "{CustomModelData:" + cmdItem.getId() + "}");
                    selectButton.setLabel(Text.translatable("better_anvil.button.copy.done"));
                };
            } else {
                onClick = () -> {
                    client.setScreen(parent);
                    giveItem(cmdItem);
                };
            }

            Builder builder = WRenameButton.builder("ID: " + cmdItem.getId())
                    .setIcon(new WItem(itemStack))
                    .setResourcePack(cmdItem.getResourcePack())
                    .setItems(List.of(item.getDefaultStack()), true)
                    .setOnClick(() -> {
                        droppedItem.setStack(itemStack);
                        mob.setArmor(itemStack);
                        field.setText("ID: " + cmdItem.getId());
                        selectButton.setOnClick(onClick);
                    })
                    .setOnCtrlClick(onClick)
                    .setOnCtrlDown(() -> {
                        droppedItem.setStack(itemStack);
                        mob.setArmor(itemStack);
                    });

            WRenameButton renameButton = builder.build();
            renameButton.setHost(this);

            panel.addWRenameButton(renameButton);
        };


        mainConsumer = (CMDItem cmdItem, WConfigPanel panel) -> {
            for (CMDItem item : favoriteList) {
                if (item.equals(cmdItem)) {
                    panel.starButton.setToggle(true);
                }
            }

            panel.starButton.setOnToggle(on -> {
                if (on) {
                    favoriteManager.addItem(cmdItem);
                    favoriteList.add(cmdItem);
                } else {
                    favoriteManager.removeItem(cmdItem);
                    favoriteList.remove(cmdItem);
                }
                createFavoriteListPanel(favoritePanel, favoriteList, favoriteConsumer);
            });

            Item item = ItemUtils.getItemByName(cmdItem.getItem());
            ItemStack itemStack = new ItemStack(item);
            NbtCompound compound = new NbtCompound();
            compound.putInt("CustomModelData", cmdItem.getId());
            itemStack.setNbt(compound);

            Runnable onClick;
            if (shouldCopyCommand) {
                onClick = () -> {
                    Keyboard keyboard = client.keyboard;
                    keyboard.setClipboard("/give @s minecraft:" + cmdItem.getItem() + "{CustomModelData:" + cmdItem.getId() + "}");
                    selectButton.setLabel(Text.translatable("better_anvil.button.copy.done"));
                };
            } else {
                onClick = () -> {
                    client.setScreen(parent);
                    giveItem(cmdItem);
                };
            }

            Builder builder = WRenameButton.builder("ID: " + cmdItem.getId())
                    .setIcon(new WItem(itemStack))
                    .setResourcePack(cmdItem.getResourcePack())
                    .setItems(List.of(item.getDefaultStack()), true)
                    .setOnClick(() -> {
                        droppedItem.setStack(itemStack);
                        mob.setArmor(itemStack);
                        field.setText("ID: " + cmdItem.getId());
                        selectButton.setOnClick(onClick);
                    })
                    .setOnCtrlClick(onClick)
                    .setOnCtrlDown(() -> {
                        droppedItem.setStack(itemStack);
                        mob.setArmor(itemStack);
                    });

            WRenameButton renameButton = builder.build();
            renameButton.setHost(this);

            panel.addWRenameButton(renameButton);
        };

        selectButton.setLabel(Text.translatable("better_anvil.button.copy"));

        itemPreviewButton.setIcon(new ItemIcon(Items.COMMAND_BLOCK));
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

        mainList = CMDParser.getAllItems();
        favoriteList = favoriteManager.getItems();

        this.init();
    }

    protected abstract void giveItem(CMDItem item);
}
