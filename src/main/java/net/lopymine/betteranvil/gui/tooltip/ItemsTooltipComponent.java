package net.lopymine.betteranvil.gui.tooltip;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import net.fabricmc.api.*;

import net.lopymine.betteranvil.BetterAnvil;

import java.util.List;

public class ItemsTooltipComponent implements TooltipComponent {
    private static final Identifier BACKGROUND_TEXTURE_DARK = BetterAnvil.i("bundle/background");
    private static final Identifier BACKGROUND_TEXTURE = new Identifier("container/bundle/background");
    private final List<ItemStack> items;
    private final boolean isDarkMode;

    public ItemsTooltipComponent(List<ItemStack> list, boolean isDarkMode) {
        this.items = list;
        this.isDarkMode = isDarkMode;
    }

    public int getHeight() {
        return this.getRowsHeight();
    }

    public int getWidth(TextRenderer textRenderer) {
        return this.getColumnsWidth();
    }

    private int getColumnsWidth() {
        return this.getColumns() * 18 + 2;
    }

    private int getRowsHeight() {
        return this.getRows() * 20 + 2;
    }

    @Override
    public void drawItems(TextRenderer textRenderer, int x, int y, DrawContext context) {
        int i = this.getColumns();
        int j = this.getRows();
        int k = 0;

        context.drawGuiTexture((isDarkMode ? BACKGROUND_TEXTURE_DARK : BACKGROUND_TEXTURE), x, y, this.getColumnsWidth(), this.getRowsHeight());

        for (int l = 0; l < j; ++l) {
            for (int m = 0; m < i; ++m) {
                int n = x + m * 18 + 1;
                int o = y + l * 20 + 1;
                this.drawSlot(n, o, k++, context, textRenderer);
            }
        }
    }

    private void drawSlot(int x, int y, int index, DrawContext context, TextRenderer textRenderer) {
        Sprite sprite = isDarkMode ? Sprite.DARK_SLOT : Sprite.SLOT;
        if (index >= this.items.size()) {
            this.draw(context, x, y, sprite);
        } else {
            ItemStack itemStack = this.items.get(index);
            this.draw(context, x, y, sprite);
            context.drawItem(itemStack, x + 1, y + 1, index);
            context.drawItemInSlot(textRenderer, itemStack, x + 1, y + 1);
        }
    }

    private void draw(DrawContext context, int x, int y, Sprite sprite) {
        context.drawGuiTexture(sprite.texture, x, y, 0, sprite.width, sprite.height);
    }

    private int getColumns() {
        return Math.max(1, (int) Math.ceil(Math.sqrt(this.items.size())));
    }

    private int getRows() {
        return (int) Math.ceil(((double) this.items.size()) / (double) this.getColumns());
    }

    @Environment(EnvType.CLIENT)
    private enum Sprite {
        SLOT(new Identifier("container/bundle/slot"), 18, 20),
        DARK_SLOT(BetterAnvil.i("bundle/slot"), 18, 20);

        public final Identifier texture;
        public final int width;
        public final int height;

         Sprite(Identifier texture, int width, int height) {
            this.texture = texture;
            this.width = width;
            this.height = height;
        }
    }
}
