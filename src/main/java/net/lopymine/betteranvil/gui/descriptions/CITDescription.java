package net.lopymine.betteranvil.gui.descriptions;

import io.github.cottonmc.cotton.gui.client.LibGui;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.fake.FakeClientPlayerEntity;
import net.lopymine.betteranvil.gui.panels.Painters;
import net.lopymine.betteranvil.gui.panels.WMyListPanel;
import net.lopymine.betteranvil.gui.widgets.WDroppedItem;
import net.lopymine.betteranvil.gui.widgets.WMob;
import net.lopymine.betteranvil.gui.widgets.WMyTextField;
import net.lopymine.betteranvil.gui.widgets.WSwitcher;
import net.lopymine.betteranvil.gui.widgets.buttons.WFavoriteButton;
import net.lopymine.betteranvil.gui.widgets.buttons.WTabButton;
import net.lopymine.betteranvil.gui.widgets.custom_list.WListPanelExt;
import net.lopymine.betteranvil.gui.widgets.enums.Switcher;
import net.lopymine.betteranvil.modmenu.BetterAnvilConfigManager;
import net.lopymine.betteranvil.resourcepacks.PackManager;
import net.lopymine.betteranvil.resourcepacks.cit.CITItem;
import net.lopymine.betteranvil.resourcepacks.cmd.CMDItem;
import net.lopymine.betteranvil.resourcepacks.utils.ItemUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.*;
import java.util.function.BiConsumer;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;

public class CITDescription extends LightweightGuiDescription implements net.lopymine.betteranvil.gui.descriptions.interfaces.CITDescription {

