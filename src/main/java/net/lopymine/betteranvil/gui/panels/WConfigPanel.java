package net.lopymine.betteranvil.gui.panels;

import io.github.cottonmc.cotton.gui.widget.WPlainPanel;

import net.lopymine.betteranvil.gui.widgets.buttons.*;

public class WConfigPanel extends WPlainPanel {

    public WStarButton starButton = new WStarButton();

    public WConfigPanel() {
        this.add(starButton, 160, 8);

        this.setSize(200, 300);
    }

    public void addWRenameButton(WRenameButton renameButton) {
        this.add(renameButton, 1, 0);
    }
}