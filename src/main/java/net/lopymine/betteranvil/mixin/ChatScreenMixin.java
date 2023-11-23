package net.lopymine.betteranvil.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.*;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.*;

import net.lopymine.betteranvil.config.BetterAnvilConfig;
import net.lopymine.betteranvil.config.resourcepacks.cmd.CMDItem;
import net.lopymine.betteranvil.gui.CustomModelDataItemsGui;
import net.lopymine.betteranvil.gui.screen.BetterAnvilScreen;
import net.lopymine.betteranvil.gui.widgets.vanilla.BetterButtonWidget;

@Mixin(ChatScreen.class)
public abstract class ChatScreenMixin extends Screen {
    @Unique
    private final boolean bl = BetterAnvilConfig.getInstance().enabledCMD;
    @Shadow
    protected TextFieldWidget chatField;

    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Inject(at = @At("TAIL"), method = "init")
    private void init(CallbackInfo ci) {
        if (!bl) return;

        BetterButtonWidget cmdButton = BetterButtonWidget.builder(Text.of(" "), button -> {
            MinecraftClient.getInstance().setScreen(new BetterAnvilScreen(new CustomModelDataItemsGui(this, false, null) {
                @Override
                protected void setCommand(CMDItem item) {
                    chatField.setText("/give @s minecraft:" + item.getItem() + "{CustomModelData:" + item.getId() + "}");
                }
            }));
        }).dimensions(this.width - 25, 5, 20, 20).build();

        if (MinecraftClient.getInstance().getResourcePackManager().getNames().isEmpty()) {
            cmdButton.active = false;
            cmdButton.setTooltip(Tooltip.of(Text.translatable("better_anvil.pack_button.disable")));
        }

        this.addDrawableChild(cmdButton);
    }

    @Inject(at = @At("TAIL"), method = "render")
    private void render(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (bl) {
            context.drawItem(Items.COMMAND_BLOCK.getDefaultStack(), this.width - 23, 7);
        }
    }

    @Inject(at = @At("TAIL"), method = "keyPressed")
    private void keyPressed(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        this.setFocused(chatField);
    }

    @Inject(at = @At("HEAD"), method = "keyPressed")
    private void keyPressed1(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        this.setFocused(chatField);
    }
}
