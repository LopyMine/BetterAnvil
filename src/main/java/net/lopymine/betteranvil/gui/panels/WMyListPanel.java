package net.lopymine.betteranvil.gui.panels;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.gui.widgets.buttons.WFavoriteButton;
import net.lopymine.betteranvil.gui.widgets.buttons.WItemButton;
import net.minecraft.util.Identifier;

import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.createLightDarkVariants;
import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.createNinePatch;

public class WMyListPanel extends WPlainPanel {

    public static BackgroundPainter backgroundPainter = createLightDarkVariants(
            createNinePatch(new Identifier(BetterAnvil.MOD_ID, "gui/mygui.png")),
            createNinePatch(new Identifier(BetterAnvil.MOD_ID, "gui/myguidark.png"))
    );

    public WItemButton wItemButton;
    public WFavoriteButton favoriteButton;

    public WMyListPanel() {

        wItemButton = new WItemButton();
        this.add(wItemButton, 1, 0);

        favoriteButton = new WFavoriteButton();
        this.add(favoriteButton, 160, 8);


        this.setSize(200, 300);
    }
}