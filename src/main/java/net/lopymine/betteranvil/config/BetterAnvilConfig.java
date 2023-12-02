package net.lopymine.betteranvil.config;

import com.google.gson.*;

import net.fabricmc.loader.api.FabricLoader;

import net.lopymine.betteranvil.config.enums.*;

import java.io.*;

import static net.lopymine.betteranvil.BetterAnvil.LOGGER;
import static net.lopymine.betteranvil.config.resourcepacks.ResourcePackConfigsManager.PATH_TO_CONFIG;

public class BetterAnvilConfig {

    private static final File FILE_PATH = new File(FabricLoader.getInstance().getConfigDir().toFile(), "better-anvil.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public Overwriting overwritingEnum = Overwriting.CHECKBOX;
    public ButtonPositions positionEnum = ButtonPositions.RIGHT;
    public ButtonTextures buttonTextureEnum = ButtonTextures.RENAME;
    public FavoriteMenuPositions favoriteMenuPositionEnum = FavoriteMenuPositions.AUTO;
    public boolean enabledCMD = true;
    public boolean enabledCIT = true;
    public boolean enabledViewResourcePacks = false;
    public boolean isCheckBoxChecked = false;
    public boolean isDarkMode = false;

    public Renames renamesCountEnum = Renames.ONE;
    public int customRenamesCount = 1;

    public int spacing = 30;

    public static BetterAnvilConfig getInstance() {
        return read();
    }

    public static BetterAnvilConfig create() {
        BetterAnvilConfig betterAnvilConfig = new BetterAnvilConfig();

        String json = GSON.toJson(betterAnvilConfig);

        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return betterAnvilConfig;
    }

    private static BetterAnvilConfig read() {
        if (!FILE_PATH.exists()) return create();

        try (FileReader reader = new FileReader(FILE_PATH)) {
            return GSON.fromJson(reader, BetterAnvilConfig.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return create();
    }

    public static void checkConfigFolder() {
        File configFolder = new File(PATH_TO_CONFIG);
        if (!configFolder.exists()) {
            if (configFolder.mkdirs()) {
                LOGGER.info("Created Better Anvil config folder!");
            } else {
                LOGGER.warn("Failed to create Better Anvil config folder!");
            }
        }
    }

    public void write() {
        String json = GSON.toJson(this, BetterAnvilConfig.class);
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
