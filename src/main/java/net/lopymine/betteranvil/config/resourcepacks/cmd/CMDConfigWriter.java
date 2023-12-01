package net.lopymine.betteranvil.config.resourcepacks.cmd;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;

import net.lopymine.betteranvil.config.resourcepacks.ConfigWriter;
import net.lopymine.betteranvil.resourcepacks.ResourcePackType;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CMDConfigWriter extends ConfigWriter<CMDItem> {
    private static final CMDConfigWriter INSTANCE = new CMDConfigWriter();
    private static final Set<String> FOLDERS = new HashSet<>(List.of("item", "block"));

    private CMDConfigWriter() {
        super(ResourcePackType.CMD);
    }

    public static CMDConfigWriter getInstance() {
        return INSTANCE;
    }

    @Override
    protected LinkedHashSet<CMDItem> readFiles(Path folderPath) {
        LinkedHashSet<CMDItem> items = new LinkedHashSet<>();
        List<Path> files = new ArrayList<>();

        for (String folder : FOLDERS) {
            Path path = folderPath.resolve(folder);

            if (!Files.exists(path)) {
                continue;
            }

            try {
                List<Path> list = Files.walk(path, 1).filter(p -> p.toString().endsWith(".json")).toList();
                files.addAll(list);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for (Path p : files) {
            try (InputStream inputStream = Files.newInputStream(p); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                JsonReader jsonReader = new JsonReader(reader);
                jsonReader.setLenient(true);
                JsonObject jsonObject = GSON.fromJson(jsonReader, JsonObject.class);
                jsonReader.close();

                JsonArray overrides = jsonObject.getAsJsonArray("overrides");
                if (overrides == null) {
                    continue;
                }

                for (JsonElement element : overrides.asList()) {
                    JsonObject override = element.getAsJsonObject();
                    JsonObject predicate = override.getAsJsonObject("predicate");
                    if (predicate == null) {
                        continue;
                    }

                    JsonElement customModelData = predicate.get("custom_model_data");

                    if (customModelData != null && override.get("model") != null) {
                        String fileName = p.getFileName().toString().replaceAll(".json", "");
                        CMDItem item = new CMDItem(fileName, customModelData.getAsInt());
                        items.add(item);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        return items;
    }
}
