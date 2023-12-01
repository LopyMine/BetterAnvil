package net.lopymine.betteranvil.gui.description;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.text.Text;

import net.fabricmc.fabric.api.util.TriState;

import io.github.cottonmc.cotton.gui.client.*;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.*;
import io.github.cottonmc.cotton.gui.widget.icon.*;

import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.config.BetterAnvilConfig;
import net.lopymine.betteranvil.config.enums.FavoriteMenuPositions;
import net.lopymine.betteranvil.fake.FakeClientPlayerEntity;
import net.lopymine.betteranvil.gui.description.interfaces.*;
import net.lopymine.betteranvil.gui.panels.WConfigPanel;
import net.lopymine.betteranvil.gui.search.SearchTags;
import net.lopymine.betteranvil.gui.widgets.buttons.*;
import net.lopymine.betteranvil.gui.widgets.buttons.WStarButton.State;
import net.lopymine.betteranvil.gui.widgets.buttons.WSwitcher.Type;
import net.lopymine.betteranvil.gui.widgets.entity.*;
import net.lopymine.betteranvil.gui.widgets.fields.*;
import net.lopymine.betteranvil.utils.*;

import java.util.*;
import java.util.function.BiConsumer;

import static net.lopymine.betteranvil.BetterAnvil.LOGGER;

public class GuiDescription<H extends GuiHandler<I>, I> extends LightweightGuiDescription implements ITabbedGui<I>, IConfigAccessor {
    protected final BetterAnvilConfig config = BetterAnvilConfig.getInstance();
    protected final List<WTabButton> tabButtons = new ArrayList<>();
    protected WTabButton favoriteTab;

    protected final WPlainPanel mainPanel = new WPlainPanel();
    protected final WPlainPanel favoritePanel = new WPlainPanel();

    protected final int screenHeight;
    protected final int screenWidth;
    protected final int panelDHeight;
    protected final int panelWidth = 208;
    protected final int ind = 5;
    protected final int selectButtonPos;
    protected final int favoriteButtonPos = 9;
    protected final int x;
    protected final int y;
    protected final int droppedItemPosX;
    protected final int droppedItemPosY;
    protected final int entitiesSize;

    protected boolean isFavoriteOpen = false;
    protected boolean hasFavoriteTab = false;
    protected int panelFHeight = 244;
    protected int s = 0;

    protected HashMap<String, LinkedHashSet<I>> mainList = new HashMap<>();
    protected LinkedHashSet<I> favoriteList = new LinkedHashSet<>();

    protected String active_pack = "all";
    protected MinecraftClient client;
    protected final Screen parent;

    protected final WPlainPanel root = new WPlainPanel() {
        @Override
        public InputResult onKeyPressed(int ch, int key, int modifiers) {
            if (ch == 256) {
                GuiDescription.this.client.setScreen(GuiDescription.this.parent);
                return InputResult.PROCESSED;
            }
            if (ch == 70) {
                GuiDescription.this.mob.swapItem();
                return InputResult.PROCESSED;
            }

            return InputResult.IGNORED;
        }
    }.setInsets(Insets.ROOT_PANEL);

    protected WLabel favoriteListEmptyLabel = new WLabel(Text.translatable("better_anvil.search.empty"))
            .setHorizontalAlignment(HorizontalAlignment.CENTER)
            .setVerticalAlignment(VerticalAlignment.CENTER)
            .setColor(0xFFFFFFFF, 0xbcbcbc);

    protected WLabel mainListEmptyLabel = new WLabel(Text.translatable("better_anvil.search.empty"))
            .setHorizontalAlignment(HorizontalAlignment.CENTER)
            .setVerticalAlignment(VerticalAlignment.CENTER)
            .setColor(0xFFFFFFFF, 0xbcbcbc);

    protected final WSwitcher switcherLeft = new WSwitcher(Type.LEFT).setOnClick(this::backSwitch);
    protected final WSwitcher switcherRight = new WSwitcher(Type.RIGHT).setOnClick(this::nextSwitch);

    public BiConsumer<I, WConfigPanel> favoriteConsumer = (I s, WConfigPanel panel) -> {
    };
    public BiConsumer<I, WConfigPanel> mainConsumer = (I s, WConfigPanel panel) -> {
    };

