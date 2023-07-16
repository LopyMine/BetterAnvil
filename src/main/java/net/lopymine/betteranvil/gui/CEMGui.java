package net.lopymine.betteranvil.gui;

import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import net.lopymine.betteranvil.fake.FakeWorld;
import net.lopymine.betteranvil.gui.descriptions.CEMDescription;
import net.lopymine.betteranvil.gui.panels.WMyListPanel;
import net.lopymine.betteranvil.resourcepacks.cem.CEMItem;
import net.lopymine.betteranvil.resourcepacks.cem.CEMParser;
import net.lopymine.betteranvil.resourcepacks.cem.writers.CEMFavoriteWriter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class CEMGui extends CEMDescription {

    protected abstract void renameMethod(CEMItem item);

    public CEMGui(Screen parent) {
        super(parent);

        dataD = CEMParser.parseCEMItems();

        dataF = CEMFavoriteWriter.getFavoriteItems();

        configuratorF = (CEMItem s, WMyListPanel destination) -> {

            destination.favoriteButton.setToggle(true);

            destination.wRenameButton.setToolTip(Text.of(s.getName()));

            destination.wRenameButton.setResourcePackToolTip(Text.of("§9" + s.getResourcePack()));

            destination.wRenameButton.setHost(this);

            destination.wRenameButton.setOnCtrlClick(() -> {
                mc.setScreen(parent);
                this.renameMethod(s);
            });

            Optional<EntityType<?>> type = EntityType.get(s.getMob());
            EntityType<?> finalType = EntityType.PIG;
            if (type.isPresent()) {
                finalType = type.get();
            }
            Entity entity = finalType.create(FakeWorld.getInstance());
            if (entity != null) entity.setCustomName(Text.of(s.getName()));
            SpawnEggItem entityEgg = SpawnEggItem.forEntity(finalType);
            if (entityEgg == null) {
                entityEgg = SpawnEggItem.forEntity(EntityType.PIG);
            }
            List<ItemStack> stacks = new ArrayList<>();
            if (entityEgg != null) {
                stacks.add(entityEgg.getDefaultStack());
                destination.wRenameButton.setStacks(stacks);
                destination.wRenameButton.setIcon(entityEgg.getDefaultStack());
            }

            destination.wRenameButton.setOnClick(() -> {
                bigFieldName.setImage(bfnTextureFocus);
                mob.setEntity(entity);
                itemName.setText(Text.of(cutString(s.getName(), maxLengthBigLabel)));
                itemName.setHorizontalAlignment(HorizontalAlignment.CENTER);

                copyButton.setOnClick(() -> {
                    mc.setScreen(parent);
                    this.renameMethod(s);
                });

            });

            destination.wRenameButton.setText(Text.literal(cutString(s.getName(), maxLength)));

            destination.favoriteButton.setOnToggle(on -> {
                if (!on) {
                    CEMFavoriteWriter.removeItem(s);
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

        configuratorD = (CEMItem s, WMyListPanel destination) -> {
            destination.wRenameButton.setText(Text.literal(cutString(s.getName(), maxLength)));

            destination.wRenameButton.setToolTip(Text.of(s.getName()));

            destination.wRenameButton.setResourcePackToolTip(Text.of("§9" + s.getResourcePack()));

            destination.wRenameButton.setHost(this);

            Optional<EntityType<?>> type = EntityType.get(s.getMob());
            EntityType<?> finalType = EntityType.PIG;
            if (type.isPresent()) {
                finalType = type.get();
            }
            Entity entity = finalType.create(FakeWorld.getInstance());
            if (entity != null) entity.setCustomName(Text.of(s.getName()));
            SpawnEggItem entityEgg = SpawnEggItem.forEntity(finalType);
            if (entityEgg == null) {
                entityEgg = SpawnEggItem.forEntity(EntityType.PIG);
            }
            List<ItemStack> stacks = new ArrayList<>();
            if (entityEgg != null) {
                stacks.add(entityEgg.getDefaultStack());
                destination.wRenameButton.setStacks(stacks);
                destination.wRenameButton.setIcon(entityEgg.getDefaultStack());
            }

            destination.wRenameButton.setOnCtrlClick(() -> {
                mc.setScreen(parent);
                this.renameMethod(s);
            });

            for (CEMItem citItem : dataF) {
                if (citItem.equals(s)) {
                    destination.favoriteButton.setToggle(true);
                }
            }

            destination.wRenameButton.setOnClick(() -> {
                bigFieldName.setImage(bfnTextureFocus);
                mob.setEntity(entity);
                itemName.setText(Text.of(cutString(s.getName(), maxLengthBigLabel)));
                itemName.setHorizontalAlignment(HorizontalAlignment.CENTER);

                copyButton.setOnClick(() -> {
                    mc.setScreen(parent);
                    this.renameMethod(s);
                });
            });

            destination.favoriteButton.setOnToggle(on -> {
                if (on) {
                    CEMFavoriteWriter.addItem(s);
                    dataF.add(s);
                    createFavoriteNameList(favorite, dataF);
                } else {
                    CEMFavoriteWriter.removeItem(s);
                    dataF.remove(s);
                    createFavoriteNameList(favorite, dataF);
                }
            });
        };

        labelD.setText(Text.translatable("better_anvil.cem_menu.title"));

        createLists();

    }
}
