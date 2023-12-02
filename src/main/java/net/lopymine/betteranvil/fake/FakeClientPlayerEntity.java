package net.lopymine.betteranvil.fake;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.*;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.stat.StatHandler;
import net.minecraft.util.*;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;

import org.jetbrains.annotations.Nullable;

public class FakeClientPlayerEntity extends ClientPlayerEntity {
    private Identifier skin = null;
    private String model = null;

    public static FakeClientPlayerEntity getInstance() {
        return new FakeClientPlayerEntity();
    }

    private FakeClientPlayerEntity() {
        super(MinecraftClient.getInstance(), FakeWorld.getInstance(), FakeClientPlayNetworkHandler.getInstance(), new StatHandler(), new ClientRecipeBook(), false, false);

        MinecraftClient.getInstance().getSkinProvider().loadSkin(MinecraftClient.getInstance().getSession().getProfile(), (type, identifier, texture) -> {
            if (type == MinecraftProfileTexture.Type.SKIN) {
                this.model = texture.getMetadata("model");
                this.skin = identifier;
            }
        }, true);

    }

    @Override
    public boolean isPartVisible(PlayerModelPart modelPart) {
        return true;
    }

    @Override
    public boolean hasSkinTexture() {
        return true;
    }

    @Override
    public String getModel() {
        return model != null ? model : DefaultSkinHelper.getModel(Uuids.getUuidFromProfile(MinecraftClient.getInstance().getSession().getProfile()));
    }

    @Override
    public Identifier getSkinTexture() {
        return skin != null ? skin : DefaultSkinHelper.getTexture(Uuids.getUuidFromProfile(MinecraftClient.getInstance().getSession().getProfile()));
    }

    @Nullable
    @Override
    protected PlayerListEntry getPlayerListEntry() {
        return null;
    }

    @Override
    public boolean isSpectator() {
        return false;
    }

    @Override
    public boolean isCreative() {
        return true;
    }
}