    protected WListPanel<I, WConfigPanel> favoriteListPanel;
    protected WListPanel<I, WConfigPanel> mainListPanel;

    protected WField field = new WField();

    protected WLabel favoriteTitle = new WLabel(Text.translatable("better_anvil.favorite.title")).setHorizontalAlignment(HorizontalAlignment.CENTER);
    protected WLabel mainTitle;

    protected WAutoCompleterTextField mainTextField = new WAutoCompleterTextField(ResourcePackUtils.getStringResourcePacksWithServer(), Text.translatable("better_anvil.search"))
            .setMaxLength(200)
            .setChangedListener(text -> {
                LinkedHashSet<I> list = (isOpenFavoriteTab() ? favoriteList : mainList.get(active_pack));
                BiConsumer<I, WConfigPanel> consumer = (isOpenFavoriteTab() ? favoriteConsumer : mainConsumer);

                if (list == null) {
                    list = new LinkedHashSet<>();
                }

                if (text.isEmpty()) {
                    createMainListPanel(mainPanel, list, consumer);
                    return;
                }

                LinkedHashSet<I> search;
                SearchTags tag = SearchTags.getTag(text);

                if (tag != null) {
                    final String textWithoutTags = tag.getTextContent(text);

                    search = switch (tag) {
                        case ITEM -> GuiDescription.this.handler.getSearchByItem(textWithoutTags, list);
                        case RESOURCE_PACK -> GuiDescription.this.handler.getSearchByPack(textWithoutTags, list);
                        case ENCHANTMENTS -> GuiDescription.this.handler.getSearchByEnchantments(textWithoutTags, list);
                        case LORE -> GuiDescription.this.handler.getSearchByLore(textWithoutTags, list);
                        case COUNT -> GuiDescription.this.handler.getSearchByCount(textWithoutTags, list);
                        case DAMAGE -> GuiDescription.this.handler.getSearchByDamage(textWithoutTags, list);

                    };
                } else {
                    if (text.startsWith("#")) {
                        search = list;
                    } else {
                        search = GuiDescription.this.handler.getSearch(text, list);
                    }
                }

                createMainListPanel(mainPanel, (search == null ? list : search), consumer);
            });

    protected final WAutoCompleterTextField favoriteTextField = new WAutoCompleterTextField(ResourcePackUtils.getStringResourcePacksWithServer(), Text.translatable("better_anvil.search"))
            .setMaxLength(200)
            .setChangedListener(text -> {
                if (text.isEmpty()) {
                    createFavoriteListPanel(favoritePanel, favoriteList, favoriteConsumer);
                    return;
                }

                LinkedHashSet<I> search;
                SearchTags tag = SearchTags.getTag(text);
                if (tag != null) {
                    final String textWithoutTags = tag.getTextContent(text);
                    search = switch (tag) {
                        case ITEM -> GuiDescription.this.handler.getSearchByItem(textWithoutTags, favoriteList);
                        case RESOURCE_PACK -> GuiDescription.this.handler.getSearchByPack(textWithoutTags, favoriteList);
                        case ENCHANTMENTS -> GuiDescription.this.handler.getSearchByEnchantments(textWithoutTags, favoriteList);
                        case LORE -> GuiDescription.this.handler.getSearchByLore(textWithoutTags, favoriteList);
                        case COUNT -> GuiDescription.this.handler.getSearchByCount(textWithoutTags, favoriteList);
                        case DAMAGE -> GuiDescription.this.handler.getSearchByDamage(textWithoutTags, favoriteList);
                    };
                } else {
                    if (text.startsWith("#")) {
                        search = favoriteList;
                    } else {
                        search = GuiDescription.this.handler.getSearch(text, favoriteList);
                    }
                }

                createFavoriteListPanel(favoritePanel, search, favoriteConsumer);
            });

    protected WDroppedItem droppedItem = new WDroppedItem(Items.AIR.getDefaultStack());
    protected WMob mob = new WMob(FakeClientPlayerEntity.getInstance());

