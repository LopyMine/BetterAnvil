package net.lopymine.betteranvil.gui;

import io.github.cottonmc.cotton.gui.widget.*;
import net.lopymine.betteranvil.gui.my.widget.WFavoriteButton;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class MyWListPanel extends WPlainPanel {
    WSprite fieldName;

    WSprite slot;

    WLabel label;

    WItem item;

    WButton select;

    WFavoriteButton favoriteButton;
    public MyWListPanel() {
        fieldName = new WSprite(new Identifier("betteranvil", "gui/namefieldfocus.png"));
        this.add(fieldName, 24, 3, 110, 15);

        label = new WLabel(Text.literal("..."));
        this.add(label, 30, 7, 90, 30);

        slot = new WSprite(new Identifier("betteranvil", "gui/slot.png"));
        this.add(slot, 0, 0, 20, 20);

        item = new WItem(new ItemStack(Items.BARRIER));
        this.add(item, 3, 3, 16, 16);

        select = new WButton(Text.translatable("gui.betteranvil.button.select"));
        this.add(select, 138, 0, 20, 20);

        favoriteButton = new WFavoriteButton();
        this.add(favoriteButton, 162, 2);
        //item = new WItem(new ItemStack(Items.BARRIER));
        //this.add(item, 0, 0, 32, 32);

        //this.setSize(718, 218);
    }
}