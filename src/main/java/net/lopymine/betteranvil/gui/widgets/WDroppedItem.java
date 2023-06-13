package net.lopymine.betteranvil.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.ItemEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.joml.Matrix4f;

public class WDroppedItem extends WWidget {
    private int size = 100;
    private ItemStack stack;
    private final MinecraftClient mc = MinecraftClient.getInstance();
    private final ItemRenderer renderer = MinecraftClient.getInstance().getItemRenderer();
    private final VertexConsumerProvider.Immediate immediate = mc.getBufferBuilders().getEntityVertexConsumers();
    private BakedModel model;
    private ItemEntity entity;
    private final ItemEntityRenderer entityRenderer;
    private double tick = 0;

    public WDroppedItem(ItemStack stack) {
        this.stack = stack;
        model = renderer.getModel(this.stack, null, null, 0);
        entity = new ItemEntity(mc.player.world, 0, 0, 0, stack);
        entityRenderer = (ItemEntityRenderer) mc.getEntityRenderDispatcher().getRenderer(entity);
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
        this.entity = new ItemEntity(mc.player.world, 0, 0, 0, stack);
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
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        matrices.push();

        matrices.translate(x, y, 500.0F);
        matrices.multiplyPositionMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
        matrices.scale(size, size, size);
        VertexConsumerProvider.Immediate immediate = mc.getBufferBuilders().getEntityVertexConsumers();
        boolean bl = !model.isSideLit();
        if (bl) {
            DiffuseLighting.disableGuiDepthLighting();
        }

        MatrixStack matrixStack = RenderSystem.getModelViewStack();

        matrixStack.push();
        matrixStack.multiplyPositionMatrix(matrices.peek().getPositionMatrix());
        RenderSystem.applyModelViewMatrix();

        renderItem(new MatrixStack(),(float) tick);

        immediate.draw();
        RenderSystem.enableDepthTest();
        if (bl) {
            DiffuseLighting.enableGuiDepthLighting();
        }

        matrices.pop();
        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
    }

    private void renderItem(MatrixStack matrices, float tick) {
        matrices.push();
        ItemStack itemStack = entity.getStack();

        boolean bl = model.hasDepth();
        int k = 1;
        float l = MathHelper.sin(((float)entity.getItemAge() + tick) / 10.0F + entity.uniqueOffset) * 0.1F + 0.1F;
        float m = model.getTransformation().getTransformation(ModelTransformation.Mode.GROUND).scale.y();
        matrices.translate(0.0F, l + 0.25F * m, 0.0F);
        float n = entity.getRotation(tick);
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
            matrices.translate(r, s, t);
        }

        for(int u = 0; u < k; ++u) {
            matrices.push();

            this.renderer.renderItem(itemStack, ModelTransformation.Mode.GUI, false, matrices, immediate, 15728880, OverlayTexture.DEFAULT_UV, model);
            matrices.pop();
            if (!bl) {
                matrices.translate(0.0F * o, 0.0F * p, 0.09375F * q);
            }
        }

        matrices.pop();
    }

}
