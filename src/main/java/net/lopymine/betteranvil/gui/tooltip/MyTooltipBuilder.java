package net.lopymine.betteranvil.gui.tooltip;

import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyTooltipBuilder implements TooltipBuilder{
    public List<Text> lines = new ArrayList<>();
    @Override
    public int size() {
        return lines.size();
    }

    @Override
    public void add(Text... texts) {
        Collections.addAll(lines,texts);
    }
}
