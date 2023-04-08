package net.lopymine.betteranvil.mixin;

import io.github.cottonmc.cotton.gui.client.CottonClientScreen;
import net.lopymine.betteranvil.gui.AnvilGui;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
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

    @Shadow
    protected abstract void onRenamed(String name);

    @Shadow protected abstract void setup();

    public AnvilGuiMixin(AnvilScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    @Inject(at = @At("RETURN"), method = "drawForeground")
    private void drawForeground(MatrixStack ms, int mouseX, int mouseY, CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();
        ItemRenderer renderer = mc.getItemRenderer();
        //BlockModelRenderer bmr = mc.getBlockRenderManager().getModelRenderer();
//
        //BlockRenderManager brm = mc.getBlockRenderManager();
//
        //VertexConsumerProvider provider = mc.getBufferBuilders().getEffectVertexConsumers();
        //VertexConsumer vertexConsumer = provider.getBuffer(RenderLayer.LINE_STRIP);

        Slot slot = ((AnvilScreenHandler) this.handler).getSlot(0);

        ItemStack slotStack = slot.getStack();


        ButtonWidget buttonWidget = new ButtonWidget(this.width / 2 + 95, this.height / 2 - 80, 25, 20, Text.of(" "), (button) -> {
            mc.setScreen(new CottonClientScreen(new AnvilGui(this, slot.getStack()) {
                @Override
                protected void rename(String name) {
                    onRenamed(name);
                    System.out.println(name);
                }
            }));

        });
        if (!slot.getStack().isEmpty()) {
            this.clearChildren();
            this.addDrawableChild(buttonWidget);
            renderer.renderInGui(new ItemStack(Items.NAME_TAG), 187, 4);
            //ButtonWidget clearButtonWidget = new ButtonWidget(this.width / 2 + 95, this.height / 2 - 55, 50, 20, Text.translatable("gui.betteranvil.button.clear"), (button1) -> {
            //    if (!slot.getStack().isEmpty()) {
            //        //slot.getStack().removeCustomName();
            //        this.onRenamed(slot.getStack().getItem().asItem().getName().getString());
            //    }
            //});
            //this.addDrawableChild(clearButtonWidget);
            return;
        }
        this.clearChildren();
        //ButtonWidget clearButtonWidget = new ButtonWidget(this.width / 2 + 95, this.height / 2 - 80, 50, 20, Text.translatable("gui.betteranvil.button.clear"), (button1) -> {
        //    if (!slot.getStack().isEmpty()) {
        //        //slot.getStack().removeCustomName();
        //        this.onRenamed(slot.getStack().getItem().asItem().getName().getString());
        //    }
        //});
        //this.addDrawableChild(clearButtonWidget);
        return;
    }
}

