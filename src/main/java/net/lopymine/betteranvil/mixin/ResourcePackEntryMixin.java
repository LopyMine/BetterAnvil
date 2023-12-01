package net.lopymine.betteranvil.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.*;
import net.minecraft.client.gui.screen.pack.PackListWidget.ResourcePackEntry;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;

import net.lopymine.betteranvil.config.BetterAnvilConfig;
import net.lopymine.betteranvil.config.resourcepacks.ResourcePackConfigsManager;
import net.lopymine.betteranvil.gui.SelectionGui;
import net.lopymine.betteranvil.gui.screen.SimpleGuiScreen;
import net.lopymine.betteranvil.resourcepacks.*;
import net.lopymine.betteranvil.utils.*;

import java.util.HashSet;

import static net.lopymine.betteranvil.config.resourcepacks.ResourcePackConfigsManager.JSON_FORMAT;

@Mixin(PackListWidget.ResourcePackEntry.class)
public abstract class ResourcePackEntryMixin {
    @Shadow
    @Final
    private PackListWidget widget;

    @Shadow
    public abstract String getName();

    @Unique
    private HashSet<ResourcePackType> types = new HashSet<>();
    @Unique
    private final BetterAnvilConfig config = BetterAnvilConfig.getInstance();

    @Inject(at = @At("TAIL"), method = "mouseClicked")
    private void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
        if (!config.enabledViewResourcePacks) {
            return;
        }
        if (isMagnifierHovered(mouseX, mouseY)) {
            MinecraftClient client = MinecraftClient.getInstance();
            client.getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));

            Screen currentScreen = client.currentScreen;
            if (types.size() == 1) {
                SelectionGui.tryOpenScreenByType(types.stream().toList().get(0), currentScreen, this.getName());
            } else {
                SelectionGui selectionGui = new SelectionGui(currentScreen, types, this.getName());
                SimpleGuiScreen screen = new SimpleGuiScreen(selectionGui);
                selectionGui.setCurrentScreen(screen);

                client.setScreen(screen);
            }
        }
    }

    @Unique
    private boolean isMagnifierHovered(double mouseX, double mouseY) {
        if (types.isEmpty()) {
            return false;
        }

        int x = (int) mouseX - widget.getRowLeft();
        int index = widget.children().indexOf(((ResourcePackEntry) (Object) this));
        int y = (int) mouseY - widget.getRowTop(index);

        return x >= 176 && x <= 188 && y >= 0 & y <= 13;
    }

    @Inject(at = @At("TAIL"), method = "render")
    private void render(DrawContext context, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
        if (!config.enabledViewResourcePacks) {
            return;
        }
        if (hovered && !types.isEmpty()) {
            Painters.drawSearch(context, x + 176, y + 1, isMagnifierHovered(mouseX, mouseY));
        }
    }

    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(MinecraftClient client, PackListWidget widget, ResourcePackOrganizer.Pack pack, CallbackInfo ci) {
        if (!config.enabledViewResourcePacks) {
            return;
        }
        if (types == null) {
            types = new HashSet<>();
        }

        for (ResourcePackType type : ResourcePackType.values()) {
            if (type == ResourcePackType.CEM) {
                continue;
            }
            String name = pack.getName();
            boolean isServer = name.equals("server");

            String configPath = isServer ? ResourcePackConfigsManager.getServerConfigPath(type) : ResourcePackConfigsManager.getConfigPath(type);
            String path = configPath + ResourcePackUtils.getResourcePackName(isServer ? ServerResourcePackManager.getServer() : name) + JSON_FORMAT;

            if (ResourcePackConfigsManager.hasConfig(path)) {
                types.add(type);
            }
        }
    }
}