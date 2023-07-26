package net.lopymine.betteranvil.mixin;

import net.lopymine.betteranvil.modmenu.BetterAnvilConfigManager;
import net.lopymine.betteranvil.modmenu.enums.ResourcePackJsonWriting;
import net.lopymine.betteranvil.resourcepacks.ConfigManager;
import net.lopymine.betteranvil.resourcepacks.PackManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
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
public abstract class PackScreenMixin extends Screen{

    @Shadow private ButtonWidget doneButton;
    @Shadow public abstract void tick();
    protected PackScreenMixin(Text title) {
        super(title);
    }
    private final BetterAnvilConfigManager config = BetterAnvilConfigManager.INSTANCE;
    @Inject(at = @At("RETURN"), method = "init")
    private void init(CallbackInfo ci) {
        this.remove(doneButton);

        CheckboxWidget checkBox = new CheckboxWidget(this.width / 2 + 158, this.height - 48, 20,20,Text.translatable("better_anvil.mod_menu.rename_writer"),config.OVERWRITE,true);
        checkBox.setTooltip(Tooltip.of(Text.translatable("better_anvil.pack_menu.checkbox.tooltip")));
        if(config.WHEN_OVERWRITE == ResourcePackJsonWriting.CHECKBOX) this.addDrawableChild(checkBox);

        this.doneButton = this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            this.close();
            if(config.WHEN_OVERWRITE == ResourcePackJsonWriting.CHECKBOX && checkBox.isChecked()) {
                if (!PackManager.getPackNamesWithServer().isEmpty()) {
                    ConfigManager.writeCITItems();
                    //ConfigManager.writeCEMItems();
                    if (config.CUSTOM_MODEL_DATA_SUPPORT) {
                        ConfigManager.writeCMDItems();
                    }
                }
            }
            config.OVERWRITE = checkBox.isChecked();
            config.write();
        }).dimensions(this.width / 2 + 4, this.height - 48, 150, 20).build());

    }
}
