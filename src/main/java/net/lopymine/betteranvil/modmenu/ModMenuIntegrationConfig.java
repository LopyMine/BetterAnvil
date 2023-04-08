package net.lopymine.betteranvil.modmenu;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.impl.client.LibGuiClient;
import io.github.cottonmc.cotton.gui.impl.modmenu.WKirbSprite;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Axis;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

public class ModMenuIntegrationConfig extends LightweightGuiDescription {
    public ModMenuIntegrationConfig(Screen previous){
        WGridPanel root = new WGridPanel();
        root.setSize(128,128);
        root.setInsets(Insets.ROOT_PANEL);

        //setRootPanel(root);


        setRootPanel(root);


        WButton doneButton = new WButton(ScreenTexts.BACK);
        doneButton.setOnClick(()->{
            MinecraftClient.getInstance().setScreen(previous);
        });
        root.add(doneButton, 0, 3, 3, 1);

        root.setBackgroundPainter(BackgroundPainter.VANILLA);

        root.validate(this);
    }
}
