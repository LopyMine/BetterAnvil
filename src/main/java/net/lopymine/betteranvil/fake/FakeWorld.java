package net.lopymine.betteranvil.fake;

import net.lopymine.betteranvil.BetterAnvil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.Difficulty;

public class FakeWorld extends ClientWorld {

    private static FakeWorld instance;

    public static FakeWorld getInstance() {
        if (instance == null) instance = new FakeWorld();
        return instance;
    }


    public FakeWorld() {
        super(FakeClientPlayNetworkHandler.getInstance(), new Properties(Difficulty.EASY, false, true), RegistryKey.of(RegistryKeys.WORLD, new Identifier(BetterAnvil.ID, "") ), new FakeRegistryEntry<>(FakeDimension.getINSTANCE(),RegistryKeys.DIMENSION_TYPE), 0, 0, () -> MinecraftClient.getInstance().getProfiler(), MinecraftClient.getInstance().worldRenderer, false, 0L);
    }
}
