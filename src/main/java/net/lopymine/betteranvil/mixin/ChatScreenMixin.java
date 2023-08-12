package net.lopymine.betteranvil.mixin;

import net.lopymine.betteranvil.gui.ChatGui;
import net.lopymine.betteranvil.gui.screen.BetterAnvilScreen;
import net.lopymine.betteranvil.gui.widgets.MyButtonWidget;
import net.lopymine.betteranvil.modmenu.BetterAnvilConfigManager;
import net.lopymine.betteranvil.resourcepacks.PackManager;
import net.lopymine.betteranvil.resourcepacks.cmd.CMDItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Items;
import net.minecraft.server.PlayerManager;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChatScreen.class)
public class ChatScreenMixin extends Screen {
    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    protected TextFieldWidget chatField;
    private final boolean b = BetterAnvilConfigManager.INSTANCE.CUSTOM_MODEL_DATA_SUPPORT;
    private boolean hasOp = false;
    private final boolean noPacks = PackManager.getPackNamesWithServer().isEmpty();

    @Inject(at = @At("RETURN"), method = "init")
    private void scan(CallbackInfo ci) {
        if(!b) return;
        if(MinecraftClient.getInstance().getServer() != null){
            PlayerManager manager = MinecraftClient.getInstance().getServer().getPlayerManager();
            this.hasOp = manager.isOperator(MinecraftClient.getInstance().getSession().getProfile());
        }
        MyButtonWidget cmdButton = MyButtonWidget.builder(Text.of(" "), button -> {
            MinecraftClient.getInstance().setScreen(new BetterAnvilScreen(new ChatGui(this) {
                @Override
                protected void giveMethod(CMDItem item) {
                    chatField.setText("/give @s minecraft:" + item.getItem() + "{CustomModelData:" + item.getId() + "}");
                }
//
            }));
        }).dimensions(this.width - 25, 5, 20, 20).build();
        if(hasOp) cmdButton.active = true;

        if(noPacks){
            cmdButton.active = false;
            cmdButton.setTooltip(Tooltip.of(Text.translatable("better_anvil.pack_button.disable")));
        }

        this.addDrawableChild(cmdButton);
    }

    @Inject(at = @At("RETURN"), method = "render")
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (b) {
            MinecraftClient.getInstance().getItemRenderer().renderInGui(Items.COMMAND_BLOCK.getDefaultStack(),this.width - 23, 7);
        }
    }

    @Inject(at = @At("RETURN"), method = "keyPressed")
    private void render(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        this.setFocused(chatField);
    }

    @Inject(at = @At("HEAD"), method = "keyPressed")
    private void render1(int keyCode, int scanCode, int modifiers, CallbackInfoReturnable<Boolean> cir) {
        this.setFocused(chatField);
    }

}
