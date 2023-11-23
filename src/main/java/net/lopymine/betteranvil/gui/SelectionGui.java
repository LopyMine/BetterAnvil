package net.lopymine.betteranvil.gui;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.*;

import net.lopymine.betteranvil.config.resourcepacks.cmd.CMDItem;
import net.lopymine.betteranvil.gui.description.SimpleGuiDescription;
import net.lopymine.betteranvil.gui.screen.BetterAnvilScreen;
import net.lopymine.betteranvil.resourcepacks.ResourcePackType;

import java.util.*;
import org.jetbrains.annotations.Nullable;

public class SelectionGui extends SimpleGuiDescription {
    @Nullable
    private CottonClientScreen currentScreen;

    public SelectionGui(Screen parent, Set<ResourcePackType> types, String resourcePack) {
        super(parent);

        if (types.isEmpty()) {
            return;
        }

        int widgetsWidth = (types.size() * 30) + (10 * (types.size() - 1));
        int x = calcPos(width, widgetsWidth);
        int y = calcPos(height, 20);

        for (ResourcePackType type : types) {
            WButton button = new WButton(Text.of(type.name())) {
                @Override
                public void addTooltip(TooltipBuilder tooltip) {
                    tooltip.add(Text.of(type.getFullName()));
                }
            }.setOnClick(() -> tryOpenScreenByType(type, this.currentScreen, resourcePack));

            root.add(button, x, y, 30, 20);
            x = x + 40;
        }

        WLabel label = new WLabel(Text.translatable("better_anvil.selection_gui.title"), 0xFFFFFFFF)
                .setHorizontalAlignment(HorizontalAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.CENTER);

        int labelX = calcPos(width, 100);
        int labelY = calcPos(height, 20) - 30;

        root.add(label, labelX, labelY, 100, 20);

        root.validate(this);
    }

    private int calcPos(int i, int d) {
        return (i - d) / 2;
    }

    public static void tryOpenScreenByType(ResourcePackType type, @Nullable Screen parent, String resourcePack) {
        MinecraftClient client = MinecraftClient.getInstance();

        Screen screen = switch (type) {
            case CIT -> new BetterAnvilScreen(new ResourcePackRenamesGui(parent, new ArrayList<>(List.of(resourcePack))));
            case CMD -> new BetterAnvilScreen(new CustomModelDataItemsGui(parent, true, new ArrayList<>(List.of(resourcePack))) {
                @Override
                protected void setCommand(CMDItem item) {
                }
            });
            case CEM -> null;
        };

        client.setScreen(screen);
    }

    public void setCurrentScreen(@Nullable CottonClientScreen currentScreen) {
        this.currentScreen = currentScreen;
    }
}
