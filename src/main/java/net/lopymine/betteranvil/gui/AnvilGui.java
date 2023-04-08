package net.lopymine.betteranvil.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.lopymine.betteranvil.cit.ConfigParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.narration.NarrationMessageBuilder;
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
    private static final int maxLengthBigLabel = 18;

    private static WLabel empty;
    private static WListPanel<String, MyWListPanel> wListPanel;

    public AnvilGui(Screen parent, ItemStack anvilItem) {
        //gui settings
        WGridPanel root = new WGridPanel();
        root.setSize(160, 400);
        root.setInsets(Insets.ROOT_PANEL);
        setRootPanel(root);

        MinecraftClient mc = MinecraftClient.getInstance();

        //label
        WLabel label = new WLabel(Text.translatable("gui.betteranvil.menu"));
        label.setSize(6, 5);
        label.setVerticalAlignment(VerticalAlignment.TOP).setHorizontalAlignment(HorizontalAlignment.CENTER);
        root.add(label, 4, 0);

        //closeButton button
        WButton closeButton = new WButton(Text.literal("Х"));
        closeButton.setOnClick(() -> {
            mc.setScreen(parent);
        });
        root.add(closeButton, 8, 0, 2, 1);

        WLabel itemName = new WLabel(Text.of(" "));

        WButton copyButton = new WButton(Text.translatable("gui.betteranvil.button.copy"));

        WSprite bigFieldName = new WSprite(new Identifier("betteranvil", "gui/bigfield.png"));
        root.add(bigFieldName, 0, 18, 10, 3);


        ArrayList<String> data = new ArrayList<>(ConfigParser.parseItemNames(anvilItem));

        BiConsumer<String, MyWListPanel> configurator = (String s, MyWListPanel destination) -> {
            destination.label.setText(Text.literal(cutString(s, maxLength)));

            ItemStack anvilItemNew = new ItemStack(anvilItem.getItem().asItem());
            anvilItemNew.setCustomName(Text.of(s));
            List<ItemStack> itemStackList = new ArrayList<>();
            itemStackList.add(anvilItemNew);
            destination.item.setItems(itemStackList);

            destination.select.setOnClick(() -> {
                bigFieldName.setImage(new Identifier("betteranvil", "gui/bigfieldfocus.png"));

                itemName.setText(Text.of(cutString(s, maxLengthBigLabel)));
                root.add(itemName, 4, 19);

                itemName.setHorizontalAlignment(HorizontalAlignment.CENTER);
                copyButton.setOnClick(() -> {
                    mc.setScreen(parent);
                    this.renameMethod(s);
                });
                root.add(copyButton, 2, 20, 6, 1);
            });
        };

        createNameList(root,data,configurator);

        WTextField textField = new WTextField(Text.translatable("gui.betteranvil.textfield.search"));
        textField.setChangedListener(s -> {
            if(textField.getText().isEmpty()){
                createNameList(root, data, configurator);
                return;
            }
            ArrayList<String> dataSearch = new ArrayList<>();
            for(String dt : data){
                if(dt.toLowerCase().contains(textField.getText().toLowerCase())){
                    dataSearch.add(dt);
                }
            }
            createNameList(root,dataSearch,configurator);
        });
        root.add(textField, 1,2, 7,1);

        //I don't know what do that...
        root.validate(this);

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

    private void createNameList(WGridPanel root, ArrayList<String> data, BiConsumer<String, MyWListPanel> configurator){
        root.remove(empty);
        root.remove(wListPanel);
        if(data.isEmpty()){
            empty = new WLabel(Text.translatable("gui.betteranvil.textfield.search.empty"));
            root.add(empty, 2, 8);
        } else {
            wListPanel = new WListPanel<>(data, MyWListPanel::new, configurator);
            wListPanel.getScrollBar().setHost(this);
            root.add(wListPanel, 0, 4, 10, 14);
        }
    }
}
