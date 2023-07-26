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
import org.lwjgl.glfw.GLFW;

import java.util.Arrays;
import java.util.Optional;

public class ModMenuIntegrationScreen {

   public static Screen createScreen(Screen parentScreen) {
        BetterAnvilConfigManager config = BetterAnvilConfigManager.INSTANCE;

        ConfigBuilder configBuilder = ConfigBuilder.create()
              .setParentScreen(parentScreen)
              .setTitle(Text.translatable("better_anvil.mod_menu.title"))
               .setSavingRunnable(config::write);


        PositionButton[] psButtons = new PositionButton[2];
        psButtons[0] = PositionButton.RIGHT;
        psButtons[1] = PositionButton.LEFT;

        ConfigCategory category = configBuilder.getOrCreateCategory(Text.translatable("better_anvil.mod_menu.title"));
        ConfigEntryBuilder entryBuilder = configBuilder.entryBuilder();

        category.addEntry(entryBuilder.startSelector(Text.translatable("better_anvil.mod_menu.menu_buttons.location"), psButtons, config.POSITION)
           .setDefaultValue(PositionButton.RIGHT)
           .setSaveConsumer(pos -> config.POSITION = pos)
           .setTooltip(Text.translatable("better_anvil.mod_menu.menu_buttons.tooltip"))
           .setNameProvider(PositionButton::getText)
           .build());

        ResourcePackJsonWriting[] variants = new ResourcePackJsonWriting[3];
        variants[0] = ResourcePackJsonWriting.LAUNCH;
        variants[1] = ResourcePackJsonWriting.CHECKBOX;
        variants[2] = ResourcePackJsonWriting.OFF;

        category.addEntry(entryBuilder.startSelector(Text.translatable("better_anvil.mod_menu.rename_writer"), variants, config.WHEN_OVERWRITE)
           .setDefaultValue(ResourcePackJsonWriting.LAUNCH)
           .setSaveConsumer(when -> config.WHEN_OVERWRITE = when)
           .setTooltip(Text.translatable("better_anvil.mod_menu.rename_writer.tooltip"))
           .setNameProvider(ResourcePackJsonWriting::getText)
           .setTooltipSupplier(when -> {
                    Text[] texts = new Text[1];
                    switch (when){
                        case LAUNCH -> texts[0] = Text.translatable("better_anvil.mod_menu.rename_writer.launch.tooltip");
                        case OFF -> texts[0] = Text.translatable("better_anvil.mod_menu.rename_writer.off.tooltip");
                        case CHECKBOX -> texts[0] = Text.translatable("better_anvil.mod_menu.rename_writer.checkbox.tooltip");
                    }
                    return Optional.of(texts);
                })
           .build());

        ResourcePackParserVersion[] versions = new ResourcePackParserVersion[2];
        versions[0] = ResourcePackParserVersion.V1;
        versions[1] = ResourcePackParserVersion.V2;

        category.addEntry(entryBuilder.startSelector(Text.translatable("better_anvil.mod_menu.parser"), versions, config.PARSER_VERSION)
          .setDefaultValue(ResourcePackParserVersion.V2)
          .setTooltip(Text.translatable("better_anvil.mod_menu.parser.tooltip"))
          .setSaveConsumer(resourcePackParserVersion -> config.PARSER_VERSION = resourcePackParserVersion)
          .build());

        category.addEntry(entryBuilder.startBooleanToggle(Text.translatable("better_anvil.mod_menu.cmd_support"), config.CUSTOM_MODEL_DATA_SUPPORT)
          .setDefaultValue(true)
          .setSaveConsumer(support -> config.CUSTOM_MODEL_DATA_SUPPORT = support)
          .setTooltip(Text.translatable("better_anvil.mod_menu.cmd_support.tooltip"))
          .build());

        ConfigCategory gui = configBuilder.getOrCreateCategory(Text.translatable( "better_anvil.mod_menu.gui.title"));

        CITButtonTexture[] textures = new CITButtonTexture[2];
        textures[0] = CITButtonTexture.THEME;
        textures[1] = CITButtonTexture.RENAME;

        gui.addEntry(entryBuilder.startSelector(Text.translatable("better_anvil.mod_menu.gui.rename_button_texture"), textures, config.BUTTON_TEXTURE)
           .setDefaultValue(CITButtonTexture.RENAME)
           .setNameProvider(CITButtonTexture::getText)
           .setSaveConsumer(citButtonTexture -> config.BUTTON_TEXTURE = citButtonTexture)
           .build());

        gui.addEntry(entryBuilder.startIntSlider(Text.translatable("better_anvil.mod_menu.gui.rename_button_spacing"), config.SPACING, 20, 40)
           .setDefaultValue(30)
           .setSaveConsumer(spacing -> config.SPACING = spacing)
           .setTooltip(Text.translatable("better_anvil.mod_menu.gui.rename_button_spacing.tooltip"))
           .build());

        boolean darkMode =  LibGuiClient.loadConfig().darkMode;

        gui.addEntry(entryBuilder.startBooleanToggle(Text.translatable("better_anvil.mod_menu.dark_mode"), darkMode)
           .setDefaultValue(false)
           .setSaveConsumer(libguiconfig -> {
           LibGuiClient.config.darkMode = libguiconfig;
              LibGuiClient.saveConfig(LibGuiClient.config);
           })
           .setTooltip(Text.translatable("better_anvil.mod_menu.dark_mode.tooltip"))
           .build());

        ConfigCategory keyBindings = configBuilder.getOrCreateCategory(Text.translatable("better_anvil.mod_menu.keybinding.title"));

        KeyBinding shift = new KeyBinding("better_anvil.mod_menu.keybinding.shift", InputUtil.Type.KEYSYM, config.SHIFT_KEY, "better_anvil.mod_menu.keybinding.shift");
        InputUtil.Key shift_key = KeyBindingHelper.getBoundKeyOf(shift);
        keyBindings.addEntry(entryBuilder.startKeyCodeField(Text.translatable("better_anvil.mod_menu.keybinding.shift"), shift_key)
            .setDefaultValue(new KeyBinding("better_anvil.mod_menu.keybinding.shift", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_SHIFT, "better_anvil.mod_menu.keybinding.shift").getDefaultKey())
            .setKeySaveConsumer(key -> config.SHIFT_KEY = key.getCode())
            .build());

        KeyBinding ctrl = new KeyBinding("better_anvil.mod_menu.keybinding.ctrl", InputUtil.Type.KEYSYM, config.CTRL_KEY, "better_anvil.mod_menu.keybinding.ctrl");
        InputUtil.Key ctrl_key = KeyBindingHelper.getBoundKeyOf(ctrl);
        keyBindings.addEntry(entryBuilder.startKeyCodeField(Text.translatable("better_anvil.mod_menu.keybinding.ctrl"), ctrl_key)
            .setDefaultValue(new KeyBinding("better_anvil.mod_menu.keybinding.ctrl", InputUtil.Type.KEYSYM, GLFW.GLFW_KEY_LEFT_CONTROL, "better_anvil.mod_menu.keybinding.ctrl").getDefaultKey())
            .setKeySaveConsumer(key -> config.CTRL_KEY = key.getCode())
            .build());



        return configBuilder.build();
    }
}
