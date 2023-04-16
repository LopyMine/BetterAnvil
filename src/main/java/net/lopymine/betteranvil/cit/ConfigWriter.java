package net.lopymine.betteranvil.cit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.lopymine.betteranvil.cit.writers.FolderWriter;
import net.lopymine.betteranvil.cit.writers.ZipWriter;

import java.nio.file.Path;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;
import static net.lopymine.betteranvil.cit.ConfigParser.*;
public class ConfigWriter {
    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void writePackConfig() {
        for (String rp : ConfigParser.getResourcePackNames().stream().toList()) {
            if(rp.endsWith(".zip")){
                if (ZipWriter.hasCitZipFolder(Path.of(pathToResourcePacks + rp))) {
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
}
