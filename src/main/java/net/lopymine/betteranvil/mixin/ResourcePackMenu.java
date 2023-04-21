package net.lopymine.betteranvil.mixin;

import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WSprite;
import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.cit.ConfigWriter;
import net.lopymine.betteranvil.modmenu.BetterAnvilConfigManager;
import net.lopymine.betteranvil.modmenu.interfaces.ResourcePackJsonWriting;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TexturedButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
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

        this.doneButton = (ButtonWidget)this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            this.close();
            if(BetterAnvilConfigManager.read().start == ResourcePackJsonWriting.BUTTON){
                ConfigWriter.writePackConfig();

            }
        }).dimensions(this.width / 2 + 4, this.height - 48, 150, 20).build());

        //this.doneButton = (ButtonWidget)this.addDrawableChild(new ButtonWidget(this.width / 2 + 4, this.height - 48, 150, 20, ScreenTexts.DONE, (button) -> {
        //    this.close();
        //    if(BetterAnvilConfigManager.read().start == ResourcePackJsonWriting.BUTTON){
        //        ConfigWriter.writePackConfig();
        //    }
        //}));
    }
}