    public final LinkedHashSet<WTabButton> buttons = new LinkedHashSet<>();
    public int s = 0;
    public final LinkedHashSet<WTabButton> buttonsSub = new LinkedHashSet<>();
    public static final int maxLength = 18;
    public static final int maxLengthBigLabel = 20;
    public static WLabel emptyF;
    public static WListPanelExt<CITItem, WMyListPanel> wListPanelF;
    public static WLabel emptyD;
    public static WListPanelExt<CITItem, WMyListPanel> wListPanelD;
    public static boolean favoriteWindowOn;
    public BiConsumer<CITItem, WMyListPanel> configuratorF;
    public BiConsumer<CITItem, WMyListPanel> configuratorD;
    public LinkedHashSet<CITItem> dataF = new LinkedHashSet<>();
    public LinkedHashSet<CITItem> dataFAll = new LinkedHashSet<>();
    public LinkedHashSet<CITItem> dataD = new LinkedHashSet<>();
    public final Identifier bigFieldNameTextureFocusDark = new Identifier(BetterAnvil.ID, "gui/namefield/namefield_focus_dark.png");
    public final Identifier bigFieldNameTextureNotFocusDark = new Identifier(BetterAnvil.ID, "gui/namefield/namefield_dark.png");
    public final Identifier bigFieldNameTextureFocus = new Identifier(BetterAnvil.ID, "gui/namefield/namefield_focus.png");
    public final Identifier bigFieldNameTextureNotFocus = new Identifier(BetterAnvil.ID, "gui/namefield/namefield.png");
    public final Identifier bfnTexture = getDarkOrWhiteTexture(bigFieldNameTextureNotFocusDark, bigFieldNameTextureNotFocus);
    public final Identifier bfnTextureFocus = getDarkOrWhiteTexture(bigFieldNameTextureFocusDark, bigFieldNameTextureFocus);
    public final WPlainPanel panel = new WPlainPanel();
    public final int screenHeight;
    public final int screenWidth;
    public final int panelDHeight;
    public int panelFHeight = 244;
    public final int panelWidth = 208;
    public final int ind = 5;
    public final int copyButtonPos;
    public final int favoriteButtonPos = 9;
    public final int buttonHeight = BetterAnvilConfigManager.INSTANCE.SPACING;
    public final WPlainPanel root = new WPlainPanel();
    public LinkedHashSet<ResourcePackProfile> profiles = new LinkedHashSet<>(PackManager.getPacksProfiles());
    public final WSwitcher switcherLeft;
    public final WSwitcher switcherRight;
    public final int w;
    public final int h;
    public final WPlainPanel favorite = new WPlainPanel();
    public final Map<String, LinkedHashSet<CITItem>> packs = new HashMap<>();
    public String active_pack = "all";
    public MinecraftClient mc = MinecraftClient.getInstance();
    public WSprite bigFieldName;
    public WLabel labelF;
    public WLabel labelD;
    public WLabel itemName;
    public WMyTextField textFieldF;
    public WTextField textFieldD;
    public WButton closeButtonD;
    public WFavoriteButton wFavoriteButton;
    public WButton copyButton;
    public WDroppedItem droppedItem;
    public WMob mob;
    public WButton itemView;
    public WButton playerView;
    public final int iw;
    public final int ih;
    public final int sizeSdks;
    public CITDescription(Screen parent) {
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

        configuratorF = (CITItem s, WMyListPanel destination) -> {

        };

        configuratorD = (CITItem s, WMyListPanel destination) -> {

        };

        // Widgets

        bigFieldName = new WSprite(bfnTexture);

        labelF = new WLabel(Text.translatable(  "better_anvil.favorite.title"));
        labelF.setHorizontalAlignment(HorizontalAlignment.CENTER);

        labelD = new WLabel(Text.translatable("better_anvil.cit_menu.title"));
        labelD.setHorizontalAlignment(HorizontalAlignment.CENTER);

        itemName = new WLabel(Text.of(" "));
        itemName.setHorizontalAlignment(HorizontalAlignment.CENTER);
        itemName.setVerticalAlignment(VerticalAlignment.CENTER);

        textFieldF = new WMyTextField(PackManager.getPackNamesWithCITConfig(), Text.translatable("better_anvil.search"));
        textFieldF.setMaxLength(60);
        textFieldF.setHost(favorite.getHost());
        textFieldF.setChangedListener(s -> {
            if (textFieldF.getText().isEmpty()) {
                createFavoriteNameList(favorite, dataF);
                return;
            }

            if(textFieldF.getText().startsWith("-")){
                createFavoriteNameList(favorite, getSearchItemData(textFieldF, dataF));
                return;
            }

            if (textFieldF.getText().startsWith("*")) {
                createFavoriteNameList(favorite, getSearchPackData(textFieldF, dataF));
                return;
            }

            createFavoriteNameList(favorite, getSearchData(textFieldF, dataF));
        });

        textFieldD = new WTextField(Text.translatable("better_anvil.search"));
        textFieldD.setMaxLength(60);
        textFieldD.setChangedListener(s -> {
            if (textFieldD.getText().isEmpty()) {
                if (active_pack.equals("all")) {
                    createAllNameList(panel, dataD);
                } else {
                    createAllNameList(panel, packs.get(active_pack));
                }
                return;
            }

            if(textFieldD.getText().startsWith("-")){
                if (active_pack.equals("all")) {
                    createAllNameList(panel, getSearchItemData(textFieldD, dataD));
                } else {
                    createAllNameList(panel, getSearchItemData(textFieldD, packs.get(active_pack)));
                }
                return;
            }

            if (active_pack.equals("all")) {
                createAllNameList(panel, getSearchData(textFieldD, dataD));
            } else {
                createAllNameList(panel, getSearchData(textFieldD, packs.get(active_pack)));
            }
        });

        closeButtonD = new WButton(Text.literal("х"));
        closeButtonD.setOnClick(() -> mc.setScreen(parent));

        wFavoriteButton = new WFavoriteButton();
        wFavoriteButton.setOnToggle(on -> {
            favoriteWindowOn = on;

            createFavoriteNameList(favorite, dataF);

            if (favoriteWindowOn) {
                root.add(favorite, (screenWidth / 2 - (panelWidth / 2)) - (panelWidth + 7), h);
                favorite.setBackgroundPainter(Painters.themePainter);
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

        copyButton = new WButton(Text.translatable("better_anvil.button.select"));

        droppedItem = new WDroppedItem(Items.AIR.getDefaultStack());

        int itemPanelWidth = screenWidth - (w + panelWidth);

        int sdks = itemPanelWidth / 40;
        sizeSdks = 10 * sdks + 20;
        droppedItem.setItemSize(sizeSdks);

        iw = screenWidth - (itemPanelWidth / 2);
        ih = screenHeight / 2;

        root.add(droppedItem, iw, ih, 1, 1);


        mob = new WMob(FakeClientPlayerEntity.getInstance());
        mob.setEntitySize(sizeSdks);
        //root.add(mob, iw, ih+(sizeSdks/2), 1, 1);

        itemView = new WButton(new ItemIcon(Items.AIR)){
            @Override
            public void addTooltip(TooltipBuilder tooltip) {
                tooltip.add(Text.translatable("better_anvil.item_view_button.tooltip"));
            }
        };
        playerView = new WButton(new TextureIcon(new Identifier(BetterAnvil.ID,"gui/sprites/player_skin.png"))){
            @Override
            public void addTooltip(TooltipBuilder tooltip) {
                tooltip.add(Text.translatable("better_anvil.player_view_button.tooltip"));
            }
        };

        root.add(itemView,w + panelWidth + 2,h + 2,20,30);
        root.add(playerView,w + panelWidth + 2,h + 24,20,30);

        //
        //
        //

        int tff = 130;
        favorite.add(textFieldF, getWPos(tff), favoriteButtonPos + 24, tff, 10);
        favorite.add(labelF, 0, favoriteButtonPos, panelWidth, 10);

        panel.setBackgroundPainter(Painters.themePainter);

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

        root.add(panel, w, h, panelWidth, panelDHeight);

        setRootPanel(root);
        root.validate(this);

    }

    @Override
    public void addPainters() {

    }

    public void createLists(){
        createFavoriteNameList(favorite,dataF);
        setButtons();
    }

    @Override
    public LinkedHashSet<CITItem> getSearchData(WTextField wTextField, LinkedHashSet<CITItem> data) {
        LinkedHashSet<CITItem> dataSearch = new LinkedHashSet<>();

        if (data == null) {
            return dataSearch;
        }
        if (data.isEmpty()) {
            return dataSearch;
        }

        for(CITItem item : data) {
            if (item.getCustomName().toLowerCase().replace("ё", "е").contains(wTextField.getText().toLowerCase().replace("ё", "е"))) {
                dataSearch.add(item);
            }
        }

        return dataSearch;
    }

    @Override
    public LinkedHashSet<CITItem> getSearchPackData(WTextField wTextField, LinkedHashSet<CITItem> data) {
        LinkedHashSet<CITItem> dataSearch = new LinkedHashSet<>();

        if (data == null) {
            return dataSearch;
        }
        if (data.isEmpty()) {
            return dataSearch;
        }

        for(CITItem item : data) {
            if (item.getResourcePack().toLowerCase().replace("ё", "е").contains(wTextField.getText().toLowerCase().replace("*", "").replace("ё", "е"))) {
                dataSearch.add(item);
            }
        }

        return dataSearch;
    }

    @Override
    public LinkedHashSet<CITItem> getPackItems(String pack) {
        LinkedHashSet<CITItem> citItems = new LinkedHashSet<>();

        for (CITItem item : dataD) {
            if (item.getResourcePack().equals(pack)) {
                citItems.add(item);
            }
        }

        return citItems;
    }

    @Override
    public LinkedHashSet<CITItem> getServerPackItems() {
        LinkedHashSet<CITItem> citItems = new LinkedHashSet<>();

        for (CITItem item : dataD) {
            if(item.getResourcePack().equals("Server") && PackManager.getServerResourcePack().get() != null){
                citItems.add(item);
            }
        }
        return citItems;
    }

    @Override
    public Identifier getDarkOrWhiteTexture(Identifier dark, Identifier white) {
        return LibGui.isDarkMode() ? dark : white;
    }

    @Override
    public int getWPos(int width) {
        return (panelWidth - width) / 2;
    }

    @Override
    public void createFavoriteNameList(WPlainPanel root, LinkedHashSet<CITItem> data) {
        root.remove(emptyF);
        root.remove(wListPanelF);

        if (data == null) {
            data = new LinkedHashSet<>();
        }

        wListPanelF = new WListPanelExt<>(data.stream().toList(), WMyListPanel::new, configuratorF);
        wListPanelF.getScrollBar().setHost(this);
        wListPanelF.setListItemHeight(buttonHeight);
        wListPanelF.setBackgroundPainter(Painters.listPainter);
        wListPanelF.layout();

        int d = 8;

        root.add(wListPanelF, d, favoriteButtonPos + 50, panelWidth - (d * 2), (panelFHeight - (favoriteButtonPos + 50)) - d);

        if (data.isEmpty()) {
            emptyF = new WLabel(Text.translatable("better_anvil.search.empty"));
            emptyF.setHorizontalAlignment(HorizontalAlignment.CENTER);
            emptyF.setVerticalAlignment(VerticalAlignment.CENTER);

            emptyF.setColor(0xFFFFFFFF);
            emptyF.setDarkmodeColor(0xbcbcbc);

            root.add(emptyF, d, favoriteButtonPos + 50, panelWidth - (d * 2), (panelFHeight - (favoriteButtonPos + 50)) - d);
        }
    }

    @Override
    public void createAllNameList(WPlainPanel root, LinkedHashSet<CITItem> data) {
        root.remove(emptyD);
        root.remove(wListPanelD);

        if (data == null) {
            data = new LinkedHashSet<>();
        }

        wListPanelD = new WListPanelExt<>(data.stream().toList(), WMyListPanel::new, configuratorD);
        wListPanelD.getScrollBar().setHost(this);
        wListPanelD.setListItemHeight(buttonHeight);
        wListPanelD.setBackgroundPainter(Painters.listPainter);
        wListPanelD.layout();

        int d = 8;
        root.add(wListPanelD, d, favoriteButtonPos + 50, panelWidth - (d * 2), copyButtonPos - 88);

        if (data.isEmpty()) {
            emptyD = new WLabel(Text.translatable("better_anvil.search.empty"));
            emptyD.setHorizontalAlignment(HorizontalAlignment.CENTER);
            emptyD.setVerticalAlignment(VerticalAlignment.CENTER);

            emptyD.setColor(0xFFFFFFFF);
            emptyD.setDarkmodeColor(0xbcbcbc);

            root.add(emptyD, d, favoriteButtonPos + 50, panelWidth - (d * 2), copyButtonPos - 88);
        }
    }
    @Override
    public LinkedHashSet<CITItem> getSearchItemData(WTextField field, LinkedHashSet<CITItem> data) {
        LinkedHashSet<CITItem> searchData = new LinkedHashSet<>();
        String text = field.getText();
        for(CITItem item : data){
            for(String itemMc : item.getItems()){
                if(ItemUtils.getItemById(itemMc).asItem().getName().getString().toLowerCase().replace('ё','е').contains(text.substring(1).toLowerCase().replace('ё','е'))) searchData.add(item);
            }
        }
        return searchData;
    }

    @Override
    public void clearButtons() {
        for (WTabButton button : buttons) {
            root.remove(button);
        }
    }

    @Override
    public void setButtons() {

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
            createAllNameList(panel, new LinkedHashSet<>());
            return;
        }

        for (ResourcePackProfile profile : profiles) {
            String pack = getPack(profile);


            LinkedHashSet<CITItem> citItems;

            if (pack.equals("server")) {
                citItems = getServerPackItems();
            } else {
                citItems = getPackItems(pack);
            }

            if (!citItems.isEmpty()) {
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
            buttonsSub.add(buttons.stream().toList().get(i));
        }
        createButtons(buttonsSub);
    }

    @Override
    public void createButtons(LinkedHashSet<WTabButton> buttonsSub) {
        if (buttonsSub.isEmpty()) {
            return;
        }
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

    @Override
    public void nextButtons() {
        s++;

        int startIndex = s * 7;
        int endIndex = startIndex + 7;
        if (endIndex > buttons.size()) {
            endIndex = buttons.size();
        }

        buttonsSub.clear();
        buttonsSub.addAll(buttons.stream().toList().subList(startIndex, endIndex));

        clearButtons();
        createButtons(buttonsSub);

    }

    @Override
    public void backButtons() {
        s--;

        int startIndex = s * 7;
        int endIndex = startIndex + 7;
        if (endIndex > buttons.size()) {
            endIndex = buttons.size();
        }

        buttonsSub.clear();
        buttonsSub.addAll(buttons.stream().toList().subList(startIndex, endIndex));

        clearButtons();
        createButtons(buttonsSub);
    }


}
