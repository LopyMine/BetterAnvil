package net.lopymine.betteranvil.modmenu;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.impl.client.LibGuiClient;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WToggleButton;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.lopymine.betteranvil.gui.my.widget.WFavoriteButton;
import net.minecraft.client.gui.screen.Screen;

public class ModMenuIntegrationConfig extends LightweightGuiDescription {
    public ModMenuIntegrationConfig(Screen previous){
        WGridPanel root = new WGridPanel();
        root.setSize(300,300);
        root.setInsets(Insets.ROOT_PANEL);
        setRootPanel(root);

        WFavoriteButton favoriteButton = new WFavoriteButton();
        root.add(favoriteButton, 3,3);

        WFavoriteButton wfavoriteButton = new WFavoriteButton();
        root.add(wfavoriteButton, 5,3);

        WToggleButton wToggleButton = new WToggleButton();
        wToggleButton.setOnToggle(on ->{
            LibGuiClient.config.darkMode = on;
            LibGuiClient.saveConfig(LibGuiClient.config);
        });

        root.add(wToggleButton, 4,4);

        //WGridPanel gridLeft = new WGridPanel();
        //WGridPanel gridRight = new WGridPanel();
//
// add s//ome widgets to the left and right grids
        //gridLeft.add(new WLabel(Text.literal("foo")), 0, 0);
        //gridRight.add(new WLabel(Text.literal("bar")), 10, 1);
        //gridRight.add(new WButton(), 2,5);
//
// add g//rids to boxes
        //root.add(gridLeft, gridLeft.getWidth(), gridLeft.getHeight()); // provide manual size so that it doesn't get resized
        //root.add(gridRight, gridRight.getWidth(), gridRight.getHeight());
        //setRootPanel(root);
        root.validate(this);
    }
}
