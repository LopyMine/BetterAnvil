package net.lopymine.betteranvil.config.resourcepacks.cmd;

import net.lopymine.betteranvil.config.BetterAnvilConfig;
import net.lopymine.betteranvil.config.resourcepacks.ConfigParser;
import net.lopymine.betteranvil.resourcepacks.ResourcePackType;

import java.util.*;
import org.jetbrains.annotations.Nullable;

public class CMDConfigParser extends ConfigParser<CMDItem, CMDConfigSet> {
    private static final CMDConfigParser INSTANCE = new CMDConfigParser();
    protected CMDConfigParser() {
        super(ResourcePackType.CMD, CMDConfigSet.class, new CMDConfigSet(new LinkedHashSet<>()));
    }

    public static CMDConfigParser getInstance() {
        return INSTANCE;
    }

    public HashMap<String, LinkedHashSet<CMDItem>> getResourcePacksItems(@Nullable List<String> resourcePacks, BetterAnvilConfig config) {
        return parseResourcePacksItems(resourcePacks);
    }

    public LinkedHashSet<CMDItem> parseItemsFromConfig(String configFileName, String path, BetterAnvilConfig config) {
        return parseItemsFromConfig(configFileName, path);
    }
}