    protected WStarButton openFavoriteMenuButton = new WStarButton().setOnToggle(on -> {
        isFavoriteOpen = on;

        createFavoriteListPanel(favoritePanel, favoriteList, favoriteConsumer);

        if (isFavoriteOpen) {
            root.add(favoritePanel, (GuiDescription.this.screenWidth / 2 - (panelWidth / 2)) - (panelWidth + 7), GuiDescription.this.y, panelWidth, panelFHeight);
        }
        if (!isFavoriteOpen) {
            root.remove(favoritePanel);
        }
    });
    protected WButton selectButton = new WButton(Text.translatable("better_anvil.button.select"));

    protected WButton itemPreviewButton = new WButton(new ItemIcon(Items.AIR)) {
        @Override
        public void addTooltip(TooltipBuilder tooltip) {
            tooltip.add(Text.translatable("better_anvil.item_view_button.tooltip"));
        }
    };
    protected WButton playerPreviewButton = new WButton(new TextureIcon(BetterAnvil.i("textures/gui/buttons/player_icon.png"))) {
        @Override
        public void addTooltip(TooltipBuilder tooltip) {
            tooltip.add(Text.translatable("better_anvil.player_view_button.tooltip"));
        }
    };

    protected final H handler;

    protected GuiDescription(H handler, Screen parent, Text title) {
        this.handler = handler;
        this.parent = parent;
        this.client = MinecraftClient.getInstance();
        this.mainTitle = new WLabel(title).setHorizontalAlignment(HorizontalAlignment.CENTER);

        // Setting Main Panel
        Screen screen = client.currentScreen != null ? client.currentScreen : parent;

        screenWidth = screen.width;
        screenHeight = screen.height;

        root.setSize(screenWidth, screenHeight);

        int panelHeight = screenHeight - (ind * 2);
        int tabHeight = 32;

        panelDHeight = panelHeight - tabHeight;
        selectButtonPos = panelDHeight - 30;

        x = (screenWidth / 2 - (panelWidth / 2)) - 5;
        y = ((ind + tabHeight) - 7);

        if ((((panelWidth * 2 + 2) + x) + 2 > screenWidth || config.favoriteMenuPositionEnum == FavoriteMenuPositions.TAB) && config.favoriteMenuPositionEnum != FavoriteMenuPositions.PANEL) {
            hasFavoriteTab = true;
        }

        panelFHeight = panelDHeight <= 240 ? 180 : panelFHeight;

        // Setting Widgets

        int entitiesPanelWidth = screenWidth - (x + panelWidth);

        entitiesSize = 10 * (entitiesPanelWidth / 40) + 20;
        droppedItem.setEntitySize(entitiesSize);
        mob.setEntitySize(entitiesSize);

        droppedItem.setScissors(x + panelWidth, 0, screenWidth, screenHeight);
        mob.setScissors(x + panelWidth, 0, screenWidth, screenHeight);

        droppedItemPosX = screenWidth - (entitiesPanelWidth / 2);
        droppedItemPosY = screenHeight / 2;

        // Adding Widgets
        root.add(droppedItem, droppedItemPosX, droppedItemPosY, 1, 1);
        root.add(itemPreviewButton, x + panelWidth + 2, y + 2, 20, 20);
        root.add(playerPreviewButton, x + panelWidth + 2, y + 24, 20, 20);

        favoritePanel.setBackgroundPainter(Painters.BACKGROUND_PAINTER);
        favoritePanel.add(favoriteTextField, calcPos(130), favoriteButtonPos + 24, 130, 10);
        favoritePanel.add(favoriteTitle, 0, favoriteButtonPos, panelWidth, 10);

        mainPanel.setBackgroundPainter(Painters.BACKGROUND_PAINTER);
        mainPanel.add(mainTitle, 0, favoriteButtonPos, panelWidth, 10);
        if (!hasFavoriteTab) {
            mainPanel.add(openFavoriteMenuButton, 10, favoriteButtonPos);
        }
        mainPanel.add(selectButton, calcPos(174), selectButtonPos, 174, 20);
        mainPanel.add(field, calcPos(field.getWidth()), selectButtonPos - 26);
        mainPanel.add(mainTextField, calcPos(130), favoriteButtonPos + 24, 130, 10);

        root.add(mainPanel, x, y, panelWidth, panelDHeight);

        setRootPanel(root);
        favoritePanel.validate(this);
        root.validate(this);
    }

    protected void init() {
        createFavoriteListPanel(favoritePanel, favoriteList, favoriteConsumer);
        initTabs();
    }

