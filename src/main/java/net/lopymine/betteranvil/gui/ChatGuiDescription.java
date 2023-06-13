package net.lopymine.betteranvil.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LibGui;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.gui.panels.WMyListPanel;
import net.lopymine.betteranvil.gui.widgets.WDroppedItem;
import net.lopymine.betteranvil.gui.widgets.WMyTextField;
import net.lopymine.betteranvil.gui.widgets.WSwitcher;
import net.lopymine.betteranvil.gui.widgets.buttons.WFavoriteButton;
import net.lopymine.betteranvil.gui.widgets.buttons.WTabButton;
import net.lopymine.betteranvil.gui.widgets.enums.Switcher;
import net.lopymine.betteranvil.modmenu.BetterAnvilConfigManager;
import net.lopymine.betteranvil.resourcepacks.PackManager;
import net.lopymine.betteranvil.resourcepacks.cmd.CMDItem;
import net.lopymine.betteranvil.resourcepacks.cmd.CMDParser;
import net.lopymine.betteranvil.resourcepacks.cmd.writers.CMDFavoriteWriter;
import net.lopymine.betteranvil.resourcepacks.utils.ItemList;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;

public abstract class ChatGuiDescription extends LightweightGuiDescription {
    @Override
    public void addPainters() {

    }

    protected abstract void method(CMDItem item);

    private final ArrayList<WTabButton> buttons = new ArrayList<>();
    private int s = 0;
    private final ArrayList<WTabButton> buttonsSub = new ArrayList<>();
    private static WLabel emptyF;
    private static WListPanel<CMDItem, WMyListPanel> wListPanelF;
    private static WLabel emptyD;
    private static WListPanel<CMDItem, WMyListPanel> wListPanelD;
    private static boolean favoriteWindowOn;
    private final BiConsumer<CMDItem, WMyListPanel> configuratorF;
    private final BiConsumer<CMDItem, WMyListPanel> configuratorD;
    private final ArrayList<CMDItem> dataF;
    private final ArrayList<CMDItem> dataD;
    public final Identifier bigFieldNameTextureFocusDark = new Identifier(BetterAnvil.MOD_ID, "gui/namefieldfocusdark.png");
    public final Identifier bigFieldNameTextureNotFocusDark = new Identifier(BetterAnvil.MOD_ID, "gui/namefielddark.png");
    public final Identifier bigFieldNameTextureFocus = new Identifier(BetterAnvil.MOD_ID, "gui/namefieldfocus.png");
    public final Identifier bigFieldNameTextureNotFocus = new Identifier(BetterAnvil.MOD_ID, "gui/namefield.png");
    public final Identifier bfnTexture = getDarkOrWhiteTexture(bigFieldNameTextureNotFocusDark, bigFieldNameTextureNotFocus);
    public final Identifier bfnTextureFocus = getDarkOrWhiteTexture(bigFieldNameTextureFocusDark, bigFieldNameTextureFocus);
    private final WPlainPanel panel = new WPlainPanel();
    private final int screenHeight;
    private final int screenWidth;
    private final int panelDHeight;
    private int panelFHeight = 244;
    private final int panelWidth = 208;
    private final int ind = 5;
    private final int copyButtonPos;
    private final int favoriteButtonPos = 9;
    private final int buttonHeight = BetterAnvilConfigManager.read().BUTTON_HEIGHT;
    private final WPlainPanel root = new WPlainPanel();
    private final ArrayList<ResourcePackProfile> profiles = PackManager.getPacksProfiles();
    private final WSwitcher switcherLeft;
    private final WSwitcher switcherRight;
    private final int w;
    private final int h;
    private final WPlainPanel favorite = new WPlainPanel();
    private final Map<String, ArrayList<CMDItem>> packs = new HashMap<>();
    private String active_pack = "all";

