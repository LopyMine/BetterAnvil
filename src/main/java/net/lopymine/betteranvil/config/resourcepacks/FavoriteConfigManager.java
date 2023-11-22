package net.lopymine.betteranvil.config.resourcepacks;

import com.google.gson.*;
import net.minecraft.item.ItemStack;

import net.lopymine.betteranvil.resourcepacks.ResourcePackType;

import java.io.*;
import java.util.LinkedHashSet;

import static net.lopymine.betteranvil.BetterAnvil.LOGGER;
import static net.lopymine.betteranvil.config.BetterAnvilConfig.checkConfigFolder;
import static net.lopymine.betteranvil.config.resourcepacks.ResourcePackConfigsManager.JSON_FORMAT;
import static net.lopymine.betteranvil.config.resourcepacks.ResourcePackConfigsManager.PATH_TO_CONFIG;

public abstract class FavoriteConfigManager<I, K extends ConfigSet<I>> {
    protected static final Gson GSON = new GsonBuilder().setLenient().setPrettyPrinting().create();
    protected final ResourcePackType type;
    protected final Class<K> cl;
    protected final K valueIfNew;

    protected FavoriteConfigManager(ResourcePackType type, Class<K> cl, K valueIfNew) {
        this.type = type;
        this.cl = cl;
        this.valueIfNew = valueIfNew;
    }

    protected K readConfig() {
        checkConfigFolder();

        try (FileReader reader = new FileReader(getPath())) {
            return GSON.fromJson(reader, cl);
        } catch (Exception s) {
            return createConfig();
        }
    }

    protected K createConfig() {
        checkConfigFolder();

        String json = GSON.toJson(valueIfNew);
        try (FileWriter writer = new FileWriter(getPath())) {
            writer.write(json);
            LOGGER.info("[{}] Created favorite config!", type.name());
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.info("[{}] Failed to create favorite config!", type.name());
        }

        return valueIfNew;
    }

    public LinkedHashSet<I> getItems() {
        return readConfig().getItems();
    }

    public LinkedHashSet<I> getWithItem(ItemStack itemStack) {
        return getWithItem(readConfig().getItems(), itemStack);
    }

    public abstract LinkedHashSet<I> getWithItem(LinkedHashSet<I> list, ItemStack itemStack);

    public abstract LinkedHashSet<I> getResourcePacksItems(LinkedHashSet<I> list, LinkedHashSet<String> resourcePacks);

    public void removeItem(I item) {
        checkConfigFolder();

        K k = readConfig();
        LinkedHashSet<I> items = k.getItems();
        items.remove(item);

        String json = GSON.toJson(k);
        try (FileWriter writer = new FileWriter(getPath())) {
            writer.write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addItem(I item) {
        checkConfigFolder();

        K k = readConfig();
        LinkedHashSet<I> items = k.getItems();
        items.add(item);

        String json = GSON.toJson(k);
        try (FileWriter writer = new FileWriter(getPath())) {
            writer.write(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected String getPath() {
        return PATH_TO_CONFIG + type.name() + "-favorite" + JSON_FORMAT;
    }
}
