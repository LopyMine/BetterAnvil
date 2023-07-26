package net.lopymine.betteranvil.gui;

import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import net.lopymine.betteranvil.gui.descriptions.CITDescription;
import net.lopymine.betteranvil.gui.panels.WMyListPanel;
import net.lopymine.betteranvil.resourcepacks.cit.CITItem;
import net.lopymine.betteranvil.resourcepacks.cit.CITParser;
import net.lopymine.betteranvil.resourcepacks.cit.writers.CITFavoriteWriter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public abstract class AnvilGui extends CITDescription {

    protected abstract void renameMethod(String name);
    public AnvilGui(Screen parent, ItemStack anvilItem) {
        super(parent);

        configuratorF = (CITItem s, WMyListPanel destination) -> {

            destination.favoriteButton.setToggle(true);

            destination.wRenameButton.setToolTip(Text.of(s.getCustomName()));

            destination.wRenameButton.setResourcePackToolTip(Text.of("§9" + s.getResourcePack()));

            destination.wRenameButton.setHost(this);

            if (s.getLore() != null) {
                destination.wRenameButton.setLore(s.getLore());
            }

            destination.wRenameButton.setOnCtrlClick(() -> {
                mc.setScreen(parent);
                this.renameMethod(s.getCustomName());
            });

            ItemStack anvilItemNew = new ItemStack(anvilItem.getItem().asItem());
            anvilItemNew.setCustomName(Text.of(s.getCustomName()));
            destination.wRenameButton.setIcon(anvilItemNew);

            destination.wRenameButton.setOnCtrlPress(()->{
                droppedItem.setStack(anvilItemNew);
                mob.setArmor(anvilItemNew);
            });

            destination.wRenameButton.setOnClick(() -> {
                bigFieldName.setImage(bfnTextureFocus);
                itemName.setText(Text.of(cutString(s.getCustomName(), maxLengthBigLabel)));
                droppedItem.setStack(anvilItemNew);
                mob.setArmor(anvilItemNew);
                itemName.setHorizontalAlignment(HorizontalAlignment.CENTER);
                copyButton.setOnClick(() -> {
                    mc.setScreen(parent);
                    this.renameMethod(s.getCustomName());
                });

            });

            destination.wRenameButton.setText(Text.literal(cutString(s.getCustomName(), maxLength)));

            destination.favoriteButton.setOnToggle(on -> {
                if (!on) {
                    CITFavoriteWriter.removeItem(s);
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

        configuratorD = (CITItem s, WMyListPanel destination) -> {
            destination.wRenameButton.setText(Text.literal(cutString(s.getCustomName(), maxLength)));

            destination.wRenameButton.setToolTip(Text.of(s.getCustomName()));

            destination.wRenameButton.setResourcePackToolTip(Text.of("§9" + s.getResourcePack()));

            destination.wRenameButton.setHost(this);

            if (s.getLore() != null) {
                destination.wRenameButton.setLore(s.getLore());
            }

            destination.wRenameButton.setOnCtrlClick(() -> {
                mc.setScreen(parent);
                this.renameMethod(s.getCustomName());
            });

            ItemStack anvilItemNew = new ItemStack(anvilItem.getItem().asItem());
            anvilItemNew.setCustomName(Text.of(s.getCustomName()));
            destination.wRenameButton.setIcon(anvilItemNew);

            destination.wRenameButton.setOnCtrlPress(()->{
                droppedItem.setStack(anvilItemNew);
                mob.setArmor(anvilItemNew);
            });

            for (CITItem citItem : dataF) {
                if (citItem.equals(s)) {
                    destination.favoriteButton.setToggle(true);
                }
            }

            destination.wRenameButton.setOnClick(() -> {
                bigFieldName.setImage(bfnTextureFocus);
                itemName.setText(Text.of(cutString(s.getCustomName(), maxLengthBigLabel)));
                itemName.setHorizontalAlignment(HorizontalAlignment.CENTER);
                copyButton.setOnClick(() -> {
                    mc.setScreen(parent);
                    this.renameMethod(s.getCustomName());
                });
                droppedItem.setStack(anvilItemNew);
                mob.setArmor(anvilItemNew);
            });

            destination.favoriteButton.setOnToggle(on -> {
                if (on) {
                    CITFavoriteWriter.addItem(s);
                    dataF.add(s);
                    createFavoriteNameList(favorite, dataF);
                } else {
                    CITFavoriteWriter.removeItem(s);
                    dataF.remove(s);
                    createFavoriteNameList(favorite, dataF);
                }
            });
        };

        itemView.setIcon(new ItemIcon(anvilItem));

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


        dataD = CITParser.parseAllItems(anvilItem);

        dataFAll = CITFavoriteWriter.readConfig().getItems();

        dataF = CITFavoriteWriter.getWithItem(dataFAll, anvilItem);

        createLists();
    }
}
