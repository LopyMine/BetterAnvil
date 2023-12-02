package net.lopymine.betteranvil.resourcepacks;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePack;

import net.lopymine.betteranvil.config.BetterAnvilConfig;
import net.lopymine.betteranvil.tasks.ResourcePackTaskManager;
import net.lopymine.betteranvil.utils.ResourcePackUtils;

import java.util.List;
import java.util.stream.Stream;

import static net.lopymine.betteranvil.BetterAnvil.LOGGER;

public class ResourcePackManager {

    public static void startWriting(BetterAnvilConfig config) {
        List<ResourcePack> list = MinecraftClient.getInstance()
                .getResourcePackManager()
                .getEnabledProfiles()
                .stream()
                .filter(profile -> profile.getName().startsWith("file/"))
                .flatMap(profile -> Stream.of(profile.createResourcePack()))
                .toList();

        startWriting(list, config);
    }

    public static void startWriting(List<ResourcePack> list, BetterAnvilConfig config) {
        if (list.isEmpty()) {
            return;
        }

        LOGGER.info("Start writing resource packs to json configs...");

        List<ParsableResourcePack> parsableResourcePacks = ResourcePackUtils.convertList(list, config);
        ResourcePackTaskManager.runTasks(parsableResourcePacks);
    }
}
