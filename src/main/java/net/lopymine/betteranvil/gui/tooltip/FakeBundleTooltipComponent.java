package net.lopymine.betteranvil.gui.tooltip;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import net.lopymine.betteranvil.BetterAnvil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

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
    public void drawItems(TextRenderer textRenderer, int x, int y, MatrixStack matrices, ItemRenderer itemRenderer,int z) {
        this.drawItems(x,y,matrices,itemRenderer,z);
    }

    private void drawItems(int x, int y, MatrixStack matrices, ItemRenderer renderer, int z) {
        int xM = 0;
        int yM = 0;

        for (ItemStack stack : items) {
            ScreenDrawing.texturedRect(matrices, x + (xM * 18), y + (yM * 18), 18, 18, new Identifier(BetterAnvil.ID, "gui/sprites/slot.png"), 0xFFFFFFFF);

            drawItem(x + (xM * 18) + 1, y + (yM * 18) + 1, z, stack, renderer);

            xM++;
            if (xM >= itemsInRow) {
                yM++;
                xM = 0;
            }
        }
    }

    private void drawItem(int x, int y, int z, ItemStack stack, ItemRenderer renderer) {
        BakedModel model = renderer.getModel(stack, (World) null,null,0);

        MinecraftClient.getInstance().getTextureManager().getTexture(SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE).setFilter(false, false);
        RenderSystem.setShaderTexture(0, SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE);
        RenderSystem.enableBlend();
        RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.translate((double) x, (double) y, (double) (100.0F + z));
        matrixStack.translate(8.0, 8.0, 0.0);
        matrixStack.scale(1.0F, -1.0F, 1.0F);
        matrixStack.scale(16.0F, 16.0F, 16.0F);
        RenderSystem.applyModelViewMatrix();
        MatrixStack matrixStack2 = new MatrixStack();
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        boolean bl = !model.isSideLit();
        if (bl) {
            DiffuseLighting.disableGuiDepthLighting();
        }

        renderer.renderItem(stack, ModelTransformation.Mode.GUI, false, matrixStack2, immediate, 15728880, OverlayTexture.DEFAULT_UV, model);
        immediate.draw();
        RenderSystem.enableDepthTest();
        if (bl) {
            DiffuseLighting.enableGuiDepthLighting();
        }

        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
    }
}
