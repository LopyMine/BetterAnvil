package net.lopymine.betteranvil.mixin;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.gui.AnvilGuiDescription;
import net.lopymine.betteranvil.gui.PacksGuiDescription;
import net.lopymine.betteranvil.gui.screen.BetterAnvilScreen;
import net.lopymine.betteranvil.modmenu.BetterAnvilConfigManager;
import net.lopymine.betteranvil.modmenu.enums.PositionButton;
import net.lopymine.betteranvil.resourcepacks.PackManager;
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
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ForgingScreen<AnvilScreenHandler> {

    @Shadow
    protected abstract void setup();

    public AnvilScreenMixin(AnvilScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    @Shadow
    private TextFieldWidget nameField;
    private static ButtonWidget citButtonWidgetLeft;
    private static ButtonWidget citButtonWidgetRight;
    private static ButtonWidget rpButtonWidgetLeft;
    private static ButtonWidget rpButtonWidgetRight;
    private final PositionButton position = BetterAnvilConfigManager.read().POSITION;
    private final ItemRenderer renderer = MinecraftClient.getInstance().getItemRenderer();
    private static final Identifier search = new Identifier(BetterAnvil.MOD_ID, "gui/search.png");

    @Inject(at = @At("RETURN"), method = "setup")
    private void init(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();

        citButtonWidgetRight = new ButtonWidget(this.width / 2 + 95, this.height / 2 - 80, 24, 20,Text.of(" "),button -> {
            if (this.handler.getSlot(0).getStack().getItem().equals(Items.AIR)) {
                return;
            }
            mc.setScreen(new BetterAnvilScreen(new AnvilGuiDescription(this, this.handler.getSlot(0).getStack()) {
                @Override
                protected void renameMethod(String name) {
                    nameField.setText(name);
                    nameField.setEditable(true);
                }
            }));
        });

        citButtonWidgetLeft = new ButtonWidget(this.width / 2 - 117, this.height / 2 - 80, 24, 20,Text.of(" "),button -> {
            if (this.handler.getSlot(0).getStack().getItem().equals(Items.AIR)) {
                return;
            }
            mc.setScreen(new BetterAnvilScreen(new AnvilGuiDescription(this, this.handler.getSlot(0).getStack()) {
                @Override
                protected void renameMethod(String name) {
                    nameField.setText(name);
                    nameField.setEditable(true);
                }
            }));
        });

        rpButtonWidgetRight = new ButtonWidget(this.width / 2 + 95, this.height / 2 - 55, 24, 20,Text.of(" "),button -> {
            mc.setScreen(new BetterAnvilScreen(new PacksGuiDescription(this, PackManager.getPacksProfiles(), false)));
        });

        rpButtonWidgetLeft = new ButtonWidget(this.width / 2 - 117, this.height / 2 - 55, 24, 20,Text.of(" "),button -> {
            mc.setScreen(new BetterAnvilScreen(new PacksGuiDescription(this, PackManager.getPacksProfiles(), false)));
        });

        switch (position) {
            case RIGHT -> {
                citButtonWidgetRight.active = false;

                this.addDrawableChild(citButtonWidgetRight);
                this.addDrawableChild(rpButtonWidgetRight);
            }
            case LEFT -> {
                citButtonWidgetLeft.active = false;

                this.addDrawableChild(citButtonWidgetLeft);
                this.addDrawableChild(rpButtonWidgetLeft);
            }
        }

        if (PackManager.getPackNamesWithServer().isEmpty()) {
            rpButtonWidgetLeft.active = false;
            rpButtonWidgetRight.active = false;

        }


    }

    @Inject(at = @At("RETURN"), method = "drawForeground")
    private void drawForeground(MatrixStack matrices, int mouseX, int mouseY, CallbackInfo ci) {
        switch (position) {
            case RIGHT -> {
                renderer.renderInGui(new ItemStack(Items.NAME_TAG), 187, 5);//187, 5
                ScreenDrawing.texturedRect(matrices, 188, 32, 12, 12, search, 0xFFFFFFFF);
            }
            case LEFT -> {
                renderer.renderInGui(new ItemStack(Items.NAME_TAG), -25, 5);
                ScreenDrawing.texturedRect(matrices, -23, 32, 12, 12, search, 0xFFFFFFFF);
            }
        }

        if (!this.handler.getSlot(0).getStack().getItem().equals(Items.AIR)) {
            citButtonWidgetLeft.active = true;

            citButtonWidgetRight.active = true;
        } else {
            citButtonWidgetLeft.active = false;

            citButtonWidgetRight.active = false;
        }

    }


}

