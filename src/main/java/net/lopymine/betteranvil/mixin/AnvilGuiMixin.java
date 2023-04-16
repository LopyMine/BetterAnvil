package net.lopymine.betteranvil.mixin;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import io.github.cottonmc.cotton.gui.widget.WButton;
import io.github.cottonmc.cotton.gui.widget.icon.ItemIcon;
import net.lopymine.betteranvil.gui.AnvilGui;
import net.lopymine.betteranvil.gui.TestGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreen.class)
public abstract class AnvilGuiMixin extends ForgingScreen<AnvilScreenHandler> {

    @Shadow protected abstract void setup();

    public AnvilGuiMixin(AnvilScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    @Shadow private TextFieldWidget nameField;

    @Inject(at = @At("RETURN"), method = "drawForeground")
    private void drawForeground(MatrixStack ms, int mouseX, int mouseY, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ItemRenderer renderer = mc.getItemRenderer();

        Slot slot = this.handler.getSlot(0);

        ItemStack slotStack = slot.getStack();


        ButtonWidget buttonWidget = new ButtonWidget(this.width / 2 + 95, this.height / 2 - 80, 25, 20, Text.of(" "), (button) -> {
            //mc.setScreen(new CottonClientScreen(new TestGui()));
            mc.setScreen(new CottonClientScreen(new TestGui(this, slotStack) {
                @Override
                protected void renameMethod(String name) {
                    nameField.setText(name);
                }
            }));

        });
        if (!slot.getStack().isEmpty()) {
            this.clearChildren();
            this.addDrawableChild(buttonWidget);
            renderer.renderInGui(new ItemStack(Items.NAME_TAG), 187, 4);
            return;
        }
        this.clearChildren();
        return;
    }
}