    public ChatGuiDescription(Screen parent) {

        // Main

        MinecraftClient mc = MinecraftClient.getInstance();
        root.setInsets(Insets.ROOT_PANEL);

        screenWidth = mc.currentScreen.width;
        screenHeight = mc.currentScreen.height;

        root.setSize(screenWidth, screenHeight);

        int panel_height = screenHeight - (ind * 2);

        int tab_height = 32;

        panelDHeight = panel_height - tab_height;

        w = (screenWidth / 2 - (panelWidth / 2)) - 5;
        h = ((ind + tab_height) - 7);

        copyButtonPos = panelDHeight - 30;

        if (panelDHeight <= 240) {
            panelFHeight = 180;
        }

        favorite.setSize(panelWidth, panelFHeight);
        favorite.setHost(this);

        dataD = new ArrayList<>(CMDParser.parseCMDItems());

        dataF = new ArrayList<>(CMDFavoriteWriter.getFavoriteItems());

        // Widgets

        WSprite bigFieldName = new WSprite(bfnTexture);

        WLabel labelF = new WLabel(Text.translatable("gui.betteranvil.favorite.title"));
        labelF.setHorizontalAlignment(HorizontalAlignment.CENTER);

        WLabel labelD = new WLabel(Text.translatable("gui.betteranvil.citmenu.title"));
        labelD.setHorizontalAlignment(HorizontalAlignment.CENTER);

        WLabel itemName = new WLabel(Text.of(" "));
        itemName.setHorizontalAlignment(HorizontalAlignment.CENTER);
        itemName.setVerticalAlignment(VerticalAlignment.CENTER);

        WMyTextField textFieldF = new WMyTextField(PackManager.getPackNamesWithCMDConfig(), Text.translatable("gui.betteranvil.citmenu.list.search"));
        textFieldF.setHost(favorite.getHost());
        textFieldF.setChangedListener(s -> {
            if (textFieldF.getText().isEmpty()) {
                createFavoriteNameList(favorite, dataF);
                return;
            }

            if(textFieldF.getText().startsWith("*")){
                createFavoriteNameList(favorite, getSearchPackData(textFieldF, dataF));
                return;
            }

            createFavoriteNameList(favorite, getSearchData(textFieldF, dataF));
        });

        WTextField textFieldD = new WTextField(Text.translatable("gui.betteranvil.citmenu.list.search"));
        textFieldD.setChangedListener(s -> {
            if (textFieldD.getText().isEmpty()) {
                if (active_pack.equals("all")) {
                    createAllNameList(panel, dataD);
                } else {
                    createAllNameList(panel, packs.get(active_pack));
                }
                return;
            }
            if (active_pack.equals("all")) {
                createAllNameList(panel, getSearchData(textFieldD,dataD));
            } else {
                createAllNameList(panel, getSearchData(textFieldD, packs.get(active_pack)));
            }
        });

        WButton closeButtonD = new WButton(Text.literal("х"));
        closeButtonD.setOnClick(() -> {
            mc.setScreen(parent);
        });

        WFavoriteButton wFavoriteButton = new WFavoriteButton();
        wFavoriteButton.setOnToggle(on -> {
            favoriteWindowOn = on;

            createFavoriteNameList(favorite, dataF);

            if (favoriteWindowOn) {
                root.add(favorite, (screenWidth / 2 - (panelWidth / 2)) - (panelWidth + 7), h);
                favorite.setBackgroundPainter(BackgroundPainter.VANILLA);
                favorite.layout();
                favorite.setSize(panelWidth, panelFHeight);
            }
            if (!favoriteWindowOn) {
                root.remove(favorite);
            }

        });

        switcherLeft = new WSwitcher(Switcher.LEFT);
        switcherLeft.setOnClick(this::backButtons);

        switcherRight = new WSwitcher(Switcher.RIGHT);
        switcherRight.setOnClick(this::nextButtons);

        WButton copyButton = new WButton(Text.translatable("gui.betteranvil.citmenu.button.select"));

        WDroppedItem droppedItem = new WDroppedItem(Items.AIR.getDefaultStack());

        int itemPanelWidth = screenWidth - (w + panelWidth);

        int sdks = itemPanelWidth / 40;
        droppedItem.setItemSize(10 * sdks + 20);

        int iw = screenWidth - (itemPanelWidth / 2);
        int ih = screenHeight / 2;

        root.add(droppedItem, iw, ih, 1,1);

        //
        //
        //

        int tff = 130;
        favorite.add(textFieldF, getWPos(tff), favoriteButtonPos + 24, tff, 10);
        favorite.add(labelF, 0, favoriteButtonPos, panelWidth, 10);

        configuratorF = (CMDItem s, WMyListPanel destination) -> {

            destination.favoriteButton.setToggle(true);

            Text text = getTextByID(s.getId());

            destination.wItemButton.setToolTip(text);

            destination.wItemButton.setText(text);

            destination.wItemButton.setResourcePackToolTip(Text.of("§9" + s.getResourcePack()));

            destination.wItemButton.setHost(this);

            destination.wItemButton.setOnCtrlClick(() -> {
                mc.setScreen(parent);
                method(s);
            });

            ItemStack anvilItemNew = new ItemStack(getItemByString(s.getItem()));

            destination.wItemButton.setStack(anvilItemNew);

            NbtCompound compound = new NbtCompound();
            compound.putInt("CustomModelData", s.getId());
            anvilItemNew.setNbt(compound);

            destination.wItemButton.setItemIcon(new WItem(anvilItemNew));

            destination.wItemButton.setOnClick(() -> {
                droppedItem.setStack(anvilItemNew);
                bigFieldName.setImage(bfnTextureFocus);
                itemName.setText(text);
                itemName.setHorizontalAlignment(HorizontalAlignment.CENTER);
                copyButton.setOnClick(() -> {
                    mc.setScreen(parent);
                    method(s);
                });

            });

            destination.favoriteButton.setOnToggle(on -> {
                if (!on) {
                    CMDFavoriteWriter.removeItem(dataF, s);
                    dataF.remove(s);
                    createFavoriteNameList(favorite, dataF);
                    createAllNameList(panel, packs.get(active_pack));
                }
            });
        };


        configuratorD = (CMDItem s, WMyListPanel destination) -> {

            Text text = getTextByID(s.getId());

            destination.wItemButton.setToolTip(text);

            destination.wItemButton.setText(text);

            destination.wItemButton.setResourcePackToolTip(Text.of("§9" + s.getResourcePack()));

            destination.wItemButton.setHost(this);

            destination.wItemButton.setOnCtrlClick(() -> {
                mc.setScreen(parent);
                method(s);
            });

            ItemStack anvilItemNew = new ItemStack(getItemByString(s.getItem()));

            destination.wItemButton.setStack(anvilItemNew);

            NbtCompound compound = new NbtCompound();
            compound.putInt("CustomModelData", s.getId());
            anvilItemNew.setNbt(compound);

            destination.wItemButton.setItemIcon(new WItem(anvilItemNew));

            for (CMDItem citItem : dataF) {
                if (citItem.equals(s)) {
                    destination.favoriteButton.setToggle(true);
                }
            }

            destination.wItemButton.setOnClick(() -> {
                droppedItem.setStack(anvilItemNew);
                bigFieldName.setImage(bfnTextureFocus);
                itemName.setText(text);
                itemName.setHorizontalAlignment(HorizontalAlignment.CENTER);
                copyButton.setOnClick(() -> {
                    mc.setScreen(parent);
                    method(s);
                });
            });

            destination.favoriteButton.setOnToggle(on -> {
                if (on) {
                    CMDFavoriteWriter.addItem(s);
                    dataF.add(s);
                    createFavoriteNameList(favorite, dataF);
                } else {
                    CMDFavoriteWriter.removeItem(dataF, s);
                    dataF.remove(s);
                    createFavoriteNameList(favorite, dataF);
                }
            });
        };

        panel.setBackgroundPainter(BackgroundPainter.VANILLA);

        panel.add(labelD, 0, favoriteButtonPos, panelWidth, 10);
        panel.add(wFavoriteButton, 10, favoriteButtonPos);
        panel.add(copyButton, getWPos(174), copyButtonPos, 174, 50);
        panel.add(closeButtonD, panelWidth - 30, favoriteButtonPos, 25, 5);

        int bfn = 164;
        panel.add(bigFieldName, getWPos(bfn), copyButtonPos - 26, bfn, 24);
        panel.add(itemName, getWPos(bfn), copyButtonPos - 26, bfn, 24);

        int tfd = 130;
        panel.add(textFieldD, getWPos(tfd), favoriteButtonPos + 24, tfd, 10);
        //

        createFavoriteNameList(favorite, dataF);

        setButtons();

        //
        if (buttons.size() > 7) {
            root.add(switcherRight, w + panelWidth + 2, h - 26);
        }

        root.add(panel, w, h, panelWidth, panelDHeight);

        setRootPanel(root);
        root.validate(this);
    }

