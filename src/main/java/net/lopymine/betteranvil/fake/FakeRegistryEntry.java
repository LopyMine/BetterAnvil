package net.lopymine.betteranvil.fake;

import com.mojang.datafixers.util.Either;
import net.lopymine.betteranvil.BetterAnvil;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;

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
        return Type.DIRECT;
    }

    @Override
    public boolean matchesRegistry(Registry<T> registry) {
        return false;
    }



}
