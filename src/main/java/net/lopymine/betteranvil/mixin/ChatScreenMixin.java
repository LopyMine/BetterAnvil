package net.lopymine.betteranvil.mixin;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.gui.PacksGuiDescription;
import net.lopymine.betteranvil.gui.screen.BetterAnvilScreen;
import net.lopymine.betteranvil.gui.ChatGuiDescription;
import net.lopymine.betteranvil.modmenu.BetterAnvilConfigManager;
import net.lopymine.betteranvil.resourcepacks.PackManager;
import net.lopymine.betteranvil.resourcepacks.custommodeldata.CMDItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChatScreen.class)
public class ChatScreenMixin extends Screen {
    protected ChatScreenMixin(Text title) {
        super(title);
    }

    @Shadow
    protected TextFieldWidget chatField;

    private static final Identifier button = new Identifier(BetterAnvil.MOD_ID, "gui/cmdbutton.png");

    private final boolean b = BetterAnvilConfigManager.read().CUSTOM_MODEL_DATA_SUPPORT;

    @Inject(at = @At("RETURN"), method = "init")
    private void scan(CallbackInfo ci) {
        ButtonWidget customModelDataButton  = new ButtonWidget(this.width - 25, 5, 20, 20,Text.of(" "),button -> {
            MinecraftClient.getInstance().setScreen(new BetterAnvilScreen(new ChatGuiDescription(this) {
                @Override
                protected void method(CMDItem item) {
                    chatField.setText("/give @s minecraft:" + item.getItem() + "{CustomModelData:" + item.getId() + "}");
                }

            }));
        });


        if (b) {
            this.addDrawableChild(customModelDataButton);
        }

    }

    @Inject(at = @At("RETURN"), method = "render")
    private void render(MatrixStack matrices, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        if (b) {
            ScreenDrawing.texturedRect(matrices, this.width - 23, 7, 16, 16, button, 0xFFFFFFFF);
        }
    }

}
