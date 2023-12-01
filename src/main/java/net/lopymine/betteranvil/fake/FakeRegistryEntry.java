package net.lopymine.betteranvil.fake;

import net.minecraft.registry.*;
import net.minecraft.registry.entry.*;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import com.mojang.datafixers.util.Either;

import net.lopymine.betteranvil.BetterAnvil;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public record FakeRegistryEntry<T>(T value, RegistryKey<? extends Registry<T>> key) implements RegistryEntry<T> {
    @Override
    public boolean hasKeyAndValue() {
        return true;
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
    public boolean isIn(TagKey<T> tag) {
        return false;
    }

    @Override
    public boolean matches(Predicate<RegistryKey<T>> predicate) {
        return false;
    }

    @Override
    public Either<RegistryKey<T>, T> getKeyOrValue() {
        return Either.right(this.value);
    }

    @Override
    public Optional<RegistryKey<T>> getKey() {
        return Optional.of(RegistryKey.of(key, BetterAnvil.i("fake_registry_entry")));
    }

    @Override
    public Type getType() {
        return Type.DIRECT;
    }

    @Override
    public boolean ownerEquals(RegistryEntryOwner<T> owner) {
        return true;
    }

    @Override
    public Stream<TagKey<T>> streamTags() {
        return Stream.of();
    }
}
