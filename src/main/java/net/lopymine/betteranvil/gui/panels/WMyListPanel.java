package net.lopymine.betteranvil.gui.panels;

import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.lopymine.betteranvil.gui.widgets.buttons.WFavoriteButton;
import net.lopymine.betteranvil.gui.widgets.buttons.WRenameButton;

import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.createNinePatch;

public class WMyListPanel extends WPlainPanel {

    public WRenameButton wRenameButton;
    public WFavoriteButton favoriteButton;

    public WMyListPanel() {

        wRenameButton = new WRenameButton();
        this.add(wRenameButton, 1, 0);

        favoriteButton = new WFavoriteButton();
        this.add(favoriteButton, 160, 8);


        this.setSize(200, 300);
    }
}