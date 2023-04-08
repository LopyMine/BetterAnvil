package net.lopymine.betteranvil.gui;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.lopymine.betteranvil.cit.ConfigParser;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;

public abstract class AnvilGui extends LightweightGuiDescription {

    protected abstract void rename(String name);

    private static final int maxLength = 15;
    private static final int maxLengthBigLabel = 18;

    public AnvilGui(Screen parent, ItemStack anvilItem) {
        //gui settings
        WGridPanel root = new WGridPanel();
        setRootPanel(root);

        MinecraftClient mc = MinecraftClient.getInstance();

        root.setSize(180, 360);
        root.setInsets(Insets.ROOT_PANEL);

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
        root.add(closeButton, 8, 0, 2, 2);

        //Big item slot
        //WSprite bigItemSlot = new WSprite(new Identifier("betteranvil", "gui/big_item_slot.png"));
        //root.add(bigItemSlot, 12, 3, 6, 6);

        //Barrier Sprite
        //WSprite barrier = new WSprite(new Identifier("minecraft:textures/item/barrier.png"));
        //root.add(barrier, 12, 3, 6, 6);

        //Big name field
        //WSprite bigFieldName = new WSprite(new Identifier("betteranvil", "gui/bigfield.png"));
        //root.add(bigFieldName, 10, 9, 11, 3);

        //label item name
        //WLabel itemName = new WLabel(Text.of(" "));
        //itemName.setSize(6, 5);
        //root.add(itemName, 11, 10);

        //delete

        //data.add("AAAAAAAAAAAAAAAAAAAAAAAA");
        //data.add("2331");
        ArrayList<String> data = new ArrayList<>(ConfigParser.parseItemNames(anvilItem));
        //data.add("тест");
        //data.add("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        //for (int i = 0; i < anvilItem.getCount(); i++) {
        //        data.add(String.valueOf(i));
        //}

        //Big name field
        WSprite bigFieldName = new WSprite(new Identifier("betteranvil", "gui/bigfield1.png"));

        //label item name
        WLabel itemName = new WLabel(Text.of(" "));
        itemName.setSize(6, 5);


        //delete

        WButton copyButton = new WButton(Text.translatable("gui.betteranvil.button.copy"));

        BiConsumer<String, MyWListPanel> configurator = (String s, MyWListPanel destination) -> {
            destination.label.setText(Text.literal(cutString(s, maxLength)));

            ItemStack anvilItemNew = new ItemStack(anvilItem.getItem().asItem());
            anvilItemNew.setCustomName(Text.of(s));

            List<ItemStack> itemStackList = new ArrayList<>();
            itemStackList.add(anvilItemNew);

            destination.item.setItems(itemStackList);


            destination.select.setOnClick(() -> {
                itemName.setText(Text.of(cutString(s, maxLengthBigLabel)));
                root.add(bigFieldName, 0, 16, 10, 3);
                root.add(itemName, 4, 17);
                itemName.setHorizontalAlignment(HorizontalAlignment.CENTER);
                //System.out.println(anvilItemNew.getName().toString());
                //System.out.println(s);
                //System.out.println(cutString(s, maxLength));
////
                copyButton.setOnClick(() -> {
                    mc.setScreen(parent);
                    this.rename(s);
                });
//
                root.add(copyButton, 2, 18, 6, 1);
            });
        };

        WListPanel<String, MyWListPanel> list = new WListPanel<>(data, MyWListPanel::new, configurator);

        WLabel empty = new WLabel(Text.translatable("gui.betteranvil.gui.label.empty"));

        if(data.isEmpty()){
            root.add(empty, 3, 8,7,2);
            empty.setSize(7,2);
        } else {
            root.add(list, 0, 2, 10, 14);
        }


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
}