    private ArrayList<CMDItem> getSearchData(WTextField wTextField, ArrayList<CMDItem> data) {
        ArrayList<CMDItem> dataSearch = new ArrayList<>();

        for (CMDItem dt : data) {
            if(String.valueOf(dt.getId()).contains(wTextField.getText())){
                dataSearch.add(dt);
            }
        }

        return dataSearch;
    }

    private ArrayList<CMDItem> getSearchPackData(WTextField wTextField, ArrayList<CMDItem> data) {
        ArrayList<CMDItem> dataSearch = new ArrayList<>();

        for (CMDItem dt : data) {
            if (dt.getResourcePack().toLowerCase().replace("ё", "е").contains(wTextField.getText().toLowerCase().replace("*","").replace("ё", "е"))) {
                dataSearch.add(dt);
            }
        }

        return dataSearch;
    }

    public void createFavoriteNameList(WPlainPanel root, ArrayList<CMDItem> data) {
        root.remove(emptyF);
        root.remove(wListPanelF);

        wListPanelF = new WListPanel<>(data, WMyListPanel::new, configuratorF);
        wListPanelF.getScrollBar().setHost(this);
        wListPanelF.setListItemHeight(buttonHeight);
        wListPanelF.setBackgroundPainter(WMyListPanel.backgroundPainter);
        wListPanelF.layout();

        int d = 8;

        root.add(wListPanelF, d, favoriteButtonPos + 50, panelWidth - (d * 2), (panelFHeight - (favoriteButtonPos + 50)) - d);

        if (data.isEmpty()) {
            emptyF = new WLabel(Text.translatable("gui.betteranvil.citmenu.list.search.empty"));
            emptyF.setHorizontalAlignment(HorizontalAlignment.CENTER);
            emptyF.setVerticalAlignment(VerticalAlignment.CENTER);

            emptyF.setColor(0xFFFFFFFF);
            emptyF.setDarkmodeColor(0xbcbcbc);

            root.add(emptyF, d, favoriteButtonPos + 50, panelWidth - (d * 2), (panelFHeight - (favoriteButtonPos + 50)) - d);
        }
    }

