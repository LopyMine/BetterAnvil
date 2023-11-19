package net.lopymine.betteranvil.fake;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.registry.*;
import net.minecraft.util.Identifier;
import net.minecraft.world.Difficulty;

import net.lopymine.betteranvil.BetterAnvil;

public class FakeWorld extends ClientWorld {

    private static FakeWorld INSTANCE;

    public FakeWorld() {
        super(
                FakeClientPlayNetworkHandler.getInstance(),
                new Properties(Difficulty.EASY, false, true),
                RegistryKey.of(RegistryKeys.WORLD, new Identifier(BetterAnvil.MOD_ID, "fake")),
                FakeDimension.getEntry(),
                0,
                0,
                () -> MinecraftClient.getInstance().getProfiler(),
                MinecraftClient.getInstance().worldRenderer,
                false,
                0L
        );
    }

    public static FakeWorld getInstance() {
        if (INSTANCE == null){
            INSTANCE = new FakeWorld();
        }
        return INSTANCE;
    }

    public static void onInitialize() {
        INSTANCE = new FakeWorld();
    }
}
