package net.lopymine.betteranvil.resourcepacks.custommodeldata.writers;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import net.lopymine.betteranvil.resourcepacks.custommodeldata.CMDCollection;
import net.lopymine.betteranvil.resourcepacks.custommodeldata.CMDItem;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;
import static net.lopymine.betteranvil.resourcepacks.ConfigManager.*;

public class CMDWriter {

    private static final Gson cmd_gson = new GsonBuilder().setLenient().setPrettyPrinting().create();

    public static void writeConfig(File file, boolean isZip, boolean isServer) {
        CMDCollection dataCollection = new CMDCollection(getCustomModelDataItems(file,isZip,isServer));

        if(dataCollection.getItems().isEmpty()){
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

    private static ArrayList<CMDItem> getCustomModelDataItems(File file, boolean isZip, boolean isServer) {

        String p = pathToCMDFolder;

        if(!isZip){
            return readFile(Path.of(file.toPath() + p));
        }

        try (FileSystem zipFS = FileSystems.newFileSystem(file.toPath(), (ClassLoader) null)) {
            Path citFolder = zipFS.getPath(p);

            return readFile(citFolder);

        } catch (IOException e) {
            e.printStackTrace();

        }
        return new ArrayList<>();
    }

    private static ArrayList<CMDItem> readFile(Path cmdPath) {
        ArrayList<CMDItem> customModelDataItems = new ArrayList<>();

        try{
            Files.walk(cmdPath).filter(path -> path.toString().endsWith(".json")).forEach(jsonFiles -> {
                try (InputStream inputStream = Files.newInputStream(jsonFiles); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                    JsonReader jsonReader = new JsonReader(reader);
                    jsonReader.setLenient(true);
                    JsonObject jsonObject = cmd_gson.fromJson(jsonReader, JsonObject.class);

                    jsonReader.close();
                    JsonArray overrides = jsonObject.getAsJsonArray("overrides");
                    if(overrides != null){

                        for(JsonElement o : overrides.getAsJsonArray()){
                            JsonObject override = o.getAsJsonObject();
                            JsonObject predicate = override.getAsJsonObject("predicate");
                            if(predicate != null){
                                JsonElement customModelData = predicate.get("custom_model_data");
                                if(customModelData != null && override.get("model") != null){
                                    CMDItem customModelDataItem = new CMDItem(jsonFiles.getFileName().toString().replace(".json",""), customModelData.getAsInt());

                                    if(!customModelDataItems.contains(customModelDataItem)){
                                        customModelDataItems.add(customModelDataItem);
                                    }
                                }
                            }
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return customModelDataItems;

    }
}


