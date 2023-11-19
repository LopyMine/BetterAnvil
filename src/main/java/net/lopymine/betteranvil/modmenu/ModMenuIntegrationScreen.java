package net.lopymine.betteranvil.modmenu;

import me.shedaniel.clothconfig2.api.*;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import net.lopymine.betteranvil.config.BetterAnvilConfig;
import net.lopymine.betteranvil.config.enums.*;

public class ModMenuIntegrationScreen {

    public static Screen createScreen(Screen parentScreen) {
        BetterAnvilConfig config = BetterAnvilConfig.getInstance();
        BetterAnvilConfig configDefault = new BetterAnvilConfig();

        ConfigBuilder configBuilder = ConfigBuilder.create()
                .setParentScreen(parentScreen)
                .setTitle(Text.translatable("better_anvil.mod_menu.title"))
                .setSavingRunnable(config::write);

        ConfigCategory category = configBuilder.getOrCreateCategory(Text.translatable("better_anvil.mod_menu.title"));
        ConfigEntryBuilder entryBuilder = configBuilder.entryBuilder();

        category.addEntry(entryBuilder.startSelector(Text.translatable("better_anvil.mod_menu.overwriting"), Overwriting.values(), config.overwritingEnum)
                .setDefaultValue(configDefault.overwritingEnum)
                .setSaveConsumer(overwriting -> config.overwritingEnum = overwriting)
                .setTooltip(Text.translatable("better_anvil.mod_menu.overwriting.tooltip"))
                .setNameProvider(Overwriting::getText)
                .build());

        category.addEntry(entryBuilder.startBooleanToggle(Text.translatable("better_anvil.mod_menu.view_resource_packs"), config.enabledViewResourcePacks)
                .setDefaultValue(configDefault.enabledViewResourcePacks)
                .setSaveConsumer(enableViewResourcePacks -> config.enabledViewResourcePacks = enableViewResourcePacks)
                .setTooltip(Text.translatable("better_anvil.mod_menu.view_resource_packs.tooltip"))
                .build());

        category.addEntry(entryBuilder.startBooleanToggle(Text.translatable("better_anvil.mod_menu.cmd_support"), config.enabledCMD)
                .setDefaultValue(configDefault.enabledCMD)
                .setSaveConsumer(bl -> config.enabledCMD = bl)
                .setTooltip(Text.translatable("better_anvil.mod_menu.cmd_support.tooltip"))
                .build());

        category.addEntry(entryBuilder.startBooleanToggle(Text.translatable("better_anvil.mod_menu.cit_support"), config.enabledCIT)
                .setDefaultValue(configDefault.enabledCIT)
                .setSaveConsumer(bl -> config.enabledCIT = bl)
                .setTooltip(Text.translatable("better_anvil.mod_menu.cit_support.tooltip"))
                .build());

        SubCategoryBuilder gui = entryBuilder.startSubCategory(Text.translatable("better_anvil.mod_menu.gui.title"));

        gui.add(0, entryBuilder.startSelector(Text.translatable("better_anvil.mod_menu.menu_buttons.location"), ButtonPositions.values(), config.positionEnum)
                .setDefaultValue(configDefault.positionEnum)
                .setSaveConsumer(pos -> config.positionEnum = pos)
                .setTooltip(Text.translatable("better_anvil.mod_menu.menu_buttons.tooltip"))
                .setNameProvider(ButtonPositions::getText)
                .build());

        gui.add(1, entryBuilder.startSelector(Text.translatable("better_anvil.mod_menu.gui.rename_button_texture"), ButtonTextures.values(), config.buttonTextureEnum)
                .setDefaultValue(configDefault.buttonTextureEnum)
                .setNameProvider(ButtonTextures::getText)
                .setSaveConsumer(citButtonTexture -> config.buttonTextureEnum = citButtonTexture)
                .build());

        gui.add(2, entryBuilder.startIntSlider(Text.translatable("better_anvil.mod_menu.gui.rename_button_spacing"), config.spacing, 20, 40)
                .setDefaultValue(configDefault.spacing)
                .setSaveConsumer(spacing -> config.spacing = spacing)
                .setTooltip(Text.translatable("better_anvil.mod_menu.gui.rename_button_spacing.tooltip"))
                .build());

        gui.add(3, entryBuilder.startBooleanToggle(Text.translatable("better_anvil.mod_menu.gui.dark_mode"), config.isDarkMode)
                .setDefaultValue(configDefault.isDarkMode)
                .setSaveConsumer(isDarkMode -> config.isDarkMode = isDarkMode)
                .setTooltip(Text.translatable("better_anvil.mod_menu.gui.dark_mode.tooltip"))
                .build());

        gui.add(4, entryBuilder.startSelector(Text.translatable("better_anvil.mod_menu.gui.favorite_menu.position"), FavoriteMenuPositions.values(), config.favoriteMenuPositionEnum)
                .setDefaultValue(configDefault.favoriteMenuPositionEnum)
                .setNameProvider(FavoriteMenuPositions::getText)
                .setSaveConsumer(favoriteMenuEnum -> config.favoriteMenuPositionEnum = favoriteMenuEnum)
                .build());

        category.addEntry(gui.build());

        SubCategoryBuilder menuCIT = entryBuilder.startSubCategory(Text.translatable("better_anvil.cit_menu.title"));

        menuCIT.add(0, entryBuilder.startSelector(Text.translatable("better_anvil.mod_menu.cit_menu.renames_enum"), Renames.values(), config.renamesCountEnum)
                .setDefaultValue(configDefault.renamesCountEnum)
                .setSaveConsumer(renames -> config.renamesCountEnum = renames)
                .setTooltip(Text.translatable("better_anvil.mod_menu.cit_menu.renames_enum.tooltip"))
                .setNameProvider(Renames::getText)
                .build());

        menuCIT.add(1, entryBuilder.startIntSlider(Text.translatable("better_anvil.mod_menu.cit_menu.renames_integer"), config.customRenamesCount, 1, 100)
                .setDefaultValue(configDefault.customRenamesCount)
                .setSaveConsumer(max -> config.customRenamesCount = max)
                .setTextGetter(integer -> {
                    if (integer == 100) {
                        return Renames.ALL.getText();
                    }
                    return Text.of(String.valueOf(integer));
                })
                .setTooltip(Text.translatable("better_anvil.mod_menu.cit_menu.renames_integer.tooltip"))
                .build());

        category.addEntry(menuCIT.build());

        return configBuilder.build();
    }
}
