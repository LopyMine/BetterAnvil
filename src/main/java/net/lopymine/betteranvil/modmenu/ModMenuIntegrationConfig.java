package net.lopymine.betteranvil.modmenu;

import io.github.cottonmc.cotton.gui.client.LightweightGuiDescription;
import io.github.cottonmc.cotton.gui.impl.client.LibGuiClient;
import io.github.cottonmc.cotton.gui.widget.WGridPanel;
import io.github.cottonmc.cotton.gui.widget.WToggleButton;
import net.lopymine.betteranvil.gui.my.widget.Switcher;
import net.lopymine.betteranvil.gui.my.widget.WSwitcher;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModMenuIntegrationConfig extends LightweightGuiDescription {

    public ModMenuIntegrationConfig(Screen previous){
        WGridPanel root = new WGridPanel();
        root.setSize(300,300);

        WSwitcher switcher = new WSwitcher(Switcher.LEFT);
        root.add(switcher, 4,4);
        WToggleButton darkmodeButton = new WToggleButton(Text.translatable("option.libgui.darkmode")) {
            @Override
            public void onToggle(boolean on) {
                LibGuiClient.config.darkMode = on;
                LibGuiClient.saveConfig(LibGuiClient.config);
            }
        };
        darkmodeButton.setToggle(LibGuiClient.config.darkMode);
        root.add(darkmodeButton, 0, 1, 6, 1);

        setRootPanel(root);
        root.validate(this);
    }
}
