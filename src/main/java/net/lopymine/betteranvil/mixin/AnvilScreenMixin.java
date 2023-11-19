package net.lopymine.betteranvil.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.*;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.*;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.*;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;

import net.lopymine.betteranvil.config.BetterAnvilConfig;
import net.lopymine.betteranvil.config.enums.ButtonPositions;
import net.lopymine.betteranvil.gui.*;
import net.lopymine.betteranvil.gui.screen.BetterAnvilScreen;
import net.lopymine.betteranvil.utils.Painters;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ForgingScreen<AnvilScreenHandler> {

    @Unique
    private final ButtonWidget packLeft = ButtonWidget.builder(Text.of(" "), (button -> {
        MinecraftClient.getInstance().setScreen(new BetterAnvilScreen(new ResourcePackRenamesGui(this, null)));
    })).dimensions(this.width / 2 - 113, this.height / 2 - 55, 20, 20).build();
    @Unique
    private final ButtonWidget packRight = ButtonWidget.builder(Text.of(" "), (button -> {
        MinecraftClient.getInstance().setScreen(new BetterAnvilScreen(new ResourcePackRenamesGui(this, null)));
    })).dimensions(this.width / 2 + 91, this.height / 2 - 55, 20, 20).build();
    @Unique
    private final ButtonPositions position = BetterAnvilConfig.getInstance().positionEnum;
    @Unique
    private final boolean noPacks = MinecraftClient.getInstance().getResourcePackManager().getNames().isEmpty();
    @Shadow
    private TextFieldWidget nameField;
    @Unique
    private final ButtonWidget citLeft = ButtonWidget.builder(Text.of(" "), (button -> {
        if (this.handler.getSlot(0).getStack().getItem().equals(Items.AIR)) {
            return;
        }
        MinecraftClient.getInstance().setScreen(new BetterAnvilScreen(new ItemRenamesGui(this, this.handler.getSlot(0).getStack()) {
            @Override
            protected void renameMethod(String name) {
                nameField.setText(name);
                nameField.setEditable(true);
            }
        }));
    })).dimensions(this.width / 2 - 113, this.height / 2 - 80, 20, 20).build();
    @Unique
    private final ButtonWidget citRight = ButtonWidget.builder(Text.of(" "), (button -> {
        if (this.handler.getSlot(0).getStack().getItem().equals(Items.AIR)) {
            return;
        }
        MinecraftClient.getInstance().setScreen(new BetterAnvilScreen(new ItemRenamesGui(this, this.handler.getSlot(0).getStack()) {
            @Override
            protected void renameMethod(String name) {
                nameField.setText(name);
                nameField.setEditable(true);
            }
        }));
    })).dimensions(this.width / 2 + 91, this.height / 2 - 80, 20, 20).build();

    public AnvilScreenMixin(AnvilScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    @Shadow
    protected abstract void setup();

    @Inject(at = @At("TAIL"), method = "setup")
    private void setup(CallbackInfo ci) {
        citRight.setPosition(this.width / 2 + 91, this.height / 2 - 80);
        citLeft.setPosition(this.width / 2 - 113, this.height / 2 - 80);

        packRight.setPosition(this.width / 2 + 91, this.height / 2 - 55);
        citLeft.setPosition(this.width / 2 - 113, this.height / 2 - 55);

        switch (position) {
            case RIGHT -> {
                citRight.active = false;
                citRight.setTooltip(Tooltip.of(Text.translatable("better_anvil.cit_button.disable")));
                this.addDrawableChild(citRight);
                this.addDrawableChild(packRight);
            }
            case LEFT -> {
                citLeft.active = false;
                citLeft.setTooltip(Tooltip.of(Text.translatable("better_anvil.cit_button.disable")));
                this.addDrawableChild(citLeft);
                this.addDrawableChild(packLeft);
            }
        }

        if (noPacks) {
            packLeft.active = false;
            packRight.active = false;

            packLeft.setTooltip(Tooltip.of(Text.translatable("better_anvil.pack_button.disable")));
            packRight.setTooltip(Tooltip.of(Text.translatable("better_anvil.pack_button.disable")));
            citRight.setTooltip(Tooltip.of(Text.translatable("better_anvil.pack_button.disable")));
            citLeft.setTooltip(Tooltip.of(Text.translatable("better_anvil.pack_button.disable")));
        }
    }

    @Inject(at = @At("TAIL"), method = "drawForeground")
    private void drawForeground(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        switch (position) {
            case RIGHT -> {
                context.drawItem(new ItemStack(Items.NAME_TAG), 181, 5);
                ScreenDrawing.texturedRect(context, 183, 32, 12, 12, Painters.SEARCH, 0xFFFFFFFF);
            }
            case LEFT -> {
                context.drawItem(new ItemStack(Items.NAME_TAG), -22, 5);
                ScreenDrawing.texturedRect(context, -22, 32, 12, 12, Painters.SEARCH, 0xFFFFFFFF);
            }
        }

        if (!this.handler.getSlot(0).getStack().isEmpty() && !noPacks) {
            citLeft.active = true;
            citLeft.setTooltip(null);

            citRight.active = true;
            citRight.setTooltip(null);
        } else if (!noPacks) {
            citLeft.active = false;
            citLeft.setTooltip(Tooltip.of(Text.translatable("better_anvil.cit_button.disable")));

            citRight.active = false;
            citRight.setTooltip(Tooltip.of(Text.translatable("better_anvil.cit_button.disable")));
        }
    }
}

