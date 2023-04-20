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
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class AnvilGuiDescription extends LightweightGuiDescription {
    @Override
    public void addPainters() {}
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

    public AnvilGuiDescription(Screen parent, ItemStack anvilItem) {
        WPlainPanel root = new WPlainPanel();
        root.setInsets(Insets.ROOT_PANEL);
        root.setSize(800, 500);

        ArrayList<WButton> buttons = new ArrayList<>();

        WSprite bigFieldName = new WSprite(bfnTexture);
        MinecraftClient mc = MinecraftClient.getInstance();
        WLabel itemName = new WLabel(Text.of(" "));
        WItemSlot anvilSlot = new WItemSlot(new PlayerInventory(mc.player), 1, 1, 1, true);
        anvilSlot.setIcon(new ItemIcon(anvilItem));

        WPlainPanel favorite = new WPlainPanel();
        favorite.setSize(204, 230);
        favorite.setHost(this);
        favorite.add(new WLabel(Text.translatable("gui.betteranvil.tooltip.textfield")).setHorizontalAlignment(HorizontalAlignment.CENTER), 95, 15);

        panel = new WPlainPanel();
        panel.setBackgroundPainter(BackgroundPainter.VANILLA);
        dataD = new ArrayList<>(ConfigParser.parseAllItemNames(anvilItem));

        dataFAll = new ArrayList<>(FavoriteWriter.readConfig().getCitItemsCollection());
        dataF = new ArrayList<>(FavoriteWriter.getWithItem(dataFAll, anvilItem));
        configuratorF = (CitItems s, MyWListPanel destination) -> {

            destination.favoriteButton.setToggle(true);

            destination.wItemButton.setToolTip(Text.of(s.getCustomName()));

            ItemStack anvilItemNew = new ItemStack(anvilItem.getItem().asItem());
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

        WTextField textFieldF = new WTextField(Text.translatable("gui.betteranvil.textfield.search"));
        textFieldF.setHost(favorite.getHost());
        textFieldF.setChangedListener(s -> {
            if (textFieldF.getText().isEmpty()) {
                createFavoriteNameList(favorite, dataF);
                return;
            }
            createFavoriteNameList(favorite, getSearchData(textFieldF));
        });

        favorite.add(textFieldF, 45, 40, 110, 10);

        createFavoriteNameList(favorite, dataF);


        WFavoriteButton wFavoriteButton = new WFavoriteButton();
        wFavoriteButton.setOnToggle(on -> {
            favoriteWindowOn = on;

            createFavoriteNameList(favorite, dataF);

            if (favoriteWindowOn) {
                root.add(favorite, 84, 53);
                favorite.setBackgroundPainter(BackgroundPainter.VANILLA);
                //favorite.setBackgroundPainter(MyWListPanel.backgroundPainter);
                favorite.layout();
                favorite.setSize(205, 230);
            }
            if (!favoriteWindowOn) {
                root.remove(favorite);
            }

        });


        WLabel labelD = new WLabel(Text.translatable("gui.betteranvil.menu"));
        labelD.setSize(6, 5);
        labelD.setVerticalAlignment(VerticalAlignment.TOP).setHorizontalAlignment(HorizontalAlignment.CENTER);
        panel.add(labelD, 85, 15);

        panel.add(wFavoriteButton, 10, 10);

        WButton copyButton = new WButton(Text.translatable("gui.betteranvil.button.copy"));
        buttons.add(copyButton);
        panel.add(copyButton, 14, 347, 175, 50);
        WButton closeButtonD = new WButton(Text.literal("х"));
        closeButtonD.setOnClick(() -> {
                  mc.setScreen(parent);
              });
        panel.add(closeButtonD, 172, 5, 25, 5);
        panel.add(bigFieldName, 18, 320, 165, 24);

        panel.add(itemName, 93, 329);
        panel.add(anvilSlot, 90, 375);


        configuratorD = (CitItems s, MyWListPanel destination) -> {
            destination.wItemButton.setText(Text.literal(cutString(s.getCustomName(), maxLength)));

            destination.wItemButton.setToolTip(Text.of(s.getCustomName()));

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
                } else {
                    FavoriteWriter.removeItem(dataFAll, s);
                    dataF.remove(s);
                }
                createFavoriteNameList(favorite, dataF);
            });
        };

        createAllNameList(panel, dataD);

        WTextField wTextFieldD = new WTextField(Text.translatable("gui.betteranvil.textfield.search"));
        wTextFieldD.setChangedListener(s -> {
                  if (wTextFieldD.getText().isEmpty()) {
                              createAllNameList(panel, dataD);
                              return;
                          }
                  createAllNameList(panel, getSearchData(wTextFieldD));
              });
        panel.add(wTextFieldD, 35, 40, 130, 10);


        root.add(panel, 293, 53, 203, 405);

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

    private ArrayList<CitItems> getSearchData(WTextField wTextField) {
        ArrayList<CitItems> dataSearch = new ArrayList<>();
        for (CitItems dt : dataD) {
            if (dt.getCustomName().toLowerCase().replace("ё", "е").contains(wTextField.getText().toLowerCase().replace("ё", "е"))) {
                dataSearch.add(dt);
            }
        }
        return dataSearch;
    }

    public void createFavoriteNameList(WPlainPanel root, ArrayList<CitItems> data) {
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

    public void createAllNameList(WPlainPanel root, ArrayList<CitItems> data) {
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

    private Identifier getDarkOrWhiteTexture(Identifier dark, Identifier white) {
        return LibGui.isDarkMode() ? dark : white;
    }
}
