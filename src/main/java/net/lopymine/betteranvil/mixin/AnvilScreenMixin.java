package net.lopymine.betteranvil.mixin;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.gui.CEMGui;
import net.lopymine.betteranvil.gui.PacksGui;
import net.lopymine.betteranvil.gui.screen.BetterAnvilScreen;
import net.lopymine.betteranvil.gui.AnvilGui;
import net.lopymine.betteranvil.modmenu.BetterAnvilConfigManager;
import net.lopymine.betteranvil.modmenu.enums.PositionButton;
import net.lopymine.betteranvil.resourcepacks.PackManager;
import net.lopymine.betteranvil.resourcepacks.cem.CEMItem;
import net.lopymine.betteranvil.resourcepacks.utils.ItemUtils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AnvilScreen;
import net.minecraft.client.gui.screen.ingame.ForgingScreen;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(AnvilScreen.class)
public abstract class AnvilScreenMixin extends ForgingScreen<AnvilScreenHandler> {

    @Shadow
    protected abstract void setup();

    public AnvilScreenMixin(AnvilScreenHandler handler, PlayerInventory playerInventory, Text title, Identifier texture) {
        super(handler, playerInventory, title, texture);
    }

    @Shadow
    private TextFieldWidget nameField;
    private static ButtonWidget citLeft = ButtonWidget.builder(Text.of(" "), button -> {}).dimensions(0,0,20,20).build();
    private static ButtonWidget citRight = ButtonWidget.builder(Text.of(" "), button -> {}).dimensions(0,0,20,20).build();

    private static ButtonWidget packLeft = ButtonWidget.builder(Text.of(" "), button -> {}).dimensions(0,0,20,20).build();

    private static ButtonWidget packRight = ButtonWidget.builder(Text.of(" "), button -> {}).dimensions(0,0,20,20).build();
    private static ButtonWidget cemLeft = ButtonWidget.builder(Text.of(" "), button -> {}).dimensions(0,0,20,20).build();
    private static ButtonWidget cemRight = ButtonWidget.builder(Text.of(" "), button -> {}).dimensions(0,0,20,20).build();
    private SpawnEggItem item;
    private final PositionButton position = BetterAnvilConfigManager.INSTANCE.POSITION;
    private final ItemRenderer renderer = MinecraftClient.getInstance().getItemRenderer();
    private static final Identifier search = new Identifier(BetterAnvil.ID, "gui/sprites/search.png");
    private final boolean noPacks = PackManager.getPackNamesWithServer().isEmpty();
    @Inject(at = @At("RETURN"), method = "setup")
    private void init(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();

        citRight = ButtonWidget.builder(Text.of(" "), (button -> {
            if (this.handler.getSlot(0).getStack().getItem().equals(Items.AIR)) {
                return;
            }
            mc.setScreen(new BetterAnvilScreen(new AnvilGui(this, this.handler.getSlot(0).getStack()) {
                @Override
                protected void renameMethod(String name) {
                    nameField.setText(name);
                    nameField.setEditable(true);
                }
            }));
        })).dimensions(this.width / 2 + 91, this.height / 2 - 80, 20, 20).build();


        citLeft = ButtonWidget.builder(Text.of(" "), (button -> {
            if (this.handler.getSlot(0).getStack().getItem().equals(Items.AIR)) {
                return;
            }
            mc.setScreen(new BetterAnvilScreen(new AnvilGui(this, this.handler.getSlot(0).getStack()) {
                @Override
                protected void renameMethod(String name) {
                    nameField.setText(name);
                    nameField.setEditable(true);
                }
            }));
        })).dimensions(this.width / 2 - 113, this.height / 2 - 80, 20, 20).build();

        packRight = ButtonWidget.builder(Text.of(" "), (button -> {
            mc.setScreen(new BetterAnvilScreen(new PacksGui(this, PackManager.getPacksProfiles(), false)));
        })).dimensions(this.width / 2 + 91, this.height / 2 - 55, 20, 20).build();


        packLeft = ButtonWidget.builder(Text.of(" "), (button -> {
            mc.setScreen(new BetterAnvilScreen(new PacksGui(this, PackManager.getPacksProfiles(), false)));
        })).dimensions(this.width / 2 - 113, this.height / 2 - 55, 20, 20).build();


        //cemRight = ButtonWidget.builder(Text.of(" "), (button -> {
        //    if (this.handler.getSlot(0).getStack().getItem().equals(Items.AIR)) {
        //        return;
        //    }
        //    mc.setScreen(new BetterAnvilScreen(new CEMGui(this) {
        //        @Override
        //        protected void renameMethod(CEMItem item) {
        //            nameField.setText(item.getName());
        //            nameField.setEditable(true);
        //        }
        //    }));
        //})).dimensions(this.width / 2 + 91, this.height / 2 - 30, 20, 20).build();
//
//
        //cemLeft = ButtonWidget.builder(Text.of(" "), (button -> {
        //    if (this.handler.getSlot(0).getStack().getItem().equals(Items.AIR)) {
        //        return;
        //    }
        //    mc.setScreen(new BetterAnvilScreen(new CEMGui(this) {
        //        @Override
        //        protected void renameMethod(CEMItem item) {
        //            nameField.setText(item.getName());
        //            nameField.setEditable(true);
        //        }
        //    }));
        //})).dimensions(this.width / 2 - 113, this.height / 2 - 30, 20, 20).build();

        switch (position) {
            case RIGHT -> {
                citRight.active = false;
                //cemRight.active = false;
                citRight.setTooltip(Tooltip.of(Text.translatable("better_anvil.cit_button.disable")));
                this.addDrawableChild(citRight);
                this.addDrawableChild(packRight);
                //this.addDrawableChild(cemRight);
            }
            case LEFT -> {
                citLeft.active = false;
                //cemLeft.active = false;
                citLeft.setTooltip(Tooltip.of(Text.translatable("better_anvil.cit_button.disable")));
                this.addDrawableChild(citLeft);
                this.addDrawableChild(packLeft);
                //this.addDrawableChild(cemLeft);
            }
        }

        if (noPacks) {
            packLeft.active = false;
            packRight.active = false;

            packLeft.setTooltip(Tooltip.of(Text.translatable("better_anvil.pack_button.disable")));
            packRight.setTooltip(Tooltip.of(Text.translatable("better_anvil.pack_button.disable")));
            citRight.setTooltip(Tooltip.of(Text.translatable("better_anvil.pack_button.disable")));
            citLeft.setTooltip(Tooltip.of(Text.translatable("better_anvil.pack_button.disable")));
            //cemRight.setTooltip(Tooltip.of(Text.translatable("better_anvil.pack_button.disable")));
            //cemLeft.setTooltip(Tooltip.of(Text.translatable("better_anvil.pack_button.disable")));
        }

        //ArrayList<SpawnEggItem> items = new ArrayList<>();
        //SpawnEggItem.getAll().iterator().forEachRemaining(items::add);
        //Random random = Random.create();
        //item = items.get(random.nextInt(items.size()-1));

    }

