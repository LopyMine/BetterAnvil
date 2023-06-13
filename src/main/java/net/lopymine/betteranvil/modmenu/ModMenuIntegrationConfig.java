package net.lopymine.betteranvil.modmenu;

import io.github.cottonmc.cotton.gui.impl.client.LibGuiClient;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.lopymine.betteranvil.modmenu.enums.CITButtonTexture;
import net.lopymine.betteranvil.modmenu.enums.PositionButton;
import net.lopymine.betteranvil.modmenu.enums.ResourcePackJsonWriting;
import net.lopymine.betteranvil.modmenu.enums.ResourcePackParserVersion;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
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

        ConfigCategory category = configBuilder.getOrCreateCategory(Text.translatable("gui.betteranvil.modmenu.title"));
        ConfigEntryBuilder entryBuilder = configBuilder.entryBuilder();

        category.addEntry(entryBuilder.startSelector(Text.translatable("gui.betteranvil.modmenu.button.position"), psButtons, config.POSITION)
                  .setDefaultValue(PositionButton.RIGHT)
              .setSaveConsumer(confignew -> config.POSITION = confignew)
           .setTooltip(Text.translatable("gui.betteranvil.modmenu.button.tooltip"))
          .build());

        ResourcePackJsonWriting[] variants = new ResourcePackJsonWriting[3];
        variants[0] = ResourcePackJsonWriting.LAUNCH;
        variants[1] = ResourcePackJsonWriting.BUTTON;
        variants[2] = ResourcePackJsonWriting.OFF;

        category.addEntry(entryBuilder.startSelector(Text.translatable("gui.betteranvil.modmenu.jsonwriter"), variants, config.START)
              //                        .setDefaultValue(ResourcePackJsonWriting.LAUNCH)
           .setSaveConsumer(configneww -> config.START = configneww)
              .setTooltip(Text.translatable("gui.betteranvil.modmenu.jsonwriter.tooltip"))
            .build());

        ResourcePackParserVersion[] versions = new ResourcePackParserVersion[2];
        versions[0] = ResourcePackParserVersion.V1;
        versions[1] = ResourcePackParserVersion.V2;
        category.addEntry(entryBuilder.startSelector(Text.translatable("gui.betteranvil.modmenu.parsersystem"), versions, config.PARSER_VERSION)
              .setDefaultValue(ResourcePackParserVersion.V2)
             .setTooltip(Text.translatable("gui.betteranvil.modmenu.parsersystem.tooltip"))
          .setSaveConsumer(resourcePackParserVersion -> config.PARSER_VERSION = resourcePackParserVersion)
               .build());

        category.addEntry(entryBuilder.startBooleanToggle(Text.translatable("gui.betteranvil.modmenu.cmdsupport"), config.CUSTOM_MODEL_DATA_SUPPORT)
            .setDefaultValue(true)
            .setSaveConsumer(b -> config.CUSTOM_MODEL_DATA_SUPPORT = b)
          .setTooltip(Text.translatable("gui.betteranvil.modmenu.cmdsupport.tooltip"))
              .build());

        ConfigCategory gui = configBuilder.getOrCreateCategory(Text.translatable("gui.betteranvil.modmenu.gui.title"));

        CITButtonTexture[] textures = new CITButtonTexture[4];
        textures[0] = CITButtonTexture.THEME;
        textures[1] = CITButtonTexture.RENAME;
        textures[2] = CITButtonTexture.RENAME_WHITE_THEME;
        textures[3] = CITButtonTexture.RENAME_DARK_THEME;

        gui.addEntry(entryBuilder.startSelector(Text.translatable(  "gui.betteranvil.modmenu.gui.button.texture"), textures, config.BUTTON_TEXTURE)
              .setDefaultValue(CITButtonTexture.THEME)
             .setSaveConsumer(citButtonTexture -> config.BUTTON_TEXTURE = citButtonTexture)
              .build());

        gui.addEntry(entryBuilder.startIntSlider(Text.translatable("gui.betteranvil.modmenu.gui.button.height"), config.BUTTON_HEIGHT, 20, 40)
               .setDefaultValue(30)

                .setSaveConsumer(integer -> config.BUTTON_HEIGHT = integer)
                .setTooltip(Text.translatable("gui.betteranvil.modmenu.gui.button.height.tooltip"))
              .build());

        boolean darkMode =  LibGuiClient.loadConfig().darkMode;
        gui.addEntry(entryBuilder.startBooleanToggle(Text.translatable("gui.betteranvil.modmenu.darkmode"), darkMode)

               .setDefaultValue(false)
               .setSaveConsumer(libguiconfig -> {
               LibGuiClient.config.darkMode = libguiconfig;
                  LibGuiClient.saveConfig(LibGuiClient.config);
              })
               .setTooltip(Text.translatable("gui.betteranvil.modmenu.darkmode.tooltip"))
               .build());

        ConfigCategory keyBindings = configBuilder.getOrCreateCategory(Text.translatable("gui.betteranvil.modmenu.keybinding.title"));

        KeyBinding shift = new KeyBinding("gui.betteranvil.keybinding.shift", InputUtil.Type.KEYSYM, 340, "gui.betteranvil.keybinding.shift.tooltip");
        InputUtil.Key shift_key = KeyBindingHelper.getBoundKeyOf(shift);
        keyBindings.addEntry(entryBuilder.startKeyCodeField(Text.translatable("gui.betteranvil.setting.keybinding.shift"), shift_key)
                .setDefaultValue(shift_key)
                .setKeySaveConsumer(key -> config.SHIFT_KEY = key.getCode())
                .build());

        KeyBinding ctrl = new KeyBinding("gui.betteranvil.keybinding.ctrl", InputUtil.Type.KEYSYM, 341, "gui.betteranvil.keybinding.ctrl.tooltip");
        InputUtil.Key ctrl_key = KeyBindingHelper.getBoundKeyOf(ctrl);
        keyBindings.addEntry(entryBuilder.startKeyCodeField(Text.translatable("gui.betteranvil.setting.keybinding.ctrl"), ctrl_key)
                .setDefaultValue(ctrl_key)
                .setKeySaveConsumer(key -> config.CTRL_KEY = key.getCode())
                .build());



        return configBuilder.build();
    }
}
