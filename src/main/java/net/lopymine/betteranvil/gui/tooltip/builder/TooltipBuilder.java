package net.lopymine.betteranvil.gui.tooltip.builder;

import net.minecraft.text.Text;

public interface TooltipBuilder {
    int size();

    void add(Text... texts);
}
