package net.lopymine.betteranvil.gui.screen;

import net.minecraft.client.gui.DrawContext;
import org.lwjgl.opengl.GL11;

import io.github.cottonmc.cotton.gui.GuiDescription;
import io.github.cottonmc.cotton.gui.client.*;
import io.github.cottonmc.cotton.gui.impl.VisualLogger;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;

import net.lopymine.betteranvil.gui.widgets.buttons.*;
import net.lopymine.betteranvil.gui.widgets.entity.*;

import org.jetbrains.annotations.Nullable;

public class BetterAnvilScreen extends CottonClientScreen {
    @Nullable
    private WWidget hit;

    public BetterAnvilScreen(GuiDescription description) {
        super(description);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int mouseButton, double deltaX, double deltaY) {
        int containerX = (int) mouseX - left;
        int containerY = (int) mouseY - top;

        WPanel root = description.getRootPanel();

        if (root.equals(this.hit)) {
            for (WWidget widget : root.streamChildren().toList()) {
                if (widget instanceof WDroppedItem droppedItem) {
                    droppedItem.onMouseDrag(containerX - droppedItem.getAbsoluteX(), containerY - droppedItem.getAbsoluteY(), mouseButton, deltaX, deltaY);
                }
                if (widget instanceof WMob mob) {
                    mob.onMouseDrag(containerX - mob.getAbsoluteX(), containerY - mob.getAbsoluteY(), mouseButton, deltaX, deltaY);
                }
            }
        }

        return super.mouseDragged(mouseX, mouseY, mouseButton, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        int containerX = (int) mouseX - left;
        int containerY = (int) mouseY - top;

        WPanel root = description.getRootPanel();

        if (root.equals(this.hit)) {
            for (WWidget widget : description.getRootPanel().streamChildren().toList()) {
                if (widget instanceof WMob mob) {
                    mob.onMouseUp(containerX - mob.getAbsoluteX(), containerY - mob.getAbsoluteY(), mouseButton);
                }
                if (widget instanceof WDroppedItem droppedItem) {
                    droppedItem.onMouseUp(containerX - droppedItem.getAbsoluteX(), containerY - droppedItem.getAbsoluteY(), mouseButton);
                }
                hit = null;
            }
        }

        return super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        int containerX = (int) mouseX - left;
        int containerY = (int) mouseY - top;

        WPanel root = description.getRootPanel();
        WWidget hit = root.hit(containerX, containerY);

        if (hit.equals(root)) {
            for (WWidget widget : root.streamChildren().toList()) {
                if (widget instanceof WMob mob) {
                    mob.onMouseDown(containerX - mob.getAbsoluteX(), containerY - mob.getAbsoluteY(), mouseButton);
                }
                if (widget instanceof WDroppedItem droppedItem) {
                    droppedItem.onMouseDown(containerX - droppedItem.getAbsoluteX(), containerY - droppedItem.getAbsoluteY(), mouseButton);
                }
            }
            this.hit = hit;
        }

        return super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean keyPressed(int ch, int keyCode, int modifiers) {
        if (super.keyPressed(ch, keyCode, modifiers)) {
            return true;
        }

        WWidget focus = description.getFocus();
        if (focus instanceof WTextField) {
            return true;
        }

        return description.getRootPanel().onKeyPressed(ch, keyCode, modifiers) == InputResult.PROCESSED;
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float partialTicks) {
        int containerX = mouseX - left;
        int containerY = mouseY - top;

        WWidget focus = description.getFocus();
        if (focus instanceof WRenameButton || focus instanceof WStarButton) {
            description.releaseFocus(focus);
        }

        WWidget hit = description.getRootPanel().hit(containerX, containerY);
        if (hit instanceof WRenameButton || hit instanceof WStarButton) {
            description.requestFocus(hit);
        }

        super.render(context, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean shouldCloseOnEsc() {
        return false;
    }
}
