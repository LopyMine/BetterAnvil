package net.lopymine.betteranvil.gui.description;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import net.lopymine.betteranvil.config.resourcepacks.cit.CITItem;
import net.lopymine.betteranvil.gui.description.handler.CITGuiHandler;

public class CITGuiDescription extends GuiDescription<CITGuiHandler, CITItem> {
    protected CITGuiDescription(CITGuiHandler handler, Screen parent) {
        super(handler, parent, Text.translatable("better_anvil.cit_menu.title"));
    }
}
