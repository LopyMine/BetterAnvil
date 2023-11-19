package net.lopymine.betteranvil.gui.description;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import net.lopymine.betteranvil.gui.description.handler.CMDGuiHandler;
import net.lopymine.betteranvil.config.resourcepacks.cmd.CMDItem;

public class CMDGuiDescription extends GuiDescription<CMDGuiHandler, CMDItem> {
    protected CMDGuiDescription(CMDGuiHandler handler, Screen parent) {
        super(handler, parent, Text.translatable("better_anvil.cmd_menu.title"));
    }
}
