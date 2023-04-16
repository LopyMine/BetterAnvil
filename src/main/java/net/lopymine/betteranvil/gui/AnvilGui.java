package net.lopymine.betteranvil.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LibGui;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.cit.ConfigParser;
import net.lopymine.betteranvil.cit.writers.FavoriteWriter;
import net.lopymine.betteranvil.gui.my.widget.Switcher;
import net.lopymine.betteranvil.gui.my.widget.WFavoriteButton;
import net.lopymine.betteranvil.gui.my.widget.WSwitcher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class AnvilGui extends LightweightGuiDescription {

    protected abstract void renameMethod(String name);

    public static final int maxLength = 18;
    @Override
    public void addPainters() {
        super.addPainters();
    }
    public static final int maxLengthBigLabel = 20;
    private static WLabel empty;
    private static WListPanel<String, MyWListPanel> wListPanel;
    private static WLabel emptyF;
    private static WListPanel<String, MyWListPanel> wListPanelF;
    private static WLabel emptyD;
    private static WListPanel<String, MyWListPanel> wListPanelD;
    private static boolean favoriteWindowOn;
    private BiConsumer<String, MyWListPanel> configuratorF;
    private BiConsumer<String, MyWListPanel> configurator;
    private BiConsumer<String, MyWListPanel> configuratorD;
    public final Identifier bigFieldNameTextureFocusDark = new Identifier(BetterAnvil.MOD_ID, "gui/namefieldfocusdark.png");
    public final Identifier bigFieldNameTextureNotFocusDark = new Identifier(BetterAnvil.MOD_ID, "gui/namefielddark.png");
    public final Identifier bigFieldNameTextureFocus = new Identifier(BetterAnvil.MOD_ID, "gui/namefieldfocus.png");
    public final Identifier bigFieldNameTextureNotFocus = new Identifier(BetterAnvil.MOD_ID, "gui/namefield.png");
    public final Identifier bfnTexture = getDarkOrWhiteTexture(bigFieldNameTextureNotFocusDark, bigFieldNameTextureNotFocus);
    public final Identifier bfnTextureFocus = getDarkOrWhiteTexture(bigFieldNameTextureFocusDark, bigFieldNameTextureFocus);
    private static Integer el = 0;
    private static final WSwitcher switcherLeft = new WSwitcher(Switcher.LEFT);
    private static final WSwitcher switcherRight = new WSwitcher(Switcher.RIGHT);
    private final ArrayList<WPlainPanel> roots = new ArrayList<>();
    private final ArrayList<ResourcePackProfile> rpProfiles = new ArrayList<>();
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final WTabPanel wTabPanel = new WTabPanel();
    private final WPlainPanel wGridPanel = new WPlainPanel();
    private final WSprite bigFieldName = new WSprite(bfnTexture);
    private final WLabel itemName = new WLabel(Text.of(" "));
    private final WButton copyButton = new WButton(Text.translatable("gui.betteranvil.button.copy"));
    private final WButton copyButtonD = new WButton(Text.translatable("gui.betteranvil.button.copy"));
    private final WItemSlot anvilSlot = new WItemSlot(new PlayerInventory(mc.player), 1,1,1, true);
    private final WFavoriteButton wFavoriteButton = new WFavoriteButton();
    private WPlainPanel favorite = new WPlainPanel();
    private WPlainPanel panel = new WPlainPanel();
    private ArrayList<String> dataF = new ArrayList<>();
    private ArrayList<String> dataD = new ArrayList<>();
    private final ItemStack anvilItem;
    private final Screen parent;

    public AnvilGui(Screen parentt, ItemStack anvilItemm) {
        wGridPanel.setSize(800, 500);

        anvilItem = anvilItemm;

        parent = parentt;

        anvilSlot.setIcon(new ItemIcon(anvilItem));

        switcherRight.setOnClick(()->{
            if(el < 5){
                ArrayList<WPlainPanel> firstTwo = new ArrayList<>(roots.subList(el, el + 2));
                System.out.println("two elements: " + firstTwo);
                el++;
                System.out.println(el);
            }
            System.out.println("123");
        });

        switcherLeft.setOnClick(()->{
            System.out.println("123");
        });

        wFavoriteButton.setOnToggle(on -> {
            favoriteWindowOn = on;

            createFavoriteNameList(favorite, dataF);

            if (favoriteWindowOn) {
                wGridPanel.add(favorite, 84, 63);
                favorite.setBackgroundPainter(BackgroundPainter.VANILLA);
                //favorite.setBackgroundPainter(MyWListPanel.backgroundPainter);
                favorite.layout();
                favorite.setSize(205, 230);
            }
            if (!favoriteWindowOn) {
                wGridPanel.remove(favorite);
            }

        });

        wGridPanel.add(settingTabs(), 293, 33);
        wGridPanel.setInsets(Insets.ROOT_PANEL);

        //I don't know what do that...
        setRootPanel(wGridPanel);

        wGridPanel.validate(this);

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

    public void createNameList(WPlainPanel root, ArrayList<String> data) {
        root.remove(empty);
        root.remove(wListPanel);
        if (data.isEmpty()) {
            empty = new WLabel(Text.translatable("gui.betteranvil.textfield.search.empty"));
            root.add(empty, 90, 170);
            empty.setHorizontalAlignment(HorizontalAlignment.CENTER);
            empty.setVerticalAlignment(VerticalAlignment.CENTER);
        } else {
            wListPanel = new WListPanel<>(data, MyWListPanel::new, configurator);
            wListPanel.getScrollBar().setHost(this);
            wListPanel.setListItemHeight(30);
            wListPanel.setBackgroundPainter(MyWListPanel.backgroundPainter);
            wListPanel.layout();
            //(200, 400);
            root.add(wListPanel, 5, 70, 192, 245);
        }
    }

    public void createFavoriteNameList(WPlainPanel root, ArrayList<String> data) {
        root.remove(emptyF);
        root.remove(wListPanelF);

        if (data.isEmpty()) {
            emptyF = new WLabel(Text.translatable("gui.betteranvil.textfield.search.empty"));
            root.add(emptyF, 90, 100);
            emptyF.setHorizontalAlignment(HorizontalAlignment.CENTER);
            emptyF.setVerticalAlignment(VerticalAlignment.CENTER);
        } else {
            wListPanelF = new WListPanel<>(data, MyWListPanel::new, configuratorF);
            wListPanelF.getScrollBar().setHost(this);
            wListPanelF.setListItemHeight(30);
            wListPanelF.setBackgroundPainter(MyWListPanel.backgroundPainter);
            wListPanelF.layout();
            //180 200
            root.add(wListPanelF, 5, 70, 193, 150);
        }
    }

    public void createAllNameList(WPlainPanel root, ArrayList<String> data) {
        root.remove(emptyD);
        root.remove(wListPanelD);
        if (data.isEmpty()) {
            emptyD = new WLabel(Text.translatable("gui.betteranvil.textfield.search.empty"));
            root.add(emptyD, 90, 170);
            emptyD.setHorizontalAlignment(HorizontalAlignment.CENTER);
            emptyD.setVerticalAlignment(VerticalAlignment.CENTER);
        } else {
            wListPanelD = new WListPanel<>(data, MyWListPanel::new, configuratorD);
            wListPanelD.getScrollBar().setHost(this);
            wListPanelD.setListItemHeight(30);
            wListPanelD.setBackgroundPainter(MyWListPanel.backgroundPainter);
            wListPanelD.layout();
            //(200, 400);
            root.add(wListPanelD, 5, 70, 192, 245);
        }
    }

    private WPlainPanel createAllNameTab(){
        panel = new WPlainPanel();
        panel.setSize(203,400);

        WLabel labelD = new WLabel(Text.translatable("gui.betteranvil.menu"));
        labelD.setSize(6, 5);
        labelD.setVerticalAlignment(VerticalAlignment.TOP).setHorizontalAlignment(HorizontalAlignment.CENTER);
        panel.add(labelD, 85, 15);

        panel.add(wFavoriteButton, 10, 10);
        panel.add(copyButtonD, 14, 347, 175, 50);
        WButton closeButtonD = new WButton(Text.literal("х"));
        closeButtonD.setOnClick(()->{
            mc.setScreen(parent);
        });
        panel.add(closeButtonD, 172, 5, 25, 5);
        panel.add(bigFieldName, 18, 320, 165, 24);

        panel.add(itemName, 95, 329);
        panel.add(anvilSlot, 90,373);

        dataD = new ArrayList<>(ConfigParser.parseItemNames(anvilItem));

        configuratorD = (String s, MyWListPanel destination) -> {
            destination.wItemButton.setText(Text.literal(cutString(s, maxLength)));

            ItemStack anvilItemNew = new ItemStack(anvilItem.getItem().asItem());
            anvilItemNew.setCustomName(Text.of(s));
            List<ItemStack> itemStackList = new ArrayList<>();
            itemStackList.add(anvilItemNew);
            destination.wItemButton.setItemIcon(new WItem(itemStackList));

            if(FavoriteWriter.getItemNames(anvilItem).contains(s)){
                destination.favoriteButton.setToggle(true);
            }

            destination.wItemButton.setOnClick(() -> {
                bigFieldName.setImage(bfnTextureFocus);

                itemName.setText(Text.of(cutString(s, maxLengthBigLabel)));
                itemName.setHorizontalAlignment(HorizontalAlignment.CENTER);
                copyButtonD.setOnClick(() -> {
                    mc.setScreen(parent);
                    this.renameMethod(s);
                });
                System.out.println("123");
                anvilSlot.setIcon(new ItemIcon(anvilItemNew));
            });

            destination.favoriteButton.setOnToggle(on -> {
                if (on) {
                    dataF.add(s);
                    FavoriteWriter.addItem(s, anvilItem);
                    createFavoriteNameList(favorite, dataF);
                } else {
                    dataF.remove(s);
                    FavoriteWriter.removeItem(s);
                    createFavoriteNameList(favorite, dataF);
                }
            });
        };

        createAllNameList(panel, dataD);

        WTextField wTextFieldD = new WTextField(Text.translatable("gui.betteranvil.textfield.search"));
        wTextFieldD.setChangedListener(s -> {
            if (wTextFieldD.getText().isEmpty()) {
                createAllNameList(panel, dataD);
                return;
            }
            ArrayList<String> dataSearchD = new ArrayList<>();
            for (String dt : dataD) {
                if (dt.toLowerCase().replace("ё", "е").contains(wTextFieldD.getText().toLowerCase().replace("ё", "е"))) {
                    dataSearchD.add(dt);
                }
            }
            createAllNameList(panel, dataSearchD);
        });
        panel.add(wTextFieldD, 35, 40, 130, 10);

        return panel;
    }

    private void createOtherTab(){
        for (ResourcePackProfile rp : ConfigParser.getResourcePackProfiles(mc)) {
            WPlainPanel wRoot = new WPlainPanel();
            wRoot.setSize(203, 400);

            //label
            WLabel wlabel = new WLabel(Text.translatable("gui.betteranvil.menu"));
            wlabel.setSize(6, 5);
            wlabel.setVerticalAlignment(VerticalAlignment.TOP).setHorizontalAlignment(HorizontalAlignment.CENTER);
            wRoot.add(wlabel, 85, 15);

            wRoot.add(wFavoriteButton, 10, 10);
            WButton closeButton = new WButton(Text.literal("х"));
            closeButton.setOnClick(()->{
                mc.setScreen(parent);
            });
            wRoot.add(closeButton, 172, 5, 25, 5);
            wRoot.add(bigFieldName, 18, 320, 165, 24);
            wRoot.add(copyButton, 14, 347, 175, 50);
            wRoot.add(itemName, 95, 329);
            wRoot.add(anvilSlot, 90,373);

            ArrayList<String> wData = new ArrayList<>(ConfigParser.parseItemsFromConfig(rp.getName().replaceAll("file/", "").replaceAll(".zip", ""), anvilItem, ConfigParser.pathToConfigFolder));

            configurator = (String s, MyWListPanel destination) -> {
                destination.wItemButton.setText(Text.literal(cutString(s, maxLength)));

                ItemStack anvilItemNew = new ItemStack(anvilItem.getItem().asItem());
                anvilItemNew.setCustomName(Text.of(s));
                List<ItemStack> itemStackList = new ArrayList<>();
                itemStackList.add(anvilItemNew);
                destination.wItemButton.setItemIcon(new WItem(itemStackList));

                if(FavoriteWriter.getItemNames(anvilItem).contains(s)){
                    destination.favoriteButton.setToggle(true);
                }

                destination.wItemButton.setOnClick(() -> {
                    bigFieldName.setImage(bfnTextureFocus);

                    itemName.setText(Text.of(cutString(s, maxLengthBigLabel)));
                    itemName.setHorizontalAlignment(HorizontalAlignment.CENTER);
                    copyButton.setOnClick(() -> {
                        mc.setScreen(parent);
                        this.renameMethod(s);
                    });

                    anvilSlot.setIcon(new ItemIcon(anvilItemNew));
                });

                destination.favoriteButton.setOnToggle(on -> {
                    if (on) {
                        dataF.add(s);
                        FavoriteWriter.addItem(s, anvilItem);
                        createFavoriteNameList(favorite, dataF);
                    } else {
                        dataF.remove(s);
                        FavoriteWriter.removeItem(s);
                        createFavoriteNameList(favorite, dataF);
                    }
                });
            };

            createNameList(wRoot, wData);

            WTextField wTextField = new WTextField(Text.translatable("gui.betteranvil.textfield.search"));
            wTextField.setChangedListener(s -> {
                if (wTextField.getText().isEmpty()) {
                    createNameList(wRoot, wData);
                    return;
                }
                ArrayList<String> wdataSearch = new ArrayList<>();
                for (String dt : wData) {
                    if (dt.toLowerCase().replace("ё", "е").contains(wTextField.getText().toLowerCase().replace("ё", "е"))) {
                        wdataSearch.add(dt);
                    }
                }
                createNameList(wRoot, wdataSearch);
            });
            wRoot.add(wTextField, 35, 40, 130, 10);
            if(!wData.isEmpty()){
                roots.add(wRoot);
                rpProfiles.add(rp);
            }
        }
    }

    private void createFavoriteTab(){
        favorite = new WPlainPanel();
        favorite.setSize(204, 230);
        favorite.setHost(this);
        favorite.add(new WLabel(Text.translatable("gui.betteranvil.tooltip.textfield")).setHorizontalAlignment(HorizontalAlignment.CENTER), 95, 15);

        dataF = new ArrayList<>(FavoriteWriter.getItemNames(anvilItem));

        configuratorF = (String s, MyWListPanel destination) -> {

            destination.favoriteButton.setToggle(true);

            ItemStack anvilItemNew = new ItemStack(anvilItem.getItem().asItem());
            anvilItemNew.setCustomName(Text.of(s));
            List<ItemStack> itemStackList = new ArrayList<>();
            itemStackList.add(anvilItemNew);
            destination.wItemButton.setItemIcon(new WItem(itemStackList));

            destination.wItemButton.setOnClick(()->{
                bigFieldName.setImage(bfnTextureFocus);
                itemName.setText(Text.of(cutString(s, maxLengthBigLabel)));
                anvilSlot.setIcon(new ItemIcon(anvilItemNew));
                itemName.setHorizontalAlignment(HorizontalAlignment.CENTER);

                copyButton.setOnClick(() -> {
                    mc.setScreen(parent);
                    this.renameMethod(s);
                });
                copyButtonD.setOnClick(() -> {
                    mc.setScreen(parent);
                    this.renameMethod(s);
                });
            });

            destination.wItemButton.setText(Text.literal(cutString(s, maxLength)));

            destination.favoriteButton.setOnToggle(on->{
                if(!on){
                    FavoriteWriter.removeItem(s);
                    dataF.remove(s);
                    createFavoriteNameList(favorite, dataF);
                }
            });
        };

        WTextField textFieldF = new WTextField(Text.translatable("gui.betteranvil.textfield.search"));
        textFieldF.setHost(favorite.getHost());
        textFieldF.setChangedListener(s -> {
            if (textFieldF.getText().isEmpty()) {
                createFavoriteNameList(favorite, dataF);
                return;
            }
            ArrayList<String> dataSearch = new ArrayList<>();
            for (String dt : dataF) {
                if (dt.toLowerCase().replace("ё", "е").contains(textFieldF.getText().toLowerCase().replace("ё", "е"))) {
                    dataSearch.add(dt);
                }
            }
            createFavoriteNameList(favorite, dataSearch);
        });

        favorite.add(textFieldF, 45, 40, 110, 10);

        createFavoriteNameList(favorite, dataF);
    }

    private Identifier getDarkOrWhiteTexture(Identifier dark, Identifier white){
        return LibGui.isDarkMode() ? dark : white;
    }

    private WTabPanel settingTabs() {

        createFavoriteTab();

        wTabPanel.add(createAllNameTab(), tab -> tab.icon(new ItemIcon(Items.NAME_TAG)));

        createOtherTab();
        //if (roots.size() >= 7) {
        //    wGridPanel.add(switcherRight, 498, 35);
        //    wGridPanel.add(switcherLeft, 275, 35);
//
            for (int i = roots.size() -1; i >= 0; i--) {
                int finalI = i;
                wTabPanel.add(roots.get(i), tab -> tab.icon(new TextureIcon(ConfigParser.loadPackIcon(mc.getTextureManager(), rpProfiles.get(finalI)))));
            }
        //}


        return wTabPanel;
    }

}
