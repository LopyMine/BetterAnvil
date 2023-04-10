package net.lopymine.betteranvil.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import io.github.cottonmc.cotton.gui.widget.icon.Icon;
import io.github.cottonmc.cotton.gui.widget.icon.TextureIcon;
import net.lopymine.betteranvil.cit.ConfigParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public abstract class AnvilGui extends LightweightGuiDescription {

    protected abstract void renameMethod(String name);

    private static final int maxLength = 15;
    private static final int maxLengthBigLabel = 20;

    private static WLabel empty;
    private static WListPanel<String, MyWListPanel> wListPanel;

    @Override
    public void addPainters() {

    }

    public AnvilGui(Screen parent, ItemStack anvilItem) {
        MinecraftClient mc = MinecraftClient.getInstance();
        WTabPanel wTabPanel = new WTabPanel();


        for (String rp : ConfigParser.getResourcePackNames(mc)) {
            WPlainPanel other = new WPlainPanel();
            other.setSize(200, 400);

            //label
            WLabel label = new WLabel(Text.translatable("gui.betteranvil.menu"));
            label.setSize(6, 5);
            label.setVerticalAlignment(VerticalAlignment.TOP).setHorizontalAlignment(HorizontalAlignment.CENTER);
            other.add(label, 85, 8);

            //closeButton button
            WButton closeButton = new WButton(Text.literal("Х"));
            closeButton.setOnClick(() -> {
                mc.setScreen(parent);
            });
            other.add(closeButton, 170, 5, 25, 5);
//
            WLabel itemName = new WLabel(Text.of(" "));
//
            WButton copyButton = new WButton(Text.translatable("gui.betteranvil.button.copy"));
//
            WSprite bigFieldName = new WSprite(new Identifier("betteranvil", "gui/bigfield.png"));
            other.add(bigFieldName, 5, 305, 200, 50);
//
//
            ArrayList<String> data = new ArrayList<>(ConfigParser.parseItemFromResourcePack(rp, anvilItem));
//
            BiConsumer<String, MyWListPanel> configurator = (String s, MyWListPanel destination) -> {
                destination.label.setText(Text.literal(cutString(s, maxLength)));
//
                ItemStack anvilItemNew = new ItemStack(anvilItem.getItem().asItem());
                anvilItemNew.setCustomName(Text.of(s));
                List<ItemStack> itemStackList = new ArrayList<>();
                itemStackList.add(anvilItemNew);
                destination.item.setItems(itemStackList);
//
                destination.select.setOnClick(() -> {
                    bigFieldName.setImage(new Identifier("betteranvil", "gui/bigfieldfocus.png"));
//
                    itemName.setText(Text.of(cutString(s, maxLengthBigLabel)));
                    other.add(itemName, 98, 321);
//
                    itemName.setHorizontalAlignment(HorizontalAlignment.CENTER);
                    copyButton.setOnClick(() -> {
                        mc.setScreen(parent);
                        this.renameMethod(s);
                    });
                    other.add(copyButton, 18, 340, 175, 50);
                });
            };
//
            createNameList(other,data,configurator);
//
            WTextField textField = new WTextField(Text.translatable("gui.betteranvil.textfield.search"));
            textField.setChangedListener(s -> {
                if(textField.getText().isEmpty()){
                    createNameList(other, data, configurator);
                    return;
                }
                ArrayList<String> dataSearch = new ArrayList<>();
                for(String dt : data){
                    if(dt.toLowerCase().contains(textField.getText().toLowerCase())){
                        dataSearch.add(dt);
                    }
                }
                createNameList(other,dataSearch,configurator);
            });
            other.add(textField, 35,40, 130,10);
//
            wTabPanel.add(other, tab -> tab.title(Text.of(rp)));
        }

        //I don't know what do that...
        setRootPanel(wTabPanel);
        wTabPanel.validate(this);

    }

    private static String cutString(String text, int length) {
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

    private void createNameList(WPlainPanel root, ArrayList<String> data, BiConsumer<String, MyWListPanel> configurator) {
        root.remove(empty);
        root.remove(wListPanel);
        if (data.isEmpty()) {
            empty = new WLabel(Text.translatable("gui.betteranvil.textfield.search.empty"));
            root.add(empty, 90,170);
            empty.setHorizontalAlignment(HorizontalAlignment.CENTER);
            empty.setVerticalAlignment(VerticalAlignment.CENTER);
        } else {
            wListPanel = new WListPanel<>(data, MyWListPanel::new, configurator);
            wListPanel.getScrollBar().setHost(this);
            //183
            root.add(wListPanel, 10, 70, 195, 230);
        }
    }
}
