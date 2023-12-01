package net.lopymine.betteranvil.fake;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.*;
import net.minecraft.entity.damage.*;
import net.minecraft.network.*;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionType;

import com.mojang.serialization.Lifecycle;

import net.lopymine.betteranvil.BetterAnvil;

import java.time.Duration;
import java.util.Optional;
import java.util.stream.Stream;

public class FakeClientPlayNetworkHandler extends ClientPlayNetworkHandler {
    private static final Registry<DimensionType> FAKE_DIMENSION_TYPE_REGISTRY = new SimpleRegistry<>(RegistryKeys.DIMENSION_TYPE, Lifecycle.stable());
    private static final Registry<Biome> FAKE_BIOME_REGISTRY = new SimpleDefaultedRegistry<>("fake", RegistryKeys.BIOME, Lifecycle.stable(), true) {
        @Override
        public RegistryEntry.Reference<Biome> entryOf(RegistryKey<Biome> key) {
            return null;
        }
    };

    private static final DynamicRegistryManager.Immutable FAKE_REGISTRY_MANAGER = new DynamicRegistryManager.Immutable() {
        private final FakeRegistry<DamageType> damageTypes = new FakeRegistry<>(RegistryKeys.DAMAGE_TYPE, BetterAnvil.i("fake_damage"),
                new DamageType("", DamageScaling.NEVER, 0));

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public Optional<Registry> getOptional(RegistryKey key) {
            var x = Registries.REGISTRIES.get(key);
            if (x != null) {
                return Optional.of(x);
            } else if (RegistryKeys.DAMAGE_TYPE.equals(key)) {
                return Optional.of(damageTypes);
            } else if (RegistryKeys.BIOME.equals(key)) {
                return Optional.of(FAKE_BIOME_REGISTRY);
            } else if (RegistryKeys.DIMENSION_TYPE.equals(key)) {
                return Optional.of(FAKE_DIMENSION_TYPE_REGISTRY);
            }

            return Optional.empty();
        }

        @Override
        public Stream<Entry<?>> streamAllRegistries() {
            return Stream.empty();
        }
    };

    private static FakeClientPlayNetworkHandler INSTANCE;

    static {
        Registry.register(FAKE_DIMENSION_TYPE_REGISTRY, BetterAnvil.i("fake_dimension"), FakeDimension.getInstance());
    }

    private FakeClientPlayNetworkHandler() {
        super(MinecraftClient.getInstance(), new ClientConnection(NetworkSide.CLIENTBOUND), new ClientConnectionState(MinecraftClient.getInstance().getGameProfile(), MinecraftClient.getInstance().getTelemetryManager().createWorldSession(true, Duration.ZERO, null), FAKE_REGISTRY_MANAGER.toImmutable(), FeatureSet.empty(), "", new ServerInfo("", "", ServerInfo.ServerType.OTHER), null));
    }

    public static FakeClientPlayNetworkHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FakeClientPlayNetworkHandler();
        }
        return INSTANCE;
    }

    @Override
    public DynamicRegistryManager.Immutable getRegistryManager() {
        return FAKE_REGISTRY_MANAGER;
    }
}