package net.lopymine.betteranvil.mixin;

import net.lopymine.betteranvil.gui.AnvilGuiDescription;
import net.lopymine.betteranvil.gui.BetterAnvilClientScreen;
import net.lopymine.betteranvil.modmenu.BetterAnvilConfigManager;
import net.lopymine.betteranvil.modmenu.enums.PositionButton;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.screen.multiplayer.MultiplayerScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreen.class)
public abstract class AnvilGuiMixin extends ForgingScreen<AnvilScreenHandler> {

    @Shadow
    protected abstract void setup();

    public AnvilGuiMixin(AnvilScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    @Shadow
    private TextFieldWidget nameField;
    private static boolean noItem = false;
    private static ButtonWidget buttonWidgetLeft;
    private static ButtonWidget buttonWidgetRight;
    private final PositionButton position = BetterAnvilConfigManager.read().POSITION;
    private final ItemRenderer renderer = MinecraftClient.getInstance().getItemRenderer();

    @Inject(at = @At("RETURN"), method = "setup")
    private void init(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();

        buttonWidgetRight = ButtonWidget.builder(Text.of(" "), (button -> {
            if (this.handler.getSlot(0).getStack().getItem().equals(Items.AIR)) {
                noItem = true;
                return;
            }
            mc.setScreen(new BetterAnvilClientScreen(new AnvilGuiDescription(this, this.handler.getSlot(0).getStack()) {
                @Override
                protected void renameMethod(String name) {
                    nameField.setText(name);
                    nameField.setEditable(true);
                }
            }));
        })).dimensions(this.width / 2 + 95, this.height / 2 - 80, 24, 20).build();


        buttonWidgetLeft = ButtonWidget.builder(Text.of(" "), (button -> {
            if (this.handler.getSlot(0).getStack().getItem().equals(Items.AIR)) {
                noItem = true;
                return;
            }
            mc.setScreen(new BetterAnvilClientScreen(new AnvilGuiDescription(this, this.handler.getSlot(0).getStack()) {
                @Override
                protected void renameMethod(String name) {
                    nameField.setText(name);
                    nameField.setEditable(true);
                }
            }));
        })).dimensions(this.width / 2 - 117, this.height / 2 - 80, 24, 20).build();

        switch (position) {
            case RIGHT -> {
                buttonWidgetRight.active = false;
                this.addDrawableChild(buttonWidgetRight);
            }
            case LEFT -> {
                buttonWidgetLeft.active = false;
                this.addDrawableChild(buttonWidgetLeft);
            }
        }
    }

    @Inject(at = @At("HEAD"), method = "drawForeground")
    private void drawForeground(MatrixStack ms, int mouseX, int mouseY, CallbackInfo ci) {
        switch (position) {
            case RIGHT -> renderer.renderInGui(ms, new ItemStack(Items.NAME_TAG), 187, 4);
            case LEFT -> renderer.renderInGui(ms, new ItemStack(Items.NAME_TAG), -25, 4);
        }

        if (!this.handler.getSlot(0).getStack().getItem().equals(Items.AIR)) {
            buttonWidgetLeft.active = true;
            buttonWidgetRight.active = true;
        } else {
            buttonWidgetLeft.active = false;
            buttonWidgetRight.active = false;
        }

        //if (!this.handler.getSlot(0).getStack().getItem().equals(Items.AIR)) {
//
        //    noItem = false;
        //}
        //if (noItem) {
        //    this.renderAnvilSlotWithoutItem(ms);
        //}

    }

    //private void renderAnvilSlotWithoutItem(MatrixStack ms) {
    //    DrawableHelper.fill(ms, 27, 47, 43, 63, 822018048);
    //}

}

