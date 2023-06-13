package net.lopymine.betteranvil.modmenu;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;
import net.lopymine.betteranvil.modmenu.enums.CITButtonTexture;
import net.lopymine.betteranvil.modmenu.enums.PositionButton;
import net.lopymine.betteranvil.modmenu.enums.ResourcePackJsonWriting;
import net.lopymine.betteranvil.modmenu.enums.ResourcePackParserVersion;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class BetterAnvilConfigManager {

    public PositionButton POSITION = PositionButton.RIGHT;
    public ResourcePackParserVersion PARSER_VERSION = ResourcePackParserVersion.V2;
    public Integer BUTTON_HEIGHT = 30;
    public CITButtonTexture BUTTON_TEXTURE = CITButtonTexture.THEME;
    public ResourcePackJsonWriting START = ResourcePackJsonWriting.LAUNCH;
    public boolean CUSTOM_MODEL_DATA_SUPPORT = true;
    public int SHIFT_KEY = 340;
    public int CTRL_KEY = 341;
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
