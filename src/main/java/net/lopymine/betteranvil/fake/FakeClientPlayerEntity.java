package net.lopymine.betteranvil.fake;

import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.PlayerListEntry;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.util.Identifier;
import net.minecraft.util.Uuids;
import org.jetbrains.annotations.Nullable;

public class FakeClientPlayerEntity extends ClientPlayerEntity {
    private Identifier skin = null;
    private String model = null;
    public static FakeClientPlayerEntity getInstance() {
        return new FakeClientPlayerEntity();
    }

    private FakeClientPlayerEntity() {
        super(MinecraftClient.getInstance(), FakeWorld.getInstance(), FakeClientPlayNetworkHandler.getInstance(), null, null,false, false);

        client.getSkinProvider().loadSkin(getGameProfile(), (type, identifier, texture) -> {
            if(type == MinecraftProfileTexture.Type.SKIN){
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
        return model != null ? model : DefaultSkinHelper.getModel(Uuids.getUuidFromProfile(client.getSession().getProfile()));
    }

    @Override
    public Identifier getSkinTexture() {
        return skin != null ? skin : DefaultSkinHelper.getTexture(Uuids.getUuidFromProfile(client.getSession().getProfile()));
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