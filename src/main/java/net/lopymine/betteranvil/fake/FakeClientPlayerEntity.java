package net.lopymine.betteranvil.fake;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.*;
import net.minecraft.client.recipebook.ClientRecipeBook;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.stat.StatHandler;

import org.jetbrains.annotations.Nullable;

public class FakeClientPlayerEntity extends ClientPlayerEntity {
    private static FakeClientPlayerEntity INSTANCE;
    private final SkinTextures skinTextures;

    private FakeClientPlayerEntity() {
        super(MinecraftClient.getInstance(), FakeWorld.getInstance(), FakeClientPlayNetworkHandler.getInstance(), new StatHandler(), new ClientRecipeBook(), false, false);

        this.skinTextures = MinecraftClient.getInstance().getSkinProvider().getSkinTextures(getGameProfile());
    }

    public static FakeClientPlayerEntity getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeClientPlayerEntity();
        }
        return INSTANCE;
    }

    public static void onInitialize() {
        INSTANCE = new FakeClientPlayerEntity();
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