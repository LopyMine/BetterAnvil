package net.lopymine.betteranvil.mixin;

import net.lopymine.betteranvil.modmenu.BetterAnvilConfigManager;
import net.lopymine.betteranvil.modmenu.enums.ResourcePackJsonWriting;
import net.lopymine.betteranvil.resourcepacks.ConfigManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackListWidget;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PackScreen.class)
public abstract class PackScreenMixin extends Screen{

    @Shadow private ButtonWidget doneButton;

    protected PackScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void init(CallbackInfo ci) {
        this.remove(doneButton);
        this.doneButton = new ButtonWidget(this.width / 2 + 4, this.height - 48, 150, 20, ScreenTexts.DONE, button -> {
            this.close();
            if(BetterAnvilConfigManager.read().START == ResourcePackJsonWriting.BUTTON){
                ConfigManager.writeCITItems();
                if(BetterAnvilConfigManager.read().CUSTOM_MODEL_DATA_SUPPORT){
                    ConfigManager.writeCMDItems();
                }
            }
        });
        addDrawableChild(doneButton);
    }
}
