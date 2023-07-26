package net.lopymine.betteranvil.gui.tooltip;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.resourcepacks.utils.ItemUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.List;

public class FakeBundleTooltipComponent implements TooltipComponent {
    private List<ItemStack> items;
    private int itemsInRow = 5;
    private int w;
    private int h;
    public FakeBundleTooltipComponent(List<ItemStack> list) {
        this.items = list;
        this.w = itemsInRow;
        this.h = (int) Math.ceil((double) list.size() / itemsInRow);
    }

    @Override
    public int getHeight() {
        return (h * 18) + 2;
    }

    @Override
    public int getWidth(TextRenderer textRenderer) {
        return itemsInRow * 18;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        this.drawItems(x,y,context);
    }

    private void drawItems(int x, int y, DrawContext context){
        int xM = 0;
        int yM = 0;

        for(ItemStack stack : items){
            context.getMatrices().push();
            context.getMatrices().translate(x,y,300);
            ScreenDrawing.texturedRect(context, xM*18,yM*18, 18, 18, new Identifier(BetterAnvil.ID, "gui/sprites/slot.png"), 0xFFFFFFFF);
            context.getMatrices().translate(0,0,50);
            context.drawItem(stack,(xM*18) + 1,(yM*18) + 1);
            context.getMatrices().pop();

            xM++;
            if (xM >= itemsInRow) {
                yM++;
                xM = 0;
            }
        }
    }
}
