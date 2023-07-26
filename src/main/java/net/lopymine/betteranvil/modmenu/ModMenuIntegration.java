package net.lopymine.betteranvil.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.HashSet;

public class ModMenuIntegration implements ModMenuApi {
    private final HashSet<String> mods = getMods();
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if(mods.contains("cloth-config")){
            return ModMenuIntegrationScreen::createScreen;
        } else {
            return screen -> new CottonClientScreen(new NoClothConfigScreen(screen));
        }
    }

    private HashSet<String> getMods(){
        HashSet<String> mods = new HashSet<>();

        for(ModContainer mod : FabricLoader.getInstance().getAllMods()){
            mods.add(mod.getMetadata().getId());
        }

        return mods;
    }
}
