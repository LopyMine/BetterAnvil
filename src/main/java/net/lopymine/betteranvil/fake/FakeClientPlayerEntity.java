package net.lopymine.betteranvil.fake;

import com.mojang.authlib.minecraft.BaseMinecraftSessionService;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import com.mojang.util.UUIDTypeAdapter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.Session;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class FakeClientPlayerEntity extends ClientPlayerEntity {
    private Identifier skinIdentifier = null;
    private String model = null;
    private final MinecraftClient mc = MinecraftClient.getInstance();
    public static FakeClientPlayerEntity getInstance() {
        return new FakeClientPlayerEntity();
    }

    private FakeClientPlayerEntity() {
        super(MinecraftClient.getInstance(), FakeWorld.getInstance(), FakeClientPlayNetworkHandler.getInstance(), null, null,false, false);

        mc.getSkinProvider().loadSkin(getGameProfile(), (type, identifier, texture) -> {
            if(type == MinecraftProfileTexture.Type.SKIN){
                this.model = texture.getMetadata("model");
                this.skinIdentifier = identifier;
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
        return model != null ? model : DefaultSkinHelper.getModel(UUIDTypeAdapter.fromString(mc.getSession().getUuid()));
    }

    @Override
    public Identifier getSkinTexture() {
        return skinIdentifier == null ? DefaultSkinHelper.getTexture(UUIDTypeAdapter.fromString(mc.getSession().getUuid())) : skinIdentifier;
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