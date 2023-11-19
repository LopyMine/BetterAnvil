package net.lopymine.betteranvil.resourcepacks;

import net.lopymine.betteranvil.utils.mixins.ResourcePackAccessor;

import java.util.Set;

public record ParsableResourcePack(ResourcePackAccessor resourcePackAccessor, Set<ResourcePackType> types) {
}
