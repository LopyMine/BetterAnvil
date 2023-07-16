package net.lopymine.betteranvil.fake;

import com.mojang.datafixers.util.Either;
import net.lopymine.betteranvil.BetterAnvil;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryOwner;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record FakeRegistryEntry<T>(T value, RegistryKey<? extends Registry<T>> key) implements RegistryEntry<T> {
    @Override
    public boolean hasKeyAndValue() {
        return false;
    }

    @Override
    public boolean matchesId(Identifier id) {
        return false;
    }

    @Override
    public boolean matchesKey(RegistryKey<T> key) {
        return false;
    }

    @Override
    public boolean matches(Predicate<RegistryKey<T>> predicate) {
        return false;
    }

    @Override
    public boolean isIn(TagKey<T> tag) {
        return false;
    }

    @Override
    public Stream<TagKey<T>> streamTags() {
        return null;
    }

    @Override
    public Either<RegistryKey<T>, T> getKeyOrValue() {
        return null;
    }

    @Override
    public Optional<RegistryKey<T>> getKey() {
        return Optional.of(RegistryKey.of(key,new Identifier(BetterAnvil.ID, "")));
    }

    @Override
    public Type getType() {
        return null;
    }

    @Override
    public boolean ownerEquals(RegistryEntryOwner<T> owner) {
        return false;
    }
}
