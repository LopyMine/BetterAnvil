package net.lopymine.betteranvil.gui.widgets.entity;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.*;
import net.minecraft.client.render.VertexConsumerProvider.Immediate;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import org.joml.*;

import com.mojang.blaze3d.systems.RenderSystem;

import java.lang.Math;

public class WDroppedItem extends WRotatableWidget {
    private ItemStack itemStack;
    private BakedModel bakedModel;
    private boolean hasException = false;

    public WDroppedItem(ItemStack itemStack) {
        this.setItemStack(itemStack);
    }

    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
        this.bakedModel = MinecraftClient.getInstance().getItemRenderer().getModel(itemStack, null, null, 0);
    }

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
        if (itemStack == null || bakedModel == null) {
            return;
        }

        try {
            applyScissor(context);
            this.renderItem(context.getMatrices(), x, y, size, bakedModel, angleX, tickDeltaX, tickDeltaY);
            disableScissor(context);
        } catch (Exception o) {
            if (this.hasException) {
                return;
            }
            o.printStackTrace();

            this.hasException = true;
        }

        if (isRotating) {
            return;
        }

        if (angleX > 0) {
            angleX -= 0.5F;
        }

        if (angleX < 0) {
            angleX += 0.5F;
        }
    }

    private void renderItem(MatrixStack matrices, int x, int y, int size, BakedModel model, float angleX, float tickDeltaX, float tickDeltaY) {
        matrices.push();
        matrices.translate((float) x, (float) y, 700.0F);
        matrices.multiplyPositionMatrix((new Matrix4f()).scaling(1.0F, -1.0F, 1.0F));
        matrices.scale(size, size, size);

        boolean bl = !bakedModel.isSideLit();
        if (bl) {
            DiffuseLighting.disableGuiDepthLighting();
        }

        Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();

        float l = MathHelper.sin(((float) d + tickDeltaY) / 10.0F + s) * 0.1F + 0.1F;
        float m = model.getTransformation().getTransformation(ModelTransformationMode.GROUND).scale.y();
        matrices.translate(0.0, (double) (l + 0.25F * m), 0.0);

        float xRot = (float) Math.atan(angleX / 40.0F);
        matrices.multiply(new Quaternionf().rotateX(-xRot * 20.0F * 0.017453292F).rotateY(getRotation(tickDeltaX)));

        MinecraftClient.getInstance().getItemRenderer().renderItem(itemStack, ModelTransformationMode.GUI, false, matrices, immediate, 15728880, OverlayTexture.DEFAULT_UV, model);

        RenderSystem.disableDepthTest();
        immediate.draw();
        RenderSystem.enableDepthTest();

        if (bl) {
            DiffuseLighting.enableGuiDepthLighting();
        }

        matrices.pop();
    }
}
