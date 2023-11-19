package net.lopymine.betteranvil.tasks;

import net.lopymine.betteranvil.resourcepacks.ParsableResourcePack;

import java.util.List;

public class ResourcePackTaskManager {

    public static void runTasks(List<ParsableResourcePack> parsableResourcePacks) {
        for (ParsableResourcePack parsableResourcePack : parsableResourcePacks) {
            if (parsableResourcePack.types().isEmpty()) continue;

            ResourcePackTask task = new ResourcePackTask(parsableResourcePack);
            runTask(task);
        }
    }

    public static void runTask(ResourcePackTask task) {
        Thread thread = new Thread(task, "Resource Pack Task#" + task.getResourcePack().getName());
        thread.start();
    }
}
