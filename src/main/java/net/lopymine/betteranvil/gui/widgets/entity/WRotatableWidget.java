package net.lopymine.betteranvil.gui.widgets.entity;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.random.Random;

import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;

public class WRotatableWidget extends WWidget {
    // Render param
    protected final float s = Random.create().nextFloat() * 3.1415927F * 2.0F;
    protected final int d = -32768;
    protected float tickDeltaX = 0;
    protected float tickDeltaY = 0;
    protected int size = 100;

    // Rotation param
    protected boolean isRotating = false;
    protected float anchorX = 0f;
    protected float anchorY = 0f;
    protected float anchorAngleX = 0f;
    protected float anchorAngleY = 0f;
    protected float angleX;
    protected float angleY;
    protected int draggingX;

    // Scissors param
    protected int x1, x2 = 0;
    protected int y1, y2 = 0;

    protected WRotatableWidget() {
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    public boolean canFocus() {
        return false;
    }

    @Override
    public void tick() {
        if (isRotating) {
            return;
        }
        this.tickDeltaX = tickDeltaX + 0.5F;
        this.tickDeltaY = tickDeltaY + 0.5F;
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

    protected void applyScissor(DrawContext context) {
        context.enableScissor(x1, y1, x2, y2);
    }

    protected void disableScissor(DrawContext context) {
        context.disableScissor();
    }

    public void setScissors(int x1, int y1, int x2, int y2) {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    public void setEntitySize(int size) {
        this.size = size;
    }

    public float getRotation(float tickDelta) {
        return ((float) d + tickDelta) / 20.0F + s;
    }
}
