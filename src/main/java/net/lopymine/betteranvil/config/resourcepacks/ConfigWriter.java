package net.lopymine.betteranvil.config.resourcepacks;

import com.google.gson.*;

import net.lopymine.betteranvil.resourcepacks.ResourcePackType;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.*;
import java.util.LinkedHashSet;

import static net.lopymine.betteranvil.BetterAnvil.LOGGER;
import static net.lopymine.betteranvil.config.resourcepacks.ResourcePackConfigsManager.*;

public abstract class ConfigWriter<I> {
    public static final Gson GSON = new GsonBuilder().setLenient().setPrettyPrinting().create();
    protected final ResourcePackType type;

    protected ConfigWriter(ResourcePackType type) {
        this.type = type;
    }

    public void writeConfig(File file, boolean isZip, boolean isServer) {
        LOGGER.info("[{} Writer] Start writing config for {}", type.name(), file.getName());

        ConfigSet<I> set = new ConfigSet<>(openFileAndRead(file, isZip));

        if (set.getItems().isEmpty()) {
            LOGGER.error("[{} Writer] Failed to find items config {}", type.name(), file.getName());
            return;
        }

        String path = isServer ? getServerConfigPath(type) : getConfigPath(type);

        String json = GSON.toJson(set);
        File configFolder = new File(path);

        if (configFolder.exists()) {
            try (FileWriter writer = new FileWriter(path + file.getName().replace(".zip", "") + JSON_FORMAT)) {
                writer.write(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        if (configFolder.mkdirs()) {
            try (FileWriter writer = new FileWriter(path + file.getName().replace(".zip", "") + JSON_FORMAT)) {
                writer.write(json);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            LOGGER.error("[{} Writer] Failed to create Better Anvil config folder!", type.name());
        }
    }


    public LinkedHashSet<I> openFileAndRead(File file, boolean isZip) {
        String path = getConfigFolderPath(type);

        if (!isZip) {
            return readFiles(Path.of(file.toPath() + path));
        }

        try (FileSystem zipFS = FileSystems.newFileSystem(file.toPath(), (ClassLoader) null)) {
            return readFiles(zipFS.getPath(path));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new LinkedHashSet<>();
    }

    protected abstract LinkedHashSet<I> readFiles(Path folderPath);
}
