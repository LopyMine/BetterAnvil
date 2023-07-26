package net.lopymine.betteranvil.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.lopymine.betteranvil.fake.FakeClientPlayerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.math.random.Random;

public class WMob extends WWidget {
    private int size = 100;
    private final EntityRenderDispatcher dispatcher = MinecraftClient.getInstance().getEntityRenderDispatcher();
    private final VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
    private double tick = 0;
    private final Random random = Random.create();
    private final int d = -32768;
    private final float s = this.random.nextFloat() * 3.1415927F * 2.0F;
    private Entity entity;
    public WMob(Entity entity) {
        this.entity = entity;
        if(entity == null) return;
        if(entity instanceof FakeClientPlayerEntity player){
            player.getInventory().armor.clear();
            player.getInventory().main.clear();
        }
    }

    public void setEntity(Entity entity){
        this.entity = entity;
        if(entity == null) return;
        if(entity instanceof FakeClientPlayerEntity player){
            player.getInventory().armor.clear();
            player.getInventory().main.clear();
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
        tick = (float) (tick + 1);
    }

    @Override
    public void renderTooltip(MatrixStack matrices, int x, int y, int tX, int tY) {
        super.renderTooltip(matrices, x, y, tX, tY);
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        if(entity == null) return;
        renderPlayer(x, y, size,(float) tick, entity);
    }

    public void renderPlayer(int x, int y, int size, float tick, Entity entity) {
        MatrixStack matrixStack = RenderSystem.getModelViewStack();
        matrixStack.push();
        matrixStack.translate((double)x, (double)y, 1850.0);
        matrixStack.scale(1.0F, 1.0F, -1.0F);
        RenderSystem.applyModelViewMatrix();
        MatrixStack matrixStack2 = new MatrixStack();
        matrixStack2.translate(0.0, 0.0, 1000.0);
        matrixStack2.scale((float)size, (float)size, (float)size);
        Quaternion quaternion = Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F);
        Quaternion quaternion2 = Vec3f.POSITIVE_Y.getRadialQuaternion(getRotation(tick));
        quaternion.hamiltonProduct(quaternion2);
        matrixStack2.multiply(quaternion);

        float i = entity.getYaw();
        float j = entity.getPitch();

        entity.setYaw(180.0F + 0 * 40.0F);
        entity.setPitch(-0 * 20.0F);

        DiffuseLighting.method_34742();
        quaternion2.conjugate();
        dispatcher.setRotation(quaternion2);
        dispatcher.setRenderShadows(false);
        VertexConsumerProvider.Immediate immediate = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        RenderSystem.runAsFancy(() -> {
            dispatcher.render(entity, 0.0, 0.0, 0.0, 0.0F, 1.0F, matrixStack2, immediate, 15728880);
        });
        immediate.draw();
        dispatcher.setRenderShadows(true);

        entity.setYaw(i);
        entity.setPitch(j);

        matrixStack.pop();
        RenderSystem.applyModelViewMatrix();
        DiffuseLighting.enableGuiDepthLighting();
    }

    public void setArmor(ItemStack stack) {
        PlayerInventory inventory;
        if(entity instanceof FakeClientPlayerEntity player){
            inventory = player.getInventory();
        } else {
            return;
        }
        inventory.armor.clear();
        inventory.main.clear();

        if(stack.getItem() == Items.CARVED_PUMPKIN){
            inventory.armor.set(3,stack);
        } else if (stack.getItem().equals(Items.ELYTRA)){
            inventory.armor.set(3,stack);
        } else if(stack.getItem() instanceof ArmorItem item) {
            int index = getIntByType(item.getSlotType());
            if(index == -1){
                return;
            }
            inventory.armor.set(index,stack);
        } else {
            inventory.main.set(0,stack);
        }
    }

    private int getIntByType(EquipmentSlot type){
        switch (type){
            case HEAD -> {
                return 3;
            }
            case CHEST -> {
                return 2;
            }
            case LEGS -> {
                return 1;
            }
            case FEET -> {
                return 0;
            }
        }
        return -1;
    }

    public float getRotation(float tickDelta) {
        return ((float)d + tickDelta) / 20.0F + s;
    }

    public void setEntitySize(int i) {
        this.size = i;
    }
}
