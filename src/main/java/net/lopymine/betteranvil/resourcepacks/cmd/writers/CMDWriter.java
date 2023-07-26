package net.lopymine.betteranvil.resourcepacks.cmd.writers;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import net.lopymine.betteranvil.resourcepacks.ConfigManager;
import net.lopymine.betteranvil.resourcepacks.cmd.CMDCollection;
import net.lopymine.betteranvil.resourcepacks.cmd.CMDItem;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.stream.Stream;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;
import static net.lopymine.betteranvil.resourcepacks.ConfigManager.*;

public class CMDWriter {

    private static final Gson cmd_gson = new GsonBuilder().setLenient().setPrettyPrinting().create();

    public static void writeConfig(File file, boolean isZip, boolean isServer) {
        CMDCollection dataCollection = new CMDCollection(getItems(file,isZip,isServer));

        if(dataCollection.getItems().isEmpty()){
            MYLOGGER.error("Failed to create CMD config for " + file.getName());
            return;
        }

        String path = pathToCMDConfigFolder;

        if(isServer){
            path = pathToCMDServerConfigFolder;
        }

        String json = cmd_gson.toJson(dataCollection);
        File dir = new File(path);
        if(dir.exists()){
            try (FileWriter writer = new FileWriter(path + file.getName().replace(".zip", "") + jsonFormat)) {
                writer.write(json);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }
        if(dir.mkdirs()){
            try (FileWriter writer = new FileWriter(path + file.getName().replace(".zip", "") + jsonFormat)) {
                writer.write(json);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            MYLOGGER.warn("Failed to create Better Anvil config folder!");
        }
    }

    public static LinkedHashSet<CMDItem> getItems(File file, boolean isZip, boolean isServer) {

        if(!isZip){
            return readFile(Path.of(file.toPath() + pathToCMDFolderItem),Path.of(file.toPath() + pathToCMDFolderBlock), isServer);
        }

        try (FileSystem zipFS = FileSystems.newFileSystem(file.toPath(), (ClassLoader) null)) {
            return readFile(zipFS.getPath(pathToCMDFolderItem),zipFS.getPath(pathToCMDFolderBlock), isServer);

        } catch (IOException e) {
            e.printStackTrace();

        }
        return new LinkedHashSet<>();
    }

    private static LinkedHashSet<CMDItem> readFile(Path item,Path block, boolean isServer) {
        LinkedHashSet<CMDItem> cmdItems = new LinkedHashSet<>();
        List<Path> packFolders = new ArrayList<>();

        packFolders.add(item);
        packFolders.add(block);

        for(Path path : packFolders){
            try{
                Files.walk(path,1).filter(path1 -> path1.toString().endsWith(".json")).forEach(jsonFiles -> {
                    try (InputStream inputStream = Files.newInputStream(jsonFiles); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                        JsonReader jsonReader = new JsonReader(reader);
                        jsonReader.setLenient(true);
                        JsonObject jsonObject = cmd_gson.fromJson(jsonReader, JsonObject.class);

                        jsonReader.close();
                        JsonArray overrides = jsonObject.getAsJsonArray("overrides");
                        if (overrides != null) {
                            for (JsonElement o : overrides.getAsJsonArray()) {
                                JsonObject override = o.getAsJsonObject();
                                JsonObject predicate = override.getAsJsonObject("predicate");
                                if (predicate != null) {
                                    JsonElement customModelData = predicate.get("custom_model_data");
                                    if (customModelData != null && override.get("model") != null) {
                                        CMDItem customModelDataItem = new CMDItem(jsonFiles.getFileName().toString().replace(".json", ""), customModelData.getAsInt());

                                        cmdItems.add(customModelDataItem);
                                    }
                                }
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
            } catch (IOException e){
            }
        }

        //System.out.println(packFolders);
        //for(Path folder : packFolders){
        //    File fileFolder = folder.toFile();
        //    File[] fileStream = fileFolder.listFiles();
        //    System.out.println(fileStream);
        //    if(fileStream != null){
        //        Arrays.stream(fileStream).filter(path -> path.toString().endsWith(".json")).forEach(file -> files.add(file.toPath()));
        //    }
        //}
//
//
        //if(files.isEmpty()){
        //    System.out.println("files is empty");
        //    return cmdItems;
        //}
        //files.forEach(jsonFiles -> {
//
        //});
        return cmdItems;
    }
}