    @Override
    public void createFavoriteListPanel(WPlainPanel root, LinkedHashSet<I> list, BiConsumer<I, WConfigPanel> consumer) {
        if (hasFavoriteTab && favoriteTab != null) {
            if (isOpenFavoriteTab()) {
                createMainListPanel(mainPanel, list, consumer);
            }
            return;
        }

        root.remove(favoriteListEmptyLabel);
        root.remove(favoriteListPanel);

        if (list == null) {
            list = new LinkedHashSet<>();
        }

        int scroll = (favoriteListPanel != null ? favoriteListPanel.getScrollBar().getValue() : 0);
        favoriteListPanel = new WListPanel<>(list.stream().toList(), WConfigPanel::new, consumer);
        favoriteListPanel.getScrollBar().setValue(scroll).setScrollingSpeed(1).setHost(this);
        favoriteListPanel.setHost(this);
        favoriteListPanel.setListItemHeight(config.spacing);
        favoriteListPanel.setBackgroundPainter(Painters.CONFIG_PAINTER);
        favoriteListPanel.layout();

        int d = 8;

        root.add(favoriteListPanel, d, favoriteButtonPos + 50, panelWidth - (d * 2), (panelFHeight - (favoriteButtonPos + 50)) - d);

        if (list.isEmpty()) {
            root.add(favoriteListEmptyLabel, d, favoriteButtonPos + 50, panelWidth - (d * 2), (panelFHeight - (favoriteButtonPos + 50)) - d);
            favoriteListEmptyLabel.setHost(this);
        }
    }

    @Override
    public void createMainListPanel(WPlainPanel root, LinkedHashSet<I> list, BiConsumer<I, WConfigPanel> consumer) {
        root.remove(mainListEmptyLabel);
        root.remove(mainListPanel);

        if (list == null) {
            list = new LinkedHashSet<>();
        }

        int scroll = (mainListPanel != null ? mainListPanel.getScrollBar().getValue() : 0);
        mainListPanel = new WListPanel<>(list.stream().toList(), WConfigPanel::new, consumer);
        mainListPanel.getScrollBar().setValue(scroll).setScrollingSpeed(1).setHost(this);
        mainListPanel.setHost(this);
        mainListPanel.setListItemHeight(config.spacing);
        mainListPanel.setBackgroundPainter(Painters.CONFIG_PAINTER);
        mainListPanel.layout();

        int d = 8;
        root.add(mainListPanel, d, favoriteButtonPos + 50, panelWidth - (d * 2), selectButtonPos - 88);

        if (list.isEmpty()) {
            root.add(mainListEmptyLabel, d, favoriteButtonPos + 50, panelWidth - (d * 2), selectButtonPos - 88);
            mainListEmptyLabel.setHost(mainListPanel.getHost());
        }
    }

    @Override
    public void clearTabs() {
        for (WTabButton tabButton : tabButtons) {
            root.remove(tabButton);
        }
    }

    @Override
    public WTabButton createDefaultTab(LinkedHashSet<I> list) {
        WTabButton tabButton = new WTabButton(null, new ItemIcon(Items.COMPASS));

        tabButton.setOnToggle((on) -> {
            if (!on) {
                return;
            }
            active_pack = "all";

            for (WTabButton button : tabButtons) {
                if (!button.equals(tabButton)) {
                    button.setToggle(false);
                }
            }

            createMainListPanel(mainPanel, list, mainConsumer);
        });

        return tabButton;
    }

    @Override
    public WTabButton createFavoriteTab(LinkedHashSet<I> list) {
        WTabButton tabButton = new WTabButton("Favorite", (context, x, y, size) -> {
            float px = 1 / 14f;

            float buttonLeft = 0;
            float buttonTop = 0;
            float buttonWidth = 7 * px;
            ScreenDrawing.texturedRect(context, x + 1, y + 1, 16, 16, State.STARRED.getTexture(), buttonLeft, buttonTop, buttonLeft + buttonWidth, buttonTop + buttonWidth, 0xFFFFFFFF);
        });

        tabButton.setOnToggle((on) -> {
            if (!on) {
                return;
            }
            active_pack = "favorite";

            for (WTabButton button : tabButtons) {
                if (!button.equals(tabButton)) {
                    button.setToggle(false);
                }
            }

            createMainListPanel(mainPanel, list, favoriteConsumer);
        });

        return tabButton;
    }

