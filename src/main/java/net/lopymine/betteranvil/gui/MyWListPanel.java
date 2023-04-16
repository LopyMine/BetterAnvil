package net.lopymine.betteranvil.gui;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;
import io.github.cottonmc.cotton.gui.widget.*;
import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.gui.my.widget.WFavoriteButton;
import net.lopymine.betteranvil.gui.my.widget.WItemButton;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.createLightDarkVariants;
import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.createNinePatch;

public class MyWListPanel extends WPlainPanel {

    public static BackgroundPainter backgroundPainter = createLightDarkVariants(
            createNinePatch(new Identifier(BetterAnvil.MOD_ID, "gui/mygui.png")),
            createNinePatch(new Identifier(BetterAnvil.MOD_ID, "gui/myguidark.png"))
    );

    WItemButton wItemButton;
    WFavoriteButton favoriteButton;

    public MyWListPanel() {

        wItemButton = new WItemButton();
        this.add(wItemButton, 0, 0);

        favoriteButton = new WFavoriteButton();
        this.add(favoriteButton, 160, 8);
        //item = new WItem(new ItemStack(Items.BARRIER));
        //this.add(item, 0, 0, 32, 32);
        this.setSize(200, 300);
    }
}