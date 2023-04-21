package net.lopymine.betteranvil.modmenu;

import io.github.cottonmc.cotton.gui.impl.client.LibGuiClient;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.lopymine.betteranvil.modmenu.interfaces.PositionButton;
import net.lopymine.betteranvil.modmenu.interfaces.ResourcePackJsonWriting;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class ModMenuIntegrationConfig {

    public static Screen createScreen(Screen parentScreen) {
        BetterAnvilConfigManager config = BetterAnvilConfigManager.INSTANCE;

        ConfigBuilder configBuilder = ConfigBuilder.create()
                .setParentScreen(parentScreen)
                .setTitle(Text.translatable("gui.betteranvil.modmenu.title"))
                .setSavingRunnable(config::write);

        PositionButton[] psButtons = new PositionButton[2];
        psButtons[0] = PositionButton.RIGHT;
        psButtons[1] = PositionButton.LEFT;

        ConfigCategory category = configBuilder.getOrCreateCategory(Text.of(" "));
        ConfigEntryBuilder entryBuilder = configBuilder.entryBuilder();

        category.addEntry(entryBuilder.startSelector(Text.translatable("gui.betteranvil.modmenu.button.position"), psButtons, config.position)
                .setSaveConsumer(confignew -> config.position = confignew)
                .setTooltip(Text.translatable("gui.betteranvil.modmenu.button.tooltip"))
                .build());

        ResourcePackJsonWriting[] variants = new ResourcePackJsonWriting[3];
        variants[0] = ResourcePackJsonWriting.LAUNCH;
        variants[1] = ResourcePackJsonWriting.BUTTON;
        variants[2] = ResourcePackJsonWriting.OFF;

        category.addEntry(entryBuilder.startSelector(Text.translatable("gui.betteranvil.modmenu.resourcepackwriter.when"), variants, config.start)
                .setSaveConsumer(configneww -> config.start = configneww)
                .setTooltip(Text.translatable("gui.betteranvil.modmenu.resourcepackwriter.tooltip"))
                .build());

        boolean darkMode =  LibGuiClient.loadConfig().darkMode;
        category.addEntry(entryBuilder.startBooleanToggle(Text.translatable("gui.betteranvil.modmenu.darkmode"), darkMode)
                .setSaveConsumer(libguiconfig -> {
                    LibGuiClient.config.darkMode = libguiconfig;
                    LibGuiClient.saveConfig(LibGuiClient.config);
                })
                .setTooltip(Text.translatable("gui.betteranvil.modmenu.darkmode.tooltip"))
                .build());

        return configBuilder.build();
    }
}