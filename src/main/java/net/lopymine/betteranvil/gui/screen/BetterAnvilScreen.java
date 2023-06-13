package net.lopymine.betteranvil.gui.screen;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.lopymine.betteranvil.gui.widgets.buttons.WItemButton;

public class BetterAnvilScreen extends CottonClientScreen {
    public BetterAnvilScreen(GuiDescription description) {
        super(description);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        int containerX = (int)mouseX-left;
        int containerY = (int)mouseY-top;

        WWidget hit = description.getRootPanel().hit(containerX, containerY);
        if(hit.getClass() == WItemButton.class){
            description.requestFocus(hit);
        }

        super.mouseMoved(mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        this.mouseMoved(mouseX,mouseY);
        return super.mouseScrolled(mouseX, mouseY, amount);
    }
}
