package net.lopymine.betteranvil.modmenu;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.ModContainer;

import java.util.ArrayList;

public class ModMenuIntegration implements ModMenuApi {
    private final ArrayList<String> mods = getMods();
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        if(mods.contains("cloth-config")){
            return ModMenuIntegrationConfig::createScreen;
        } else {
            return screen -> new CottonClientScreen(new NoClothConfigScreen(screen));
        }
    }

    private ArrayList<String> getMods(){
        ArrayList<String> mods = new ArrayList<>();

        for(ModContainer mod : FabricLoader.getInstance().getAllMods()){
            mods.add(mod.getMetadata().getId());
        }

        return mods;
    }
}
