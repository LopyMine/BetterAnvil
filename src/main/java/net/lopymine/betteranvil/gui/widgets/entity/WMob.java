package net.lopymine.betteranvil.gui.widgets.entity;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import org.joml.*;

import net.lopymine.betteranvil.fake.FakeClientPlayerEntity;

import java.lang.Math;

public class WMob extends WRotatableWidget {
    private LivingEntity entity;
    private EquipmentSlot swapSlot = EquipmentSlot.OFFHAND;
    private boolean hasException = false;

    public WMob(LivingEntity entity) {
        this.entity = entity;
        if (entity == null) {
            return;
        }
        if (entity instanceof FakeClientPlayerEntity player) {
            player.getInventory().armor.clear();
            player.getInventory().main.clear();
        }
    }

    public void setEntity(LivingEntity entity) {
        this.entity = entity;
        if (entity == null) {
            return;
        }
        if (entity instanceof FakeClientPlayerEntity player) {
            player.getInventory().clear();
        }
    }

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
        if (entity == null) {
            return;
        }

        try {
            //applyScissor(context, x, y);
            renderPlayer(context, x, y, size, angleX, getRotation(tickDeltaX), entity);
            //disableScissor(context);
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

    public void renderPlayer(DrawContext context, int x, int y, int size, float rotationX, float rotationY, LivingEntity entity) {
        float rotation = (float) Math.atan(rotationX / 20.0F);
        Quaternionf quaternionf = new Quaternionf().rotateZ(3.1415927F);
        Quaternionf quaternionf2 = new Quaternionf().rotateX(rotation * 20.0F * 0.017453292F).rotateY(rotationY);
        quaternionf.mul(quaternionf2);

        float h = entity.bodyYaw;
        float i = entity.getYaw();
        float j = entity.getPitch();
        float k = entity.prevHeadYaw;
        float l = entity.headYaw;
        entity.bodyYaw = 180.0F + 0 * 20.0F;
        entity.setYaw(180.0F + 0 * 40.0F);
        entity.setPitch(-0 * 20.0F);
        entity.headYaw = entity.getYaw();
        entity.prevHeadYaw = entity.getYaw();
        MatrixStack matrices = context.getMatrices();
        matrices.push();
        matrices.translate(0.0, 0.0, 250.0);
        InventoryScreen.drawEntity(context, x, y, size, quaternionf, quaternionf2, entity);
        matrices.pop();
        entity.bodyYaw = h;
        entity.setYaw(i);
        entity.setPitch(j);
        entity.prevHeadYaw = k;
        entity.headYaw = l;
    }

    public void setArmor(ItemStack stack) {
        if (!(entity instanceof FakeClientPlayerEntity player)) {
            return;
        }

        PlayerInventory inventory = player.getInventory();
        inventory.clear();

        if (stack.getItem() == Items.CARVED_PUMPKIN) {
            inventory.armor.set(3, stack);
            this.swapSlot = EquipmentSlot.HEAD;
        } else if (stack.getItem().equals(Items.ELYTRA)) {
            inventory.armor.set(2, stack);
            this.swapSlot = EquipmentSlot.CHEST;
        } else if (stack.getItem() instanceof ArmorItem armorItem) {
            int index = getIntByType(armorItem.getType());
            if (index == -1) {
                return;
            }
            inventory.armor.set(index, stack);
            this.swapSlot = armorItem.getSlotType();
        } else {
            inventory.main.set(0, stack);
            this.swapSlot = EquipmentSlot.MAINHAND;
        }
    }

    private int getIntByType(ArmorItem.Type type) {
        switch (type) {
            case HELMET -> {
                return 3;
            }
            case CHESTPLATE -> {
                return 2;
            }
            case LEGGINGS -> {
                return 1;
            }
            case BOOTS -> {
                return 0;
            }
        }
        return -1;
    }

    public void swapItem() {
        if (!(entity instanceof PlayerEntity player)) {
            return;
        }

        ItemStack offStack = player.getEquippedStack(EquipmentSlot.OFFHAND);
        ItemStack mainStack = player.getEquippedStack(EquipmentSlot.MAINHAND);

        if (swapSlot == EquipmentSlot.MAINHAND || swapSlot == EquipmentSlot.OFFHAND) {
            player.equipStack(EquipmentSlot.OFFHAND, mainStack);
            player.equipStack(EquipmentSlot.MAINHAND, offStack);
        } else {
            player.equipStack(EquipmentSlot.OFFHAND, mainStack);
            player.equipStack(EquipmentSlot.MAINHAND, player.getEquippedStack(swapSlot));
            player.equipStack(swapSlot, offStack);
        }
    }
}