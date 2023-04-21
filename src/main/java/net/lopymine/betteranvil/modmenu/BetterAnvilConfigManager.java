package net.lopymine.betteranvil.modmenu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.lopymine.betteranvil.modmenu.interfaces.PositionButton;
import net.lopymine.betteranvil.modmenu.interfaces.ResourcePackJsonWriting;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BetterAnvilConfigManager {
    //
    public PositionButton position = PositionButton.RIGHT;
    public ResourcePackJsonWriting start = ResourcePackJsonWriting.LAUNCH;
    private static final File FILE_PATH = new File(FabricLoader.getInstance().getConfigDir().toFile(), "betteranvil.json");
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    public static final BetterAnvilConfigManager INSTANCE = read();
    public static BetterAnvilConfigManager create(){

        BetterAnvilConfigManager betterAnvilConfig = new BetterAnvilConfigManager();

        String json = gson.toJson(betterAnvilConfig);

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(json);
        } catch (IOException ignored) {
        }
        return betterAnvilConfig;
    }
    public void write(){
        String json = gson.toJson(this, BetterAnvilConfigManager.class);
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static BetterAnvilConfigManager read(){
        if(!FILE_PATH.exists()) return create();

        try (FileReader reader = new FileReader(FILE_PATH)) {
            return gson.fromJson(reader, BetterAnvilConfigManager.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return create();
    }

}