    @Inject(at = @At("RETURN"), method = "drawForeground")
    private void drawForeground(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        switch (position) {
            case RIGHT -> {
                context.drawItem(new ItemStack(Items.NAME_TAG), 181, 5);
                //renderer.renderInGui(matrices, item.getDefaultStack(), 181, 54);
                ScreenDrawing.texturedRect(context, 183, 32, 12, 12, search, 0xFFFFFFFF);
            }
            case LEFT -> {
                context.drawItem(new ItemStack(Items.NAME_TAG), -22, 5);
                //renderer.renderInGui(matrices, item.getDefaultStack(), -23, 54);
                ScreenDrawing.texturedRect(context, -22, 32, 12, 12, search, 0xFFFFFFFF);
            }
        }

        if (!this.handler.getSlot(0).getStack().getItem().equals(Items.AIR) && !noPacks) {
            citLeft.active = true;
            citLeft.setTooltip(null);

            citRight.active = true;
            citRight.setTooltip(null);
        } else if(!noPacks) {
            citLeft.active = false;
            citLeft.setTooltip(Tooltip.of(Text.translatable("better_anvil.cit_button.disable")));

            citRight.active = false;
            citRight.setTooltip(Tooltip.of(Text.translatable("better_anvil.cit_button.disable")));
        }

        //if(this.handler.getSlot(0).getStack().getItem().equals(Items.NAME_TAG) && !noPacks){
        //    cemLeft.active = true;
        //    cemLeft.setTooltip(null);
        //    cemRight.active = true;
        //    cemRight.setTooltip(null);
        //} else if(!noPacks){
        //    cemLeft.active = false;
        //    cemLeft.setTooltip(Tooltip.of(Text.translatable("better_anvil.cem_button.disable")));
        //    cemRight.active = false;
        //    cemRight.setTooltip(Tooltip.of(Text.translatable("better_anvil.cem_button.disable")));
        //}

    }


}