    public void createAllNameList(WPlainPanel root, ArrayList<CMDItem> data) {
        root.remove(emptyD);
        root.remove(wListPanelD);

        wListPanelD = new WListPanel<>(data, WMyListPanel::new, configuratorD);
        wListPanelD.getScrollBar().setHost(this);
        wListPanelD.setListItemHeight(buttonHeight);
        wListPanelD.setBackgroundPainter(WMyListPanel.backgroundPainter);
        wListPanelD.layout();

        int d = 8;
        root.add(wListPanelD, d, favoriteButtonPos + 50, panelWidth - (d * 2), copyButtonPos - 88);

        if (data.isEmpty()) {
            emptyD = new WLabel(Text.translatable("gui.betteranvil.citmenu.list.search.empty"));
            emptyD.setHorizontalAlignment(HorizontalAlignment.CENTER);
            emptyD.setVerticalAlignment(VerticalAlignment.CENTER);

            emptyD.setColor(0xFFFFFFFF);
            emptyD.setDarkmodeColor(0xbcbcbc);

            root.add(emptyD, d, favoriteButtonPos + 50, panelWidth - (d * 2), copyButtonPos - 88);
        }
    }

    private void setButtons() {

        WTabButton wTabButton = new WTabButton();
        wTabButton.setItem(new ItemStack(Items.NAME_TAG));
        wTabButton.setOnToggle((on) -> {
            active_pack = "all";

            for (WTabButton tabButton : buttons) {
                if (!tabButton.equals(wTabButton)) {
                    tabButton.setToggle(false);
                }
            }

            createAllNameList(panel, dataD);

        });

        buttons.add(wTabButton);

        if (profiles.isEmpty()) {
            createAllNameList(panel, new ArrayList<>());
            return;
        }

        for (ResourcePackProfile profile : profiles) {
            String pack = getPack(profile);

            ArrayList<CMDItem> citItems;

            if(pack.equals("server")){
                citItems = getServerPackItems();
            } else {
                citItems = getPackItems(pack);
            }

            if(!citItems.isEmpty()){
                WTabButton button = new WTabButton();

                button.setResourcePack(pack);
                button.setIcon(PackManager.getPackIcon(profile));

                packs.put("pack-" + pack, citItems);

                button.setOnToggle((on) -> {
                    active_pack = "pack-" + pack;

                    for (WTabButton tabButton : buttons) {
                        if (!tabButton.equals(button)) {
                            tabButton.setToggle(false);
                        }
                    }

                    createAllNameList(panel, packs.get("pack-" + pack));

                });

                buttons.add(button);
            }
        }

        int d = buttons.size();
        if (d > 7) {
            d = 7;
        }
        for (int i = 0; i < d; i++) {
            buttonsSub.add(buttons.get(i));
        }
        createButtons(buttonsSub);
    }

