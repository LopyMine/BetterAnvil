package net.lopymine.betteranvil.mixin;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import net.fabricmc.fabric.impl.resource.loader.ResourcePackSourceTracker;
import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.gui.PacksGuiDescription;
import net.lopymine.betteranvil.gui.screen.BetterAnvilScreen;
import net.lopymine.betteranvil.resourcepacks.ConfigManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.client.gui.screen.pack.ResourcePackOrganizer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.resource.ResourcePackProfile;
import net.minecraft.resource.ResourcePackSource;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.OrderedText;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ArrayList;
import java.util.Objects;

@Mixin(PackListWidget.ResourcePackEntry.class)
public abstract class ResourcePackEntryMixin {
    @Shadow
    @Final
    private PackListWidget widget;
    @Shadow
    @Final
    private ResourcePackOrganizer.Pack pack;
    private static final Identifier search = new Identifier(BetterAnvil.MOD_ID, "gui/search.png");
    private boolean look = false;
    private ResourcePackManager manager = MinecraftClient.getInstance().getResourcePackManager();

    @Inject(at = @At("RETURN"), method = "mouseClicked")
    private void init(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {

        if (!look) {
            return;
        }

        int x = (int) mouseX - this.widget.getRowLeft();
        int d = (int) mouseY - getRowTop(0);
        int y = (int) mouseY - getRowTop(d / 35);

        ArrayList<ResourcePackProfile> profiles = new ArrayList<>();
        if (manager.getProfile("file/" + pack.getDisplayName().getString()) != null) {
            profiles.add(manager.getProfile("file/" + pack.getDisplayName().getString()));
        } else if (manager.getProfile("server") != null) {
            profiles.add(manager.getProfile("server"));
        }

        if(profiles.isEmpty()){
            System.out.println("emm...");
            return;
        }

        if (x >= 177 && y >= 0 & y <= 14) {
            MinecraftClient.getInstance().setScreen(new BetterAnvilScreen(new PacksGuiDescription(null, profiles, true)));
        }

    }

    @Inject(at = @At("RETURN"), method = "render")
    private void init(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        if (hovered && look) {
            ScreenDrawing.texturedRect(matrices, x + 176, y + 2, 12, 12, search, 0xFFFFFFFF);
        }
    }

    @Inject(at = @At("RETURN"), method = "<init>")
    private void init(MinecraftClient client, PackListWidget widget, Screen screen, ResourcePackOrganizer.Pack pack, CallbackInfo ci) {
        if(pack.getSource() == ResourcePackSource.PACK_SOURCE_NONE || pack.getSource() == ResourcePackSource.PACK_SOURCE_SERVER || pack.getSource() == ResourcePackSource.PACK_SOURCE_WORLD){
            String name;
            if (manager.getProfile("file/" + pack.getDisplayName().getString()) != null) {
                name = manager.getProfile("file/" + pack.getDisplayName().getString()).getName();
            } else if (manager.getProfile("server") != null) {
                name = "server";
            } else {
                return;
            }

            if (name.equals("server")) {
                look = true;
            } else if (ConfigManager.hasCITFolder(ConfigManager.pathToResourcePacks + name.replaceAll("file/", ""))) {
                look = true;
            }
        }
    }

    private int getRowTop(int index) {
        return 32 + 4 - (int) this.widget.getScrollAmount() + index * 36 + (int) (9.0F * 1.5F);
    }


}