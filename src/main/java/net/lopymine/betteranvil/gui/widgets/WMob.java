package net.lopymine.betteranvil.gui.widgets;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.lopymine.betteranvil.fake.FakeClientPlayerEntity;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.PlayerSkinDrawer;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.screen.world.WorldCreator;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.*;
import net.minecraft.client.render.entity.*;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.EntityModels;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.gen.WorldPreset;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.BufferUtils;

import java.util.Map;

public class WMob extends WWidget {
    private int size = 100;
    private double tick = 0;
    private final Random random = Random.create();
    private final int d = -32768;
    private final float s = this.random.nextFloat() * 3.1415927F * 2.0F;
    private LivingEntity entity;
    public WMob(LivingEntity entity) {
        this.entity = entity;
        if(entity == null) return;
        if(entity instanceof FakeClientPlayerEntity player){
            player.getInventory().armor.clear();
            player.getInventory().main.clear();
        }
    }

    public void setEntity(LivingEntity entity){
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
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
        if(entity == null) return;
        renderPlayer(context, x, y, size,(float) tick, entity);
    }

    public void renderPlayer(DrawContext context, int x, int y, int size, float tick, LivingEntity entity) {
        Quaternionf quaternionf = (new Quaternionf()).rotateZ(3.1415927F);
        Quaternionf quaternionf2 = (new Quaternionf()).rotateY(getRotation(tick));
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
        InventoryScreen.drawEntity(context, x, y, size, quaternionf, quaternionf2, entity);
        entity.bodyYaw = h;
        entity.setYaw(i);
        entity.setPitch(j);
        entity.prevHeadYaw = k;
        entity.headYaw = l;
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
            int index = getIntByType(item.getType());
            if(index == -1){
                return;
            }
            inventory.armor.set(index,stack);
        } else {
            inventory.main.set(0,stack);
        }
    }

    private int getIntByType(ArmorItem.Type type){
        switch (type){
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
        return ((float)d + tickDelta) / 20.0F + s;
    }

    public void setEntitySize(int i) {
        this.size = i;
    }
}