    private void nextButtons() {
        s++;

        int startIndex = s * 7;
        int endIndex = startIndex + 7;
        if (endIndex > buttons.size()) {
            endIndex = buttons.size();
        }

        buttonsSub.clear();
        buttonsSub.addAll(buttons.subList(startIndex, endIndex));

        clearButtons();
        createButtons(buttonsSub);

    }

    private void backButtons() {
        s--;

        int startIndex = s * 7;
        int endIndex = startIndex + 7;
        if (endIndex > buttons.size()) {
            endIndex = buttons.size();
        }

        buttonsSub.clear();
        buttonsSub.addAll(buttons.subList(startIndex, endIndex));

        clearButtons();
        createButtons(buttonsSub);
    }

    private void createButtons(ArrayList<WTabButton> buttonsSub) {
        int d = 0;

        int f = buttons.size() / 7;

        root.remove(switcherLeft);
        root.remove(switcherRight);

        root.add(switcherRight, w + panelWidth + 2, h - 26);
        root.add(switcherLeft, w - 15, h - 26);

        if (s == 0) {
            root.remove(switcherLeft);
        }

        if (s == f) {
            root.remove(switcherRight);
        }

        if (buttonsSub.isEmpty()) {
            return;
        }

        root.remove(panel);

        for (WTabButton button : buttonsSub) {
            root.add(button, w + d, h - 29);
            d = d + 30;
        }

        root.add(panel, w, h, panelWidth, panelDHeight);

        WTabButton button = buttonsSub.get(0);
        button.setToggle(true);

        if(button.getOnToggle() != null){
            button.getOnToggle().accept(true);
        } else {
            MYLOGGER.warn("W-wait... WTabButton consumer on toggle is null?");
        }

    }

    private ArrayList<CMDItem> getPackItems(String pack) {
        ArrayList<CMDItem> citItems = new ArrayList<>();

        for (CMDItem item : dataD) {
            if (item.getResourcePack().equals(pack)) {
                citItems.add(item);
            }
        }

        return citItems;
    }

    private ArrayList<CMDItem> getServerPackItems() {
        ArrayList<CMDItem> citItems = new ArrayList<>();

        for (CMDItem item : dataD) {
            if (item.getResourcePack().equals("Server")) {
                citItems.add(item);
            }
        }

        return citItems;
    }

    private Item getItemByString(String item) {
        for(Item i : ItemList.getItems()){
            if(getItemName(i.getTranslationKey()).equals(item)){
                return i;
            }
        }
        return Items.AIR;
    }

    private String getItemName(String translationKey) {
        return translationKey.replaceAll("item.minecraft.", "").replaceAll("block.minecraft.", "");
    }

    private Text getTextByID(int id) {
        return Text.of("ID: " + id);
    }

    private void clearButtons() {
        for (WTabButton button : buttons) {
            root.remove(button);
        }
    }

    private Identifier getDarkOrWhiteTexture(Identifier dark, Identifier white) {
        return LibGui.isDarkMode() ? dark : white;
    }

    private int getWPos(int width) {
        return (panelWidth - width) / 2;
    }

    private String getPack(ResourcePackProfile profile) {
        return profile.getName().replaceAll(".zip", "").replaceAll("file/", "");
    }

}
