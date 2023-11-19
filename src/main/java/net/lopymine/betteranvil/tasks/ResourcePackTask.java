package net.lopymine.betteranvil.tasks;

import net.minecraft.resource.ResourcePack;

import net.lopymine.betteranvil.config.resourcepacks.cit.CITConfigWriter;
import net.lopymine.betteranvil.config.resourcepacks.cmd.CMDConfigWriter;
import net.lopymine.betteranvil.resourcepacks.*;
import net.lopymine.betteranvil.utils.mixins.ResourcePackAccessor;

import java.util.Set;

public class ResourcePackTask implements Runnable {

    protected final ParsableResourcePack parsableResourcePack;

    public ResourcePackTask(ParsableResourcePack parsableResourcePack) {
        this.parsableResourcePack = parsableResourcePack;
    }

    public Set<ResourcePackType> getTypes() {
        return parsableResourcePack.types();
    }

    public ResourcePack getResourcePack() {
        return parsableResourcePack.resourcePackAccessor().betterAnvil$getResourcePack();
    }

    @Override
    public void run() {
        for (ResourcePackType type : getTypes()) {
            try {
                this.runByType(type);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void runByType(ResourcePackType type) {
        ResourcePackAccessor resourcePackAccessor = parsableResourcePack.resourcePackAccessor();

        switch (type) {
            case CMD ->
                    CMDConfigWriter.getInstance().writeConfig(resourcePackAccessor.betterAnvil$getFile(), resourcePackAccessor.betterAnvil$isZip(), resourcePackAccessor.betterAnvil$isServer());
            case CIT ->
                    CITConfigWriter.getInstance().writeConfig(resourcePackAccessor.betterAnvil$getFile(), resourcePackAccessor.betterAnvil$isZip(), resourcePackAccessor.betterAnvil$isServer());
            case CEM -> {
            }
        }
    }
}
