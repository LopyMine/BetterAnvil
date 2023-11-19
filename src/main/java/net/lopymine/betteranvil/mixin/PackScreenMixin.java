package net.lopymine.betteranvil.mixin;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.pack.PackScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.lopymine.betteranvil.config.BetterAnvilConfig;
import net.lopymine.betteranvil.config.enums.Overwriting;
import net.lopymine.betteranvil.gui.widgets.vanilla.BetterCheckBoxWidget;
import net.lopymine.betteranvil.resourcepacks.ResourcePackManager;

@Mixin(PackScreen.class)
public abstract class PackScreenMixin extends Screen {
    @Unique
    private final BetterAnvilConfig config = BetterAnvilConfig.getInstance();
    @Shadow
    private ButtonWidget doneButton;

    protected PackScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    public abstract void tick();

    @Shadow
    public abstract void close();

    @Inject(at = @At("TAIL"), method = "init")
    private void init(CallbackInfo ci) {
        if (config.overwritingEnum != Overwriting.CHECKBOX) return;

        BetterCheckBoxWidget checkBox = this.addDrawableChild(new BetterCheckBoxWidget(this.width / 2 + 158, this.height - 48, 20, 20, Text.translatable("better_anvil.mod_menu.overwriting"), config.isCheckBoxChecked, true)
                .setOnPress((on) -> {
                    config.isCheckBoxChecked = on;
                    config.write();
                }));

        checkBox.setTooltip(Tooltip.of(Text.translatable("better_anvil.pack_menu.checkbox.tooltip")));

        this.remove(doneButton);

        this.doneButton = ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
            this.close();

            if (checkBox.isChecked()) {
                ResourcePackManager.startWriting(config);
            }
        }).dimensions(this.width / 2 + 4, this.height - 48, 150, 20).build();

        this.addDrawableChild(doneButton);
    }
}
