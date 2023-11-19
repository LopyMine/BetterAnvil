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
import net.minecraft.util.math.random.Random;
import org.joml.*;

import com.mojang.blaze3d.systems.RenderSystem;

import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;

import java.lang.Math;

public class WDroppedItem extends WWidget {
    // Item param
    private ItemStack stack;
    private BakedModel model;

    // Render param
    private final float s = Random.create().nextFloat() * 3.1415927F * 2.0F;
    private boolean hasException = false;
    private final int d = -32768;
    private float tickDeltaX = 0;
    private float tickDeltaY = 0;
    private int size = 100;

    // Rotation param
    private boolean isRotating = false;
    private float anchorX = 0f;
    private float anchorY = 0f;
    private float anchorAngleX = 0f;
    private float anchorAngleY = 0f;
    private float angleX;
    private float angleY;
    private int draggingX;

    public WDroppedItem(ItemStack stack) {
        this.stack = stack;
        this.model = MinecraftClient.getInstance().getItemRenderer().getModel(this.stack, null, null, 0);
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
        this.model = MinecraftClient.getInstance().getItemRenderer().getModel(stack, null, null, 0);
    }

    @Override
    public void tick() {
        if (isRotating) {
            return;
        }
        this.tickDeltaX = tickDeltaX + 0.5F;
        this.tickDeltaY = tickDeltaY + 0.5F;
    }

    public int getItemSize() {
        return size;
    }

    public void setItemSize(int size) {
        this.size = size;
    }

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
        try {
            this.renderItem(context.getMatrices(), x, y, size, model, angleX, tickDeltaX, tickDeltaY);
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

        boolean bl = !model.isSideLit();
        if (bl) {
            DiffuseLighting.disableGuiDepthLighting();
        }

        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.multiplyPositionMatrix(matrices.peek().getPositionMatrix());
        RenderSystem.applyModelViewMatrix();

        Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        this.renderItem(new MatrixStack(), immediate, angleX, this.getRotation(tickDeltaX), tickDeltaY);
        immediate.draw();

        RenderSystem.enableDepthTest();
        if (bl) {
            DiffuseLighting.enableGuiDepthLighting();
        }

        matrices.pop();
        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
    }

    private void renderItem(MatrixStack matrices, Immediate immediate, float rotationX, float rotationY, float tickDeltaY) {
        matrices.push();
        boolean bl = model.hasDepth();
        float l = MathHelper.sin(((float)d + tickDeltaY) / 10.0F + s) * 0.1F + 0.1F;
        float m = model.getTransformation().getTransformation(ModelTransformationMode.GROUND).scale.y();
        matrices.translate(0.0, (double)(l + 0.25F * m), 0.0);

        float xRot = (float) Math.atan(rotationX / 40.0F);
        matrices.multiply(new Quaternionf().rotateX(-xRot * 20.0F * 0.017453292F).rotateY(rotationY));

        float o = model.getTransformation().ground.scale.x();
        float p = model.getTransformation().ground.scale.y();
        float q = model.getTransformation().ground.scale.z();
        float s;
        float t;
        if (!bl) {
            float r = -0.0F * (float) (0) * 0.5F * o;
            s = -0.0F * (float) (0) * 0.5F * p;
            t = -0.09375F * (float) (0) * 0.5F * q;
            matrices.translate(r, s, (double) t);
        }

        matrices.push();
        MinecraftClient.getInstance().getItemRenderer().renderItem(stack, ModelTransformationMode.GUI, false, matrices, immediate, 15728880, OverlayTexture.DEFAULT_UV, model);
        matrices.pop();

        if (!bl) {
            matrices.translate(0.0F * o, 0.0F * p, (double) (0.09375F * q));
        }

        matrices.pop();
    }

    public float getRotation(float tickDelta) {
        return ((float) d + tickDelta) / 20.0F + s;
    }

    @Override
    public InputResult onMouseDrag(int x, int y, int button, double deltaX, double deltaY) {
        if (isRotating) {

            // _ Rotation
            if (draggingX != x) {
                tickDeltaX = tickDeltaX + (deltaX < 0 ? -0.85F : 0.85F);
                draggingX = x;
            }

            // | Rotation
            float angleX = anchorAngleX + (anchorY - y);
            float angleY = anchorAngleY - (anchorX - x);

            if (angleX <= 90 && angleX >= -90) {
                this.angleX = angleX;
            }
            if (angleY <= 90 && angleY >= -90) {
                this.angleY = angleY;
            }
        }

        return InputResult.PROCESSED;
    }

    @Override
    public InputResult onMouseUp(int x, int y, int button) {
        if (button == 0) {
            isRotating = false;
            return InputResult.PROCESSED;
        }

        return InputResult.IGNORED;
    }

    @Override
    public InputResult onMouseDown(int x, int y, int button) {
        anchorX = (float) x;
        anchorY = (float) y;

        anchorAngleX = angleX;
        anchorAngleY = angleY;

        isRotating = true;
        return InputResult.PROCESSED;
    }
}
