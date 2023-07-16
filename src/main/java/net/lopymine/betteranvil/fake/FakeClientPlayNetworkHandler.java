package net.lopymine.betteranvil.fake;

import com.mojang.serialization.Lifecycle;
import net.lopymine.betteranvil.BetterAnvil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.damage.DamageScaling;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.NetworkSide;
import net.minecraft.registry.*;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.biome.Biome;

import java.time.Duration;
import java.util.Optional;
import java.util.stream.Stream;

public class FakeClientPlayNetworkHandler extends ClientPlayNetworkHandler {
    private static FakeClientPlayNetworkHandler INSTANCE;

    private FakeClientPlayNetworkHandler() {
        super(MinecraftClient.getInstance(), null, new ClientConnection(NetworkSide.CLIENTBOUND), MinecraftClient.getInstance().getCurrentServerEntry(),MinecraftClient.getInstance().getSession().getProfile(),MinecraftClient.getInstance().getTelemetryManager().createWorldSession(false, Duration.ofSeconds(0)));
    }

    private final Registry<Biome> cursedBiomeRegistry = new SimpleDefaultedRegistry<>("dummy", RegistryKeys.BIOME, Lifecycle.stable(), true) {
        @Override
        public RegistryEntry.Reference<Biome> entryOf(RegistryKey<Biome> key) {
            return null;
        }
    };

    private final DynamicRegistryManager cursedRegistryManager = new DynamicRegistryManager.Immutable() {
        private final FakeRegistry<DamageType> damageTypes = new FakeRegistry<>(RegistryKeys.DAMAGE_TYPE,new Identifier(BetterAnvil.ID,""),new DamageType("", DamageScaling.NEVER, 0));

        @SuppressWarnings({"unchecked", "rawtypes"})
        @Override
        public Optional<Registry> getOptional(RegistryKey key) {
            var x = Registries.REGISTRIES.get(key);
            if (x != null) {
                return Optional.of(x);
            } else if (RegistryKeys.DAMAGE_TYPE.equals(key)) {
                return Optional.of(damageTypes);
            } else if (RegistryKeys.BIOME.equals(key)) {
                return Optional.of(cursedBiomeRegistry);
            }

            return Optional.empty();
        }

        @Override
        public Stream<Entry<?>> streamAllRegistries() {
            return Stream.empty();
        }
    };
    public static FakeClientPlayNetworkHandler getInstance() {
        if (INSTANCE == null) INSTANCE = new FakeClientPlayNetworkHandler();
        return INSTANCE;
    }

    @Override
    public DynamicRegistryManager getRegistryManager() {
        return cursedRegistryManager;
    }
}
