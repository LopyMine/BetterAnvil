package net.lopymine.betteranvil.gui.tooltip.utils;

import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;

import java.util.List;

public record TooltipComponentsData(List<TooltipComponent> list) implements TooltipData {
}
