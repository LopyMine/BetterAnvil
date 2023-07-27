package net.lopymine.betteranvil.gui;

import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import net.lopymine.betteranvil.gui.descriptions.CITDescription;
import net.lopymine.betteranvil.gui.panels.WMyListPanel;
import net.lopymine.betteranvil.gui.widgets.buttons.WTabButton;
import net.lopymine.betteranvil.resourcepacks.ConfigManager;
import net.lopymine.betteranvil.resourcepacks.PackManager;
import net.lopymine.betteranvil.resourcepacks.cit.CITItem;
import net.lopymine.betteranvil.resourcepacks.cit.CITParser;
import net.lopymine.betteranvil.resourcepacks.cit.writers.CITFavoriteWriter;
import net.lopymine.betteranvil.resourcepacks.cit.writers.CITWriter;
import net.lopymine.betteranvil.resourcepacks.utils.ItemUtils;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;

public class PacksGui extends CITDescription {
    private final boolean lookMode;
    public PacksGui(Screen parent, LinkedHashSet<ResourcePackProfile> profiles, boolean lookMode) {
        super(parent);

        this.profiles = new LinkedHashSet<>(profiles);
        this.lookMode = lookMode;
        Keyboard keyboard = MinecraftClient.getInstance().keyboard;

        configuratorF = (CITItem s, WMyListPanel destination) -> {

            destination.favoriteButton.setToggle(true);

            destination.wRenameButton.setToolTip(Text.of(s.getCustomName()));

            destination.wRenameButton.setResourcePackToolTip(Text.of("§9" + s.getResourcePack()));

            destination.wRenameButton.setHost(this);

            if (s.getLore() != null) {
                destination.wRenameButton.setLore(s.getLore());
            }

            destination.wRenameButton.setOnCtrlClick(() -> {
                keyboard.setClipboard(s.getCustomName());
            });

            ItemStack anvilItemNew = new ItemStack(ItemUtils.getItemById(new ArrayList<>(s.getItems()).get(0)));
            anvilItemNew.setCustomName(Text.of(s.getCustomName()));

            destination.wRenameButton.setOnCtrlPress(()->{
                droppedItem.setStack(anvilItemNew);
                mob.setArmor(anvilItemNew);
            });

            destination.wRenameButton.setStacks(getStacksByStrings(s.getItems()));
            destination.wRenameButton.setIcon(anvilItemNew);

            destination.wRenameButton.setOnClick(() -> {
                droppedItem.setStack(anvilItemNew);
                mob.setArmor(anvilItemNew);
                bigFieldName.setImage(bfnTextureFocus);
                itemName.setText(Text.of(cutString(s.getCustomName(), maxLengthBigLabel)));

                itemName.setHorizontalAlignment(HorizontalAlignment.CENTER);
                copyButton.setOnClick(() -> {
                    keyboard.setClipboard(s.getCustomName());
                    copyButton.setLabel(Text.translatable("better_anvil.button.copy.done"));
                });
                copyButton.setLabel(Text.translatable("better_anvil.button.copy"));
            });

            destination.wRenameButton.setText(Text.literal(cutString(s.getCustomName(), maxLength)));

            destination.favoriteButton.setOnToggle(on -> {
                if (!on) {
                    CITFavoriteWriter.removeItem(s);
                    dataF.remove(s);
                    createFavoriteNameList(favorite, dataF);
                    createAllNameList(panel, packs.get(active_pack));
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
                keyboard.setClipboard(s.getCustomName());
            });

            ItemStack anvilItemNew = new ItemStack(ItemUtils.getItemById(s.getItems().stream().toList().get(0)));
            anvilItemNew.setCustomName(Text.of(s.getCustomName()));

            destination.wRenameButton.setOnCtrlPress(()->{
                droppedItem.setStack(anvilItemNew);
                mob.setArmor(anvilItemNew);
            });

            destination.wRenameButton.setStacks(getStacksByStrings(s.getItems()));
            destination.wRenameButton.setIcon(anvilItemNew);

            for (CITItem citItem : dataF) {
                if (citItem.equals(s)) {
                    destination.favoriteButton.setToggle(true);
                }
            }

            destination.wRenameButton.setOnClick(() -> {
                droppedItem.setStack(anvilItemNew);
                mob.setArmor(anvilItemNew);
                bigFieldName.setImage(bfnTextureFocus);
                itemName.setText(Text.of(cutString(s.getCustomName(), maxLengthBigLabel)));
                itemName.setHorizontalAlignment(HorizontalAlignment.CENTER);
                copyButton.setOnClick(() -> {
                    keyboard.setClipboard(s.getCustomName());
                    copyButton.setLabel(Text.translatable("better_anvil.button.copy.done"));
                });
                copyButton.setLabel(Text.translatable("better_anvil.button.copy"));
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

        copyButton = new WButton(Text.translatable("better_anvil.button.copy"));

        itemView.setIcon(new ItemIcon(Items.NAME_TAG));

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

        dataF = getFItems(profiles);

        createLists();

        labelD = buttons.size() > 1 ? labelD.setText(Text.translatable("better_anvil.packs_menu.title")) : labelD.setText(Text.translatable("better_anvil.pack_menu.title"));
        labelD.setHorizontalAlignment(HorizontalAlignment.CENTER);
    }
    private LinkedHashSet<CITItem> getFItems(LinkedHashSet<ResourcePackProfile> profiles) {
        LinkedHashSet<String> packs = new LinkedHashSet<>();
        for (ResourcePackProfile profile : profiles) {
            packs.add(getPack(profile));
        }
        return CITFavoriteWriter.getPackItems(CITFavoriteWriter.readConfig().getItems(),packs);
    }

    @Override
    public void setButtons() {

        if (profiles.isEmpty()) {
            createAllNameList(panel, new LinkedHashSet<>());
            return;
        }

        for (ResourcePackProfile profile : profiles) {
            String pack = getPack(profile);
            LinkedHashSet<CITItem> citItems;

            if (lookMode) {
                boolean d = pack.equals("server");
                String path = (d ? ConfigManager.pathToCITServerConfigFolder : ConfigManager.pathToCITConfigFolder);

                if(PackManager.getServerResourcePack().get() != null && d){
                    citItems = CITParser.parseItemsFromConfig(PackManager.getServerResourcePack().get(), path);
                } else if (ConfigManager.hasConfig(path + pack + ConfigManager.jsonFormat)) {
                    citItems = CITParser.parseItemsFromConfig(pack, path);
                } else {
                    citItems = CITParser.transformCitItems(CITParser.setCitItemsRP(CITWriter.getCITItems(new File(ConfigManager.pathToResourcePacks + getPackWithZip(profile)), getPackWithZip(profile).endsWith(".zip"), false).getItems(), pack));
                }
            } else {
                dataD = CITParser.parseAllItems();
                if(pack.equals("server")){
                    citItems = getServerPackItems();
                } else {
                    citItems = getPackItems(pack);
                }
            }

            if (!citItems.isEmpty()) {
                WTabButton button = new WTabButton();

                button.setResourcePack(pack);
                button.setIcon(PackManager.getPackIcon(profile));

                packs.put(pack, citItems);

                button.setOnToggle((on) -> {
                    active_pack = pack;

                    for (WTabButton tabButton : buttons) {
                        if (!tabButton.equals(button)) {
                            tabButton.setToggle(false);
                        }
                    }

                    createAllNameList(panel, packs.get(pack));

                });

                buttons.add(button);
            } else {
                MYLOGGER.warn("CIT Items is empty for pack " + pack + "!");
            }
        }

        int d = buttons.size();
        if (d > 7) {
            d = 7;
        }
        for (int i = 0; i < d; i++) {
            buttonsSub.add(buttons.stream().toList().get(i));
        }
        createButtons(buttonsSub);
    }

    private List<ItemStack> getStacksByStrings(List<String> ids) {
        List<ItemStack> stacks = new ArrayList<>();

        for(String item : ids){
            stacks.add(ItemUtils.getItemById(item).getDefaultStack());
        }
        return stacks;
    }

    @Override
    public void createButtons(LinkedHashSet<WTabButton> buttonsSub) {
        int d = 0;

        int f = (int) Math.ceil((double) buttons.size() / 7);

        root.remove(switcherLeft);
        root.remove(switcherRight);

        root.add(switcherRight, w + panelWidth + 2, h - 26);
        root.add(switcherLeft, w - 15, h - 26);

        if (s <= 0) {
            root.remove(switcherLeft);
        }

        if(s >= f || s == f - 1){
            root.remove(switcherRight);
        }

        if (buttonsSub.isEmpty()) {
            MYLOGGER.warn("Buttons sub is empty");
            WTabButton wTabButton = new WTabButton();
            wTabButton.setItem(new ItemStack(Items.NAME_TAG));
            wTabButton.setOnToggle((on) -> {
                active_pack = "all";

                for (WTabButton tabButton : buttons) {
                    if (!tabButton.equals(wTabButton)) {
                        tabButton.setToggle(false);
                    }
                }

                createAllNameList(panel, new LinkedHashSet<>());

            });

            buttonsSub.add(wTabButton);
            buttons.add(wTabButton);
        }

        root.remove(panel);

        for (WTabButton button : buttonsSub) {
            root.add(button, w + d, h - 29);
            d = d + 30;
        }

        root.add(panel, w, h, panelWidth, panelDHeight);

        WTabButton button = buttonsSub.stream().toList().get(0);
        button.setToggle(true);

        if (button.getOnToggle() != null) {
            button.getOnToggle().accept(true);
        } else {
            MYLOGGER.warn("W-wait... WTabButton consumer on toggle is null?");
        }

    }
}
