package net.lopymine.betteranvil.gui.widgets;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import org.joml.Matrix4f;

public class WDroppedItem extends WWidget {
    private int size = 100;
    private ItemStack stack;
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final ItemRenderer renderer = MinecraftClient.getInstance().getItemRenderer();
    private final VertexConsumerProvider.Immediate immediate = mc.getBufferBuilders().getEntityVertexConsumers();
    private BakedModel model;
    private double tick = 0;
    private final Random random = Random.create();
    private final int d = -32768;
    private final float s = this.random.nextFloat() * 3.1415927F * 2.0F;

    public WDroppedItem(ItemStack stack) {
        this.stack = stack;
        model = renderer.getModel(this.stack, null, null, 0);
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    public boolean canFocus() {
        return false;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
        this.model = renderer.getModel(stack, null, null, 0);
    }

    @Override
    public void tick() {
        tick = (float) (tick + 1);
    }

    public void setItemSize(int size) {
        this.size = size;
    }

    public int getItemSize() {
        return size;
    }

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
        context.getMatrices().push();
        context.getMatrices().translate((float)x, (float)y, 500.0F);
        context.getMatrices().multiplyPositionMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
        context.getMatrices().scale(size,size,size);
        boolean bl = !model.isSideLit();
        if (bl) {
            DiffuseLighting.disableGuiDepthLighting();
        }
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.multiplyPositionMatrix(context.getMatrices().peek().getPositionMatrix());
        RenderSystem.applyModelViewMatrix();

        renderItem(new MatrixStack(), (float) tick);

        immediate.draw();
        RenderSystem.enableDepthTest();
        if (bl) {
            DiffuseLighting.enableGuiDepthLighting();
        }

        context.getMatrices().pop();
        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
    }

    private void renderItem(MatrixStack matrices, float tick) {
        matrices.push();
        boolean bl = model.hasDepth();
        int k = 1;
        float l = MathHelper.sin(((float)d + tick) / 10.0F + s) * 0.1F + 0.1F;
        float m = model.getTransformation().getTransformation(ModelTransformationMode.GROUND).scale.y();
        matrices.translate(0.0, (double)(l + 0.25F * m), 0.0);
        float n = getRotation(tick);
        matrices.multiply(RotationAxis.POSITIVE_Y.rotation(n));
        float o = model.getTransformation().ground.scale.x();
        float p = model.getTransformation().ground.scale.y();
        float q = model.getTransformation().ground.scale.z();
        float s;
        float t;
        if (!bl) {
            float r = -0.0F * (float)(0) * 0.5F * o;
            s = -0.0F * (float)(0) * 0.5F * p;
            t = -0.09375F * (float)(0) * 0.5F * q;
            matrices.translate((double)r, (double)s, (double)t);
        }

        for(int u = 0; u < k; ++u) {
            matrices.push();

            this.renderer.renderItem(stack, ModelTransformationMode.GUI, false, matrices, immediate, 15728880, OverlayTexture.DEFAULT_UV, model);
            matrices.pop();
            if (!bl) {
                matrices.translate((double)(0.0F * o), (double)(0.0F * p), (double)(0.09375F * q));
            }
        }

        matrices.pop();
    }

    public float getRotation(float tickDelta) {
        return ((float)d + tickDelta) / 20.0F + s;
    }

}
