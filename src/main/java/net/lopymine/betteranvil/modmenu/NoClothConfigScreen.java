package net.lopymine.betteranvil.modmenu;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.Insets;
import io.github.cottonmc.cotton.gui.widget.data.VerticalAlignment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;

public class NoClothConfigScreen extends LightweightGuiDescription {
    public NoClothConfigScreen(Screen screen){
        WPlainPanel root = new WPlainPanel();
        root.setInsets(Insets.ROOT_PANEL);

        MinecraftClient mc = MinecraftClient.getInstance();

        int w = mc.currentScreen.width;
        int h = mc.currentScreen.height;

        root.setSize(w, h);

        WLabel label = new WLabel(Text.translatable("gui.betteranvil.modmenu.noclothconfig"));
        label.setDarkmodeColor(0xFFFFFFFF);
        label.setColor(0xFFFFFFFF);

        label.setHorizontalAlignment(HorizontalAlignment.CENTER);
        label.setVerticalAlignment(VerticalAlignment.CENTER);


        root.add(label,0,0,w,h);

        WButton button = new WButton(ScreenTexts.BACK);
        button.setOnClick(()-> mc.setScreen(screen));

        root.add(button,0,2, 60, 20);
        button.setLocation(w-62,2);

        setRootPanel(root);
    }

    @Override
    public void addPainters() {

    }
}
