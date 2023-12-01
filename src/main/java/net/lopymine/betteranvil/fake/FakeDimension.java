package net.lopymine.betteranvil.fake;

import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.dimension.*;

import java.util.OptionalLong;

public class FakeDimension {
    private static DimensionType INSTANCE;

    public static DimensionType getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DimensionType(
                    OptionalLong.of(6000L),
                    true,
                    false,
                    false,
                    true,
                    1.0,
                    true,
                    false,
                    -64,
                    384,
                    384,
                    BlockTags.INFINIBURN_OVERWORLD,
                    DimensionTypes.OVERWORLD_ID,
                    0.0f,
                    new DimensionType.MonsterSettings(
                            false,
                            true,
                            UniformIntProvider.create(0, 7),
                            0
                    )
            );
        }

        return INSTANCE;
    }


    public static RegistryEntry<DimensionType> getEntry() {
        return new FakeRegistryEntry<>(getInstance(), RegistryKeys.DIMENSION_TYPE);
    }
}
