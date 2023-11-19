package net.lopymine.betteranvil.config.resourcepacks;

import com.google.gson.*;

import net.lopymine.betteranvil.resourcepacks.ResourcePackType;

import java.io.File;
import java.nio.file.*;

public class ResourcePackConfigsManager {

    public static final String PATH_TO_CIT_FOLDER = "/assets/minecraft/optifine/cit/";
    public static final String PATH_TO_CIT_CONFIG_FOLDER = "config/betteranvil/resourcepacks/";
    public static final String PATH_TO_CIT_SERVER_CONFIG_FOLDER = "config/betteranvil/resourcepacks/servers/";

    public static final String PATH_TO_CEM_FOLDER = "/assets/minecraft/optifine/random/entity/";
    public static final String PATH_TO_CEM_CONFIG_FOLDER = "config/betteranvil/resourcepacks/cem/";
    public static final String PATH_TO_CEM_SERVER_CONFIG_FOLDER = "config/betteranvil/resourcepacks/servers/cem/";

    public static final String PATH_TO_CMD_FOLDER = "/assets/minecraft/models/";
    public static final String PATH_TO_CMD_CONFIG_FOLDER = "config/betteranvil/resourcepacks/cmd/";
    public static final String PATH_TO_CMD_SERVER_CONFIG_FOLDER = "config/betteranvil/resourcepacks/servers/cmd/";

    public static final String PATH_TO_RESOURCE_PACKS = "resourcepacks/";
    public static final String PATH_TO_CONFIG = "config/betteranvil/";

    public static final String JSON_FORMAT = ".json";

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static boolean hasPath(File file, String path) {
        if (file.getPath().endsWith(".zip")) {
            return hasZipPath(file, path);
        }
        return Files.isDirectory(Path.of(file.getPath() + path));
    }

    public static boolean hasZipPath(File file, String path) {
        try (FileSystem zipFS = FileSystems.newFileSystem(file.toPath(), (ClassLoader) null)) {
            return Files.isDirectory(zipFS.getPath(path));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static boolean hasConfig(String path) {
        return new File(path).exists();
    }

    public static String getConfigFolderPath(ResourcePackType type) {
        return switch (type) {
            case CEM -> PATH_TO_CEM_FOLDER;
            case CIT -> PATH_TO_CIT_FOLDER;
            case CMD -> PATH_TO_CMD_FOLDER;
        };
    }

    public static String getConfigPath(ResourcePackType type) {
        return switch (type) {
            case CEM -> PATH_TO_CEM_CONFIG_FOLDER;
            case CIT -> PATH_TO_CIT_CONFIG_FOLDER;
            case CMD -> PATH_TO_CMD_CONFIG_FOLDER;
        };
    }

    public static String getServerConfigPath(ResourcePackType type) {
        return switch (type) {
            case CEM -> PATH_TO_CEM_SERVER_CONFIG_FOLDER;
            case CIT -> PATH_TO_CIT_SERVER_CONFIG_FOLDER;
            case CMD -> PATH_TO_CMD_SERVER_CONFIG_FOLDER;
        };
    }
}
