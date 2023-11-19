package net.lopymine.betteranvil.gui.widgets.entity;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.*;
import net.minecraft.entity.player.*;
import net.minecraft.item.*;
import net.minecraft.util.math.random.Random;
import org.joml.Quaternionf;

import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;

import net.lopymine.betteranvil.fake.FakeClientPlayerEntity;

public class WMob extends WWidget {
    // Entity param
    private LivingEntity entity;
    private EquipmentSlot swapSlot = EquipmentSlot.OFFHAND;

    // Render param
    private final float s = Random.create().nextFloat() * 3.1415927F * 2.0F;
    private boolean hasException = false;
    private float tickDelta = 0;
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
        this.tickDelta = (float) (tickDelta + 0.5);
    }

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
        if (entity == null) {
            return;
        }

        try {
            renderPlayer(context, x, y, size, angleX, getRotation(tickDelta), entity);
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
        float xRot = (float) Math.atan(rotationX / 20.0F);
        Quaternionf quaternionf = new Quaternionf().rotateZ(3.1415927F);
        Quaternionf quaternionf2 = new Quaternionf().rotateX(xRot * 20.0F * 0.017453292F).rotateY(rotationY);
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

    public float getRotation(float tickDelta) {
        return ((float) -32768 + tickDelta) / 20.0F + s;
    }

    public void setEntitySize(int size) {
        this.size = size;
    }

    @Override
    public InputResult onMouseDrag(int x, int y, int button, double deltaX, double deltaY) {
        if (isRotating) {

            // _ Rotation
            if (draggingX != x) {
                tickDelta = tickDelta + (deltaX < 0 ? -0.85F : 0.85F);
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
