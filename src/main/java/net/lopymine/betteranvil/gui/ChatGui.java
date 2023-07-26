package net.lopymine.betteranvil.gui;

import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.lopymine.betteranvil.gui.descriptions.CMDescription;
import net.lopymine.betteranvil.gui.panels.WMyListPanel;
import net.lopymine.betteranvil.resourcepacks.cmd.CMDItem;
import net.lopymine.betteranvil.resourcepacks.cmd.CMDParser;
import net.lopymine.betteranvil.resourcepacks.cmd.writers.CMDFavoriteWriter;
import net.lopymine.betteranvil.resourcepacks.utils.ItemUtils;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;

public abstract class ChatGui extends CMDescription {

    protected abstract void giveMethod(CMDItem item);
    public ChatGui(Screen parent) {
        super(parent);

        configuratorF = (CMDItem s, WMyListPanel destination) -> {

            destination.favoriteButton.setToggle(true);

            Text text = getTextByID(s.getId());

            destination.wRenameButton.setToolTip(text);

            destination.wRenameButton.setText(text);

            destination.wRenameButton.setResourcePackToolTip(Text.of("§9" + s.getResourcePack()));

            destination.wRenameButton.setHost(this);

            destination.wRenameButton.setOnCtrlClick(() -> {
                mc.setScreen(parent);
                giveMethod(s);
            });
            Item item = ItemUtils.getItemById(clearId(s.getItem()));
            ItemStack anvilItemNew = new ItemStack(item);
            NbtCompound compound = new NbtCompound();
            compound.putInt("CustomModelData", s.getId());
            anvilItemNew.setNbt(compound);

            destination.wRenameButton.setOnCtrlPress(()->{
                droppedItem.setStack(anvilItemNew);
                mob.setArmor(anvilItemNew);
            });

            destination.wRenameButton.addStack(item.getDefaultStack());
            destination.wRenameButton.setIcon(anvilItemNew);

            destination.wRenameButton.setOnClick(() -> {
                droppedItem.setStack(anvilItemNew);
                mob.setArmor(anvilItemNew);
                bigFieldName.setImage(bfnTextureFocus);
                itemName.setText(text);
                itemName.setHorizontalAlignment(HorizontalAlignment.CENTER);
                copyButton.setOnClick(() -> {
                    mc.setScreen(parent);
                    giveMethod(s);
                });

            });

            destination.favoriteButton.setOnToggle(on -> {
                if (!on) {
                    CMDFavoriteWriter.removeItem(s);
                    dataF.remove(s);
                    createFavoriteNameList(favorite, dataF);
                    if (active_pack.equals("all")) {
                        createAllNameList(panel, getSearchData(textFieldD, dataD));
                    } else {
                        createAllNameList(panel, getSearchData(textFieldD, packs.get(active_pack)));
                    }
                }
            });
        };

        configuratorD = (CMDItem s, WMyListPanel destination) -> {

            Text text = getTextByID(s.getId());

            destination.wRenameButton.setToolTip(text);

            destination.wRenameButton.setText(text);

            destination.wRenameButton.setResourcePackToolTip(Text.of("§9" + s.getResourcePack()));

            destination.wRenameButton.setHost(this);

            destination.wRenameButton.setOnCtrlClick(() -> {
                mc.setScreen(parent);
                giveMethod(s);
            });
            Item item = ItemUtils.getItemById(clearId(s.getItem()));
            ItemStack anvilItemNew = new ItemStack(item);
            NbtCompound compound = new NbtCompound();
            compound.putInt("CustomModelData", s.getId());
            anvilItemNew.setNbt(compound);

            destination.wRenameButton.setOnCtrlPress(()->{
                droppedItem.setStack(anvilItemNew);
                mob.setArmor(anvilItemNew);
            });

            destination.wRenameButton.addStack(item.getDefaultStack());
            destination.wRenameButton.setIcon(anvilItemNew);

            for (CMDItem citItem : dataF) {
                if (citItem.equals(s)) {
                    destination.favoriteButton.setToggle(true);
                }
            }

            destination.wRenameButton.setOnClick(() -> {
                droppedItem.setStack(anvilItemNew);
                mob.setArmor(anvilItemNew);
                bigFieldName.setImage(bfnTextureFocus);
                itemName.setText(text);
                itemName.setHorizontalAlignment(HorizontalAlignment.CENTER);
                copyButton.setOnClick(() -> {
                    mc.setScreen(parent);
                    giveMethod(s);
                });
            });

            destination.favoriteButton.setOnToggle(on -> {
                if (on) {
                    CMDFavoriteWriter.addItem(s);
                    dataF.add(s);
                    createFavoriteNameList(favorite, dataF);
                } else {
                    CMDFavoriteWriter.removeItem(s);
                    dataF.remove(s);
                    createFavoriteNameList(favorite, dataF);
                }
            });
        };

        itemView.setOnClick(()->{
            root.remove(mob);
            root.remove(droppedItem);
            root.add(droppedItem, iw, ih, 1, 1);
        });

        playerView.setOnClick(()->{
            root.remove(mob);
            root.remove(droppedItem);
            root.add(mob, iw, ih+(sizeSdks/2)+30, 1, 1);
        });

        dataD = CMDParser.parseCMDItems();
        dataF = CMDFavoriteWriter.getFavoriteItems();

        createLists();

    }
}
