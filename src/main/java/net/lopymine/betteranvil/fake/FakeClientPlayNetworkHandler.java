package net.lopymine.betteranvil.fake;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;

public class FakeClientPlayNetworkHandler extends ClientPlayNetworkHandler {
    private static FakeClientPlayNetworkHandler INSTANCE;

    private FakeClientPlayNetworkHandler() {
        super(MinecraftClient.getInstance(), null, new ClientConnection(NetworkSide.CLIENTBOUND), MinecraftClient.getInstance().getSession().getProfile(), MinecraftClient.getInstance().createTelemetrySender());
    }

    public static FakeClientPlayNetworkHandler getInstance() {
        if (INSTANCE == null) INSTANCE = new FakeClientPlayNetworkHandler();
        return INSTANCE;
    }

}
