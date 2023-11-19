package net.lopymine.betteranvil.fake;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.damage.*;
import net.minecraft.network.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
import net.minecraft.registry.entry.RegistryEntry.Reference;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import com.mojang.serialization.Lifecycle;

import net.lopymine.betteranvil.BetterAnvil;

import java.time.Duration;
import java.util.Optional;
import java.util.stream.Stream;

public class FakeClientPlayNetworkHandler extends ClientPlayNetworkHandler {
    private static FakeClientPlayNetworkHandler INSTANCE;
    private final RegistryEntryOwner<Biome> fakeBiomeRegistryOwner = new RegistryEntryOwner<>(){};
    private final Registry<Biome> fakeBiomeRegistry = new SimpleDefaultedRegistry<>("fake_player", RegistryKeys.BIOME, Lifecycle.stable(), true) {
        @Override
        public RegistryEntry.Reference<Biome> entryOf(RegistryKey<Biome> key) {
            return Reference.standAlone(fakeBiomeRegistryOwner, key);
        }
    };
    private final DynamicRegistryManager cursedRegistryManager = new DynamicRegistryManager.Immutable() {
        private final FakeRegistry<DamageType> damageTypes = new FakeRegistry<>(RegistryKeys.DAMAGE_TYPE, new Identifier(BetterAnvil.MOD_ID, "fake_damage"), new DamageType("", DamageScaling.NEVER, 0));

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public Optional<Registry> getOptional(RegistryKey key) {
            var x = Registries.REGISTRIES.get(key);
            if (x != null) {
                return Optional.of(x);
            } else if (RegistryKeys.DAMAGE_TYPE.equals(key)) {
                return Optional.of(damageTypes);
            } else if (RegistryKeys.BIOME.equals(key)) {
                return Optional.of(fakeBiomeRegistry);
            }

            return Optional.empty();
        }

        @Override
        public Stream<Entry<?>> streamAllRegistries() {
            return Stream.empty();
        }
    };

    private FakeClientPlayNetworkHandler() {
        super(MinecraftClient.getInstance(), null, new ClientConnection(NetworkSide.CLIENTBOUND), MinecraftClient.getInstance().getCurrentServerEntry(), MinecraftClient.getInstance().getSession().getProfile(), MinecraftClient.getInstance().getTelemetryManager().createWorldSession(false, Duration.ofSeconds(0), ""));
    }

    public static FakeClientPlayNetworkHandler getInstance() {
        if (INSTANCE == null){
            INSTANCE = new FakeClientPlayNetworkHandler();
        }
        return INSTANCE;
    }

    public static void onInitialize() {
        INSTANCE = new FakeClientPlayNetworkHandler();
    }

    @Override
    public DynamicRegistryManager getRegistryManager() {
        return cursedRegistryManager;
    }
}
