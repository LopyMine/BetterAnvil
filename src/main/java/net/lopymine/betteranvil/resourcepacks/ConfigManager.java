package net.lopymine.betteranvil.resourcepacks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.lopymine.betteranvil.resourcepacks.cit.writers.CITWriter;
import net.lopymine.betteranvil.resourcepacks.cmd.writers.CMDWriter;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;

public class ConfigManager {

    public static final String pathToCITFolder = "/assets/minecraft/optifine/cit/";
    public static final String pathToCITConfigFolder = "config/betteranvil/resourcepacks/";
    public static final String pathToCITServerConfigFolder = "config/betteranvil/resourcepacks/servers/";

    public static final String pathToResourcePacks = "resourcepacks/";
    public static final String pathToConfig = "config/betteranvil/";


    public static final String pathToCMDFolder = "/assets/minecraft/models/";
    public static final String pathToCMDConfigFolder = "config/betteranvil/resourcepacks/custommodeldata/";
    public static final String pathToCMDServerConfigFolder = "config/betteranvil/resourcepacks/custommodeldata/server/";
    public static final String jsonFormat = ".json";

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void writeCITItems(){
        for (String rp : PackManager.getPacks()) {
            if (hasCITFolder(pathToResourcePacks + rp)) {
                MYLOGGER.info(rp + " resource pack has a cit folder!");
                CITWriter.writeConfig(new File(pathToResourcePacks + rp), rp.endsWith(".zip"), false);
            }
        }
    }

    public static void writeCMDItems(){
        for (String rp : PackManager.getPacks()) {
            if (hasCMDFolder(pathToResourcePacks + rp)) {
                MYLOGGER.info(rp + " resource pack has a CMD folder!");
                CMDWriter.writeConfig(new File(pathToResourcePacks + rp), rp.endsWith(".zip"), false);
            }
        }
    }

    public static boolean hasCMDFolder(String path) {
        Path fpath = Paths.get(path.replace(".zip", "") + pathToCMDFolder);

        if(path.endsWith(".zip")) return hasZipCMDFolder(Paths.get(path));

        return Files.isDirectory(fpath);
    }

    public static boolean hasZipCMDFolder(Path path) {
        try (FileSystem zipFS = FileSystems.newFileSystem(path, (ClassLoader) null)) {
            Path citFolder = zipFS.getPath(pathToCMDFolder);
            return Files.isDirectory(citFolder);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean hasCITFolder(String path) {
        Path fpath = Paths.get(path.replace(".zip", "") + pathToCITFolder);

        if(path.endsWith(".zip")) return hasZipCITFolder(Paths.get(path));

        return Files.isDirectory(fpath);
    }

    public static boolean hasZipCITFolder(Path path) {
        try (FileSystem zipFS = FileSystems.newFileSystem(path, (ClassLoader) null)) {
            Path citFolder = zipFS.getPath(pathToCITFolder);
            return Files.isDirectory(citFolder);
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean hasConfig(String path) {
        File file = new File(path);
        return file.exists();
    }

}
