package net.lopymine.betteranvil.gui;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import io.github.cottonmc.cotton.gui.widget.WLabel;
import io.github.cottonmc.cotton.gui.widget.data.*;

import net.lopymine.betteranvil.gui.description.SimpleGuiDescription;

public class NoClothConfigGui extends SimpleGuiDescription {
    public NoClothConfigGui(Screen parent) {
        super(parent);

        WLabel label = new WLabel(Text.translatable("better_anvil.no_cloth_config.title"), 0xFFFFFFFF)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.CENTER);

        root.add(label, 0, 0, width, height);

        root.validate(this);
    }
}
