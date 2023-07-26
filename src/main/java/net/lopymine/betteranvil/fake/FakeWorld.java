package net.lopymine.betteranvil.fake;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Difficulty;

public class FakeWorld extends ClientWorld {

    private static FakeWorld instance;

    public static FakeWorld getInstance() {
        if (instance == null) instance = new FakeWorld();
        return instance;
    }


    public FakeWorld() {
        super(FakeClientPlayNetworkHandler.getInstance(), new Properties(Difficulty.EASY, false, true), null, new FakeRegistryEntry<>(FakeDimension.getInstance(), Registry.DIMENSION_TYPE_KEY), 0,
                0,
                () -> MinecraftClient.getInstance().getProfiler(),
                MinecraftClient.getInstance().worldRenderer,
                false,
                0L);
    }
}
