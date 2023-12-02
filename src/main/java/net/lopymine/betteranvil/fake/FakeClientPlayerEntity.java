package net.lopymine.betteranvil.fake;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.*;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.stat.StatHandler;

import org.jetbrains.annotations.Nullable;

public class FakeClientPlayerEntity extends ClientPlayerEntity {
    private final SkinTextures skinTextures;

    private FakeClientPlayerEntity() {
        super(MinecraftClient.getInstance(), FakeWorld.getInstance(), FakeClientPlayNetworkHandler.getInstance(), new StatHandler(), new ClientRecipeBook(), false, false);

        this.skinTextures = MinecraftClient.getInstance().getSkinProvider().getSkinTextures(MinecraftClient.getInstance().getGameProfile());
    }

    public static FakeClientPlayerEntity getInstance() {
        return new FakeClientPlayerEntity();
    }

    @Override
    public boolean isPartVisible(PlayerModelPart modelPart) {
        return true;
    }

    @Override
    public SkinTextures getSkinTextures() {
        return skinTextures;
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