    @Override
    public void initTabs() {
        boolean hasDefaultTab = false;

        for (String resourcePack : mainList.keySet()) {
            LinkedHashSet<I> list = mainList.get(resourcePack);

            if (resourcePack.equals("all")) {
                WTabButton defaultTab = createDefaultTab((list == null ? new LinkedHashSet<>() : list));
                tabButtons.add(defaultTab);
                hasDefaultTab = true;
                continue;
            }

            if (list == null || list.isEmpty()) {
                LOGGER.warn("[CIT GUI] Failed to get items for " + resourcePack + "!");
                continue;
            }

            ResourcePackProfile profile = MinecraftClient.getInstance().getResourcePackManager().getProfile(resourcePack);
            String pack = ResourcePackUtils.getResourcePackNameWithZip(resourcePack);
            WTabButton tabButton = new WTabButton(pack, (profile != null ? new TextureIcon(ResourcePackUtils.getPackIcon(profile)) : new ItemIcon(Items.AIR)));
            tabButton.setOnToggle((on) -> {
                if (!on) {
                    return;
                }

                active_pack = resourcePack;

                for (WTabButton tab : tabButtons) {
                    if (!tab.equals(tabButton)) {
                        tab.setToggle(false);
                    }
                }

                createMainListPanel(mainPanel, list, mainConsumer);
            });

            tabButtons.add(tabButton);
        }

        if (hasFavoriteTab) {
            int f = (hasDefaultTab ? 1 : 0);
            WTabButton favoriteTab = createFavoriteTab(favoriteList);
            this.favoriteTab = favoriteTab;
            tabButtons.add(f, favoriteTab);
        }

        int d = tabButtons.size();
        if (d > 7) {
            d = 7;
        }

        List<WTabButton> list = new ArrayList<>();
        for (int i = 0; i < d; i++) {
            list.add(tabButtons.get(i));
        }

        addTabs(list);
    }

    @Override
    public void addTabs(List<WTabButton> list) {
        if (list.isEmpty()) {
            WTabButton defaultTab = createDefaultTab(new LinkedHashSet<>());
            list.add(defaultTab);
        }

        initSwitchers();

        root.remove(mainPanel);

        int x = 0;
        for (WTabButton button : list) {
            root.add(button, this.x + x, y - 29);
            x = x + 30;
        }

        root.add(mainPanel, this.x, y, panelWidth, panelDHeight);

        WTabButton button = list.get(0);
        button.setToggle(true, true);
    }

    @Override
    public void nextSwitch() {
        s++;

        int startIndex = s * 7;
        int endIndex = startIndex + 7;
        if (endIndex > tabButtons.size()) {
            endIndex = tabButtons.size();
        }

        clearTabs();
        addTabs(tabButtons.subList(startIndex, endIndex));
    }

    @Override
    public void backSwitch() {
        s--;

        int startIndex = s * 7;
        int endIndex = startIndex + 7;
        if (endIndex > tabButtons.size()) {
            endIndex = tabButtons.size();
        }

        clearTabs();
        addTabs(tabButtons.subList(startIndex, endIndex));
    }

    @Override
    public void initSwitchers() {
        int page = (int) Math.ceil((double) tabButtons.size() / 7);

        root.remove(switcherLeft);
        root.remove(switcherRight);

        root.add(switcherRight, x + panelWidth + 2, y - 26);
        root.add(switcherLeft, x - 15, y - 26);

        if (s <= 0) {
            root.remove(switcherLeft);
        }

        if (s >= page || s == page - 1) {
            root.remove(switcherRight);
        }
    }

    @Override
    public void addPainters() {
        // Remove root panel background
    }

    private boolean isOpenFavoriteTab() {
        return hasFavoriteTab && favoriteTab != null && favoriteTab.getToggle();
    }

    protected int calcPos(int width) {
        return (panelWidth - width) / 2;
    }

    @Override
    public TriState isDarkMode() {
        return TriState.of(getConfig().isDarkMode);
    }

    @Override
    public BetterAnvilConfig getConfig() {
        return config;
    }
}
