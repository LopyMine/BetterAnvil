package net.lopymine.betteranvil.cit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.lopymine.betteranvil.cit.writers.FolderWriter;
import net.lopymine.betteranvil.cit.writers.ZipWriter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackManager;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;

public class ConfigWriter {

    private static final String pathToConfigFolder = "config/betteranvil/";
    private static final String jsonFormat = ".json";
    private static final String pathToCitFolder = "/assets/minecraft/optifine/cit/";
    private static final String pathToResourcePacks = "resourcepacks/";

    static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void createConfig() {
        MinecraftClient mc = MinecraftClient.getInstance();
        ResourcePackManager resourcePackManager = mc.getResourcePackManager();

        Collection<String> resourcePackCollection = resourcePackManager.getEnabledNames();
        for (String rp : clearNames(resourcePackCollection.stream().toList())) {
            Path pathToResourcePack = Path.of(pathToResourcePacks + rp);
            if(rp.endsWith(".zip")){
                if (ZipWriter.hasCitZipFolder(pathToResourcePack)) {
                    MYLOGGER.info(rp + " resource pack has a cit folder!");
                    ZipWriter.writeConfig(rp, gson);
                }
            } else {
                if(FolderWriter.hasCitFolder(pathToResourcePacks + rp + pathToCitFolder)){
                    MYLOGGER.info(rp + " resource pack has a cit folder!");
                    FolderWriter.writeConfig(rp, gson);
                }
            }
        }
    }

    public static List<String> clearNames(List<String> names) {
        List<String> newNames = new ArrayList<>();
        for (int i = 0; i < names.size(); i++) {
            if (names.get(i).startsWith("file/")) {
                newNames.add(names.get(i).replaceAll("file/", ""));
                continue;
            }
        }
        return newNames;
    }

}
