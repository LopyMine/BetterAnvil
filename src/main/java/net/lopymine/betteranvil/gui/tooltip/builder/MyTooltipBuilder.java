package net.lopymine.betteranvil.gui.tooltip.builder;

import net.minecraft.text.Text;

import java.util.*;

public class MyTooltipBuilder implements TooltipBuilder {
    public List<Text> lines = new ArrayList<>();

    @Override
    public int size() {
        return lines.size();
    }

    @Override
    public void add(Text... texts) {
        Collections.addAll(lines, texts);
    }
}
