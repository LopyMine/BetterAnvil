package net.lopymine.betteranvil.mixin;

import net.lopymine.betteranvil.cit.ConfigWriter;
import net.lopymine.betteranvil.modmenu.BetterAnvilConfigManager;
import net.lopymine.betteranvil.modmenu.enums.ResourcePackJsonWriting;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PackScreen.class)
public abstract class ResourcePackMenu extends Screen{

    @Shadow private ButtonWidget doneButton;

    protected ResourcePackMenu(Text title) {
        super(title);
    }

    @Inject(at = @At("RETURN"), method = "init")
    private void init(CallbackInfo ci) {
        this.remove(doneButton);

        this.doneButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            this.close();
            if(BetterAnvilConfigManager.read().START == ResourcePackJsonWriting.BUTTON){
                ConfigWriter.writePackConfig();

            }
        }).dimensions(this.width / 2 + 4, this.height - 48, 150, 20).build());

    }
}
