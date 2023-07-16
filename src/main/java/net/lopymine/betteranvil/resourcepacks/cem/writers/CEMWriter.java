package net.lopymine.betteranvil.resourcepacks.cem.writers;

import com.google.gson.*;
import net.lopymine.betteranvil.resourcepacks.cem.CEMCollection;
import net.lopymine.betteranvil.resourcepacks.cem.CEMItem;
import net.lopymine.betteranvil.resourcepacks.properties.CEMHandler;

import java.io.*;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.stream.Stream;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;
import static net.lopymine.betteranvil.resourcepacks.ConfigManager.*;

public class CEMWriter {

    private static final Gson cem_gson = new GsonBuilder().setLenient().setPrettyPrinting().create();
    public static void writeConfig(File file, boolean isZip, boolean isServer) {
        CEMCollection dataCollection = new CEMCollection(getItems(file,isZip, isServer));

        if(dataCollection.getItems().isEmpty()){
            return;
        }

        String path = pathToCEMConfigFolder;

        if(isServer){
            path = pathToCEMServerConfigFolder;
        }

        String json = cem_gson.toJson(dataCollection);
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

    public static LinkedHashSet<CEMItem> getItems(File file, boolean isZip, boolean isServer) {

        String p = pathToCEMFolder;

        if(!isZip){
            return readFile(Path.of(file.toPath() + p), isServer);
        }

        try (FileSystem zipFS = FileSystems.newFileSystem(file.toPath(), (ClassLoader) null)) {
            Path citFolder = zipFS.getPath(p);

            return readFile(citFolder,isServer);

        } catch (IOException e) {
            e.printStackTrace();

        }
        return new LinkedHashSet<>();
    }

    private static LinkedHashSet<CEMItem> readFile(Path cmdPath, boolean isServer) {
        LinkedHashSet<CEMItem> cemItems = new LinkedHashSet<>();
        Stream<Path> files;

        try{
            files = Files.walk(cmdPath).filter(path -> path.toString().endsWith(".properties"));
        } catch (IOException e) {
            e.printStackTrace();

            return cemItems;
        }

        files.forEach(jsonFiles -> {
            try (InputStream inputStream = Files.newInputStream(jsonFiles); BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if(CEMHandler.isName(line)){
                        CEMItem item = new CEMItem(jsonFiles.getFileName().toString().replace(".properties",""), CEMHandler.getName(line));
                        cemItems.add(item);
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }});

        return cemItems;

    }
}
