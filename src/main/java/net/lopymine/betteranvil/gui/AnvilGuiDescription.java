package net.lopymine.betteranvil.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LibGui;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.cit.CitItems;
import net.lopymine.betteranvil.cit.ConfigParser;
import net.lopymine.betteranvil.cit.writers.FavoriteWriter;
import net.lopymine.betteranvil.gui.my.widget.WFavoriteButton;
import net.lopymine.betteranvil.gui.my.widget.WMyTextField;
import net.lopymine.betteranvil.modmenu.BetterAnvilConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class AnvilGuiDescription extends LightweightGuiDescription {
    @Override
    public void addPainters() {

    }
    protected abstract void renameMethod(String name);
    public static final int maxLength = 18;
    public static final int maxLengthBigLabel = 20;
    private static WLabel emptyF;
    private static WListPanel<CitItems, MyWListPanel> wListPanelF;
    private static WLabel emptyD;
    private static WListPanel<CitItems, MyWListPanel> wListPanelD;
    private static boolean favoriteWindowOn;
    private final BiConsumer<CitItems, MyWListPanel> configuratorF;
    private final BiConsumer<CitItems, MyWListPanel> configuratorD;
    private final ArrayList<CitItems> dataF;
    private final Collection<CitItems> dataFAll;
    private final ArrayList<CitItems> dataD;
    public final Identifier bigFieldNameTextureFocusDark = new Identifier(BetterAnvil.MOD_ID, "gui/namefieldfocusdark.png");
    public final Identifier bigFieldNameTextureNotFocusDark = new Identifier(BetterAnvil.MOD_ID, "gui/namefielddark.png");
    public final Identifier bigFieldNameTextureFocus = new Identifier(BetterAnvil.MOD_ID, "gui/namefieldfocus.png");
    public final Identifier bigFieldNameTextureNotFocus = new Identifier(BetterAnvil.MOD_ID, "gui/namefield.png");
    public final Identifier bfnTexture = getDarkOrWhiteTexture(bigFieldNameTextureNotFocusDark, bigFieldNameTextureNotFocus);
    public final Identifier bfnTextureFocus = getDarkOrWhiteTexture(bigFieldNameTextureFocusDark, bigFieldNameTextureFocus);
    private final WPlainPanel panel;
    private final int height;
    private final int width;
    private final int panelDHeight;
    private final int panelDWidth = 203;
    private final int ots = 8;
    private final int copyButtonPos;
    private final int favoriteButtonPos = 9;
    private final int buttonHeight = BetterAnvilConfigManager.read().BUTTON_HEIGHT;

    public AnvilGuiDescription(Screen parent, ItemStack anvilItem) {
        WPlainPanel root = new WPlainPanel();
        MinecraftClient mc = MinecraftClient.getInstance();
        root.setInsets(Insets.ROOT_PANEL);
        height = mc.currentScreen.height;
        width = mc.currentScreen.width;
        root.setSize(width, height);

        panelDHeight = height;
        copyButtonPos = panelDHeight - 70;
        ArrayList<WButton> buttons = new ArrayList<>();

        WSprite bigFieldName = new WSprite(bfnTexture);
        WLabel itemName = new WLabel(Text.of(" "));
        WItemSlot anvilSlot = new WItemSlot(new PlayerInventory(mc.player), 1, 1, 1, true);
        anvilSlot.setIcon(new ItemIcon(anvilItem));

        WPlainPanel favorite = new WPlainPanel();
        favorite.setSize(203, 230);
        favorite.setHost(this);
        favorite.add(new WLabel(Text.translatable("gui.betteranvil.favorite.title")).setHorizontalAlignment(HorizontalAlignment.CENTER), 85, favoriteButtonPos);
        panel = new WPlainPanel();
        panel.setBackgroundPainter(BackgroundPainter.VANILLA);
        dataD = new ArrayList<>(ConfigParser.parseAllItemNames(anvilItem));

        dataFAll = new ArrayList<>(FavoriteWriter.readConfig().getCitItemsCollection());
        dataF = new ArrayList<>(FavoriteWriter.getWithItem(dataFAll, anvilItem));
        configuratorF = (CitItems s, MyWListPanel destination) -> {

            destination.favoriteButton.setToggle(true);

            destination.wItemButton.setItemNameToolTip(Text.of(s.getCustomName()));

            destination.wItemButton.setResourcePackToolTip(Text.of("§9" + s.getResourcePack()));

            destination.wItemButton.setHost(this);

            if(s.getLore() != null){
                destination.wItemButton.setLore(s.getLore());
            }

            destination.wItemButton.setOnCtrlClick(()->{
                mc.setScreen(parent);
                this.renameMethod(s.getCustomName());
            });

            ItemStack anvilItemNew = new ItemStack(anvilItem.getItem().asItem());

            //NbtCompound compound = new NbtCompound();
            //compound.putInt("CustomModelData", 1234567);
            //anvilItemNew.setNbt(compound);

            anvilItemNew.setCustomName(Text.of(s.getCustomName()));
            List<ItemStack> itemStackList = new ArrayList<>();
            itemStackList.add(anvilItemNew);
            destination.wItemButton.setItemIcon(new WItem(itemStackList));

            destination.wItemButton.setOnClick(() -> {
                bigFieldName.setImage(bfnTextureFocus);
                itemName.setText(Text.of(cutString(s.getCustomName(), maxLengthBigLabel)));
                anvilSlot.setIcon(new ItemIcon(anvilItemNew));
                itemName.setHorizontalAlignment(HorizontalAlignment.CENTER);
                for (WButton button : buttons) {
                    button.setOnClick(() -> {
                        mc.setScreen(parent);
                        this.renameMethod(s.getCustomName());
                    });
                }
            });

            destination.wItemButton.setText(Text.literal(cutString(s.getCustomName(), maxLength)));

            destination.favoriteButton.setOnToggle(on -> {
                if (!on) {
                    FavoriteWriter.removeItem(dataFAll, s);
                    dataF.remove(s);
                    createFavoriteNameList(favorite, dataF);
                    createAllNameList(panel, dataD);
                }
            });
        };

        WMyTextField textFieldF = new WMyTextField(ConfigParser.getResourcePacksWithCITFolder(), Text.translatable("gui.betteranvil.citmenu.list.search"));

        textFieldF.setHost(favorite.getHost());
        textFieldF.setChangedListener(s -> {
            if (textFieldF.getText().isEmpty()) {
                createFavoriteNameList(favorite, dataF);
                return;
            }
            createFavoriteNameList(favorite, getSearchData(textFieldF, dataF));
        });

        favorite.add(textFieldF, 35, favoriteButtonPos + 24, 130, 10);

        createFavoriteNameList(favorite, dataF);

        WFavoriteButton wFavoriteButton = new WFavoriteButton();
        wFavoriteButton.setOnToggle(on -> {
            favoriteWindowOn = on;

            createFavoriteNameList(favorite, dataF);

            if (favoriteWindowOn) {
                root.add(favorite, (width / 2 - (panelDWidth / 2)) - 210, ots / 2);
                favorite.setBackgroundPainter(BackgroundPainter.VANILLA);
                //favorite.setBackgroundPainter(MyWListPanel.backgroundPainter);
                favorite.layout();
                favorite.setSize(203, 224);
            }
            if (!favoriteWindowOn) {
                root.remove(favorite);
            }

        });


        WLabel labelD = new WLabel(Text.translatable("gui.betteranvil.citmenu.title"));
        labelD.setSize(6, 5);
        labelD.setVerticalAlignment(VerticalAlignment.TOP).setHorizontalAlignment(HorizontalAlignment.CENTER);
        panel.add(labelD, 85, favoriteButtonPos);

        panel.add(wFavoriteButton, 10, favoriteButtonPos);

        WButton copyButton = new WButton(Text.translatable("gui.betteranvil.citmenu.button.copy"));
        buttons.add(copyButton);
        panel.add(copyButton, 14, copyButtonPos, 175, 50);
        WButton closeButtonD = new WButton(Text.literal("х"));
        closeButtonD.setOnClick(() -> {
            mc.setScreen(parent);
        });
        panel.add(closeButtonD, 172, favoriteButtonPos, 25, 5);
        panel.add(bigFieldName, 18, copyButtonPos - 26, 165, 24); // 26 because height 24 + 2

        panel.add(itemName, 93, copyButtonPos - 16);
        panel.add(anvilSlot, 90, copyButtonPos + 26);


        configuratorD = (CitItems s, MyWListPanel destination) -> {
            destination.wItemButton.setText(Text.literal(cutString(s.getCustomName(), maxLength)));

            destination.wItemButton.setItemNameToolTip(Text.of(s.getCustomName()));

            destination.wItemButton.setResourcePackToolTip(Text.of("§9" + s.getResourcePack()));

            destination.wItemButton.setHost(this);

            if(s.getLore() != null){
                destination.wItemButton.setLore(s.getLore());
            }

            destination.wItemButton.setOnCtrlClick(()->{
                mc.setScreen(parent);
                this.renameMethod(s.getCustomName());
            });


            ItemStack anvilItemNew = new ItemStack(anvilItem.getItem().asItem());
            anvilItemNew.setCustomName(Text.of(s.getCustomName()));
            List<ItemStack> itemStackList = new ArrayList<>();
            itemStackList.add(anvilItemNew);
            destination.wItemButton.setItemIcon(new WItem(itemStackList));

            for (CitItems citItem : dataF) {
                if (citItem.getCustomName().equals(s.getCustomName())) {
                    destination.favoriteButton.setToggle(true);
                }
            }

            destination.wItemButton.setOnClick(() -> {
                bigFieldName.setImage(bfnTextureFocus);

                itemName.setText(Text.of(cutString(s.getCustomName(), maxLengthBigLabel)));
                itemName.setHorizontalAlignment(HorizontalAlignment.CENTER);
                copyButton.setOnClick(() -> {
                    mc.setScreen(parent);
                    this.renameMethod(s.getCustomName());
                });
                anvilSlot.setIcon(new ItemIcon(anvilItemNew));
            });

            destination.favoriteButton.setOnToggle(on -> {
                if (on) {
                    FavoriteWriter.addItem(s);
                    dataF.add(s);
                    createFavoriteNameList(favorite, dataF);
                } else {
                    FavoriteWriter.removeItem(dataFAll, s);
                    dataF.remove(s);
                    createFavoriteNameList(favorite, dataF);
                }
            });
        };

        createAllNameList(panel, dataD);

        WMyTextField textFieldD = new WMyTextField(ConfigParser.getResourcePacksWithCITFolder(), Text.translatable("gui.betteranvil.citmenu.list.search"));
        textFieldD.setChangedListener(s -> {
            if (textFieldD.getText().isEmpty()) {
                createAllNameList(panel, dataD);
                return;
            }
            createAllNameList(panel, getSearchData(textFieldD, dataD));
        });
        panel.add(textFieldD, 35, favoriteButtonPos + 24, 130, 10);

        root.add(panel, (width / 2 - (panelDWidth / 2)) -5, ots / 2, panelDWidth, panelDHeight - (ots * 2));//(panel, 293, 53, 203, 405);
        System.out.println(panelDHeight);
        System.out.println(panelDHeight - (ots * 2));
        System.out.println(height);




        setRootPanel(root);
        root.validate(this);
    }

    public static String cutString(String text, int length) {
        if (text.length() <= length) {
            return text;
        } else {
            String cutText = text.substring(0, length);
            if (cutText.endsWith(" ")) {
                cutText = cutText.substring(0, cutText.length() - 1);
            }
            return cutText + "...";
        }
    }

    private ArrayList<CitItems> getSearchData(WMyTextField wTextField, ArrayList<CitItems> data) {
        ArrayList<CitItems> dataSearch = new ArrayList<>();
        for (CitItems dt : data) {
            if(wTextField.getText().startsWith("*")){
                if(dt.getResourcePack().toLowerCase().contains(wTextField.getText().toLowerCase().replace("*",""))){
                    dataSearch.add(dt);
                }
            } else {
                if (dt.getCustomName().toLowerCase().replace("ё", "е").contains(wTextField.getText().toLowerCase().replace("ё", "е"))) {
                    dataSearch.add(dt);
                }
            }
        }
        return dataSearch;
    }

    public void createFavoriteNameList(WPlainPanel root, ArrayList<CitItems> data) {
        root.remove(emptyF);
        root.remove(wListPanelF);

        if (data.isEmpty()) {
            emptyF = new WLabel(Text.translatable("gui.betteranvil.citmenu.list.search.empty"));
            root.add(emptyF, 90, 100);
            emptyF.setHorizontalAlignment(HorizontalAlignment.CENTER);
            emptyF.setVerticalAlignment(VerticalAlignment.CENTER);
        } else {
            wListPanelF = new WListPanel<>(data, MyWListPanel::new, configuratorF);
            wListPanelF.getScrollBar().setHost(this);
            wListPanelF.setListItemHeight(buttonHeight);
            wListPanelF.setBackgroundPainter(MyWListPanel.backgroundPainter);
            wListPanelF.layout();
            //180 200
            root.add(wListPanelF, 5, 64, 191, 150);
        }
    }

    public void createAllNameList(WPlainPanel root, ArrayList<CitItems> data) {
        root.remove(emptyD);
        root.remove(wListPanelD);
        if (data.isEmpty()) {
            emptyD = new WLabel(Text.translatable("gui.betteranvil.citmenu.list.search.empty"));
            root.add(emptyD, 90, panelDHeight / 2 - 20);
            emptyD.setHorizontalAlignment(HorizontalAlignment.CENTER);
            emptyD.setVerticalAlignment(VerticalAlignment.CENTER);
        } else {
            wListPanelD = new WListPanel<>(data, MyWListPanel::new, configuratorD);
            wListPanelD.getScrollBar().setHost(this);
            wListPanelD.setListItemHeight(buttonHeight);
            wListPanelD.setBackgroundPainter(MyWListPanel.backgroundPainter);
            wListPanelD.layout();
            //(200, 400);
            root.add(wListPanelD, 5, favoriteButtonPos + 50, 192, copyButtonPos - 88);
        }
    }

    private Identifier getDarkOrWhiteTexture(Identifier dark, Identifier white) {
        return LibGui.isDarkMode() ? dark : white;
    }



}