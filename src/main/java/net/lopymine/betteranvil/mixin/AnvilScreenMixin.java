package net.lopymine.betteranvil.mixin;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.gui.AnvilGui;
import net.lopymine.betteranvil.gui.CEMGui;
import net.lopymine.betteranvil.gui.PacksGui;
import net.lopymine.betteranvil.gui.screen.BetterAnvilScreen;
import net.lopymine.betteranvil.modmenu.BetterAnvilConfigManager;
import net.lopymine.betteranvil.modmenu.enums.PositionButton;
import net.lopymine.betteranvil.resourcepacks.PackManager;
import net.lopymine.betteranvil.resourcepacks.cem.CEMItem;
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
    private static ButtonWidget citLeft = new ButtonWidget(0,0,20,20,Text.of(""),(button -> {}));
    private static ButtonWidget citRight = new ButtonWidget(0,0,20,20,Text.of(""),(button -> {}));
    private static ButtonWidget packLeft = new ButtonWidget(0,0,20,20,Text.of(""),(button -> {}));
    private static ButtonWidget packRight = new ButtonWidget(0,0,20,20,Text.of(""),(button -> {}));
    private SpawnEggItem item;
    private final PositionButton position = BetterAnvilConfigManager.INSTANCE.POSITION;
    private final ItemRenderer renderer = MinecraftClient.getInstance().getItemRenderer();
    private static final Identifier search = new Identifier(BetterAnvil.ID, "gui/sprites/search.png");
    private final boolean noPacks = PackManager.getPackNamesWithServer().isEmpty();
    @Inject(at = @At("RETURN"), method = "setup")
    private void init(CallbackInfo ci) {
        MinecraftClient mc = MinecraftClient.getInstance();

        citRight = new ButtonWidget(this.width / 2 + 91, this.height / 2 - 80, 20, 20,Text.of(" "),button -> {
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
        });

        citLeft = new ButtonWidget(this.width / 2 - 113, this.height / 2 - 80, 20, 20,Text.of(" "),button -> {
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
        });

        packRight = new ButtonWidget(this.width / 2 + 91, this.height / 2 - 55, 20, 20,Text.of(" "),button -> {
            mc.setScreen(new BetterAnvilScreen(new PacksGui(this, PackManager.getPacksProfiles(), false)));
        });

        packLeft = new ButtonWidget(this.width / 2 - 113, this.height / 2 - 55, 20, 20,Text.of(" "),button -> {
            mc.setScreen(new BetterAnvilScreen(new PacksGui(this, PackManager.getPacksProfiles(), false)));
        });


        //cemRight = new ButtonWidget(this.width / 2 + 91, this.height / 2 - 30, 20, 20,Text.of(" "),button -> {
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
        //});
//
        //cemLeft = new ButtonWidget(this.width / 2 - 113, this.height / 2 - 30, 20, 20,Text.of(" "),button -> {
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
        //});

        switch (position) {
            case RIGHT -> {
                citRight.active = false;
                //cemRight.active = false;

                this.addDrawableChild(citRight);
                this.addDrawableChild(packRight);
                //this.addDrawableChild(cemRight);
            }
            case LEFT -> {
                citLeft.active = false;
                //cemLeft.active = false;

                this.addDrawableChild(citLeft);
                this.addDrawableChild(packLeft);
                //this.addDrawableChild(cemLeft);
            }
        }

        if (noPacks) {
            packLeft.active = false;
            packRight.active = false;
        }
    }

    @Inject(at = @At("RETURN"), method = "drawForeground")
    private void drawForeground(MatrixStack matrices, int mouseX, int mouseY, CallbackInfo ci) {
        switch (position) {
            case RIGHT -> {
                renderer.renderInGui(new ItemStack(Items.NAME_TAG), 181, 5);//187, 5
                ScreenDrawing.texturedRect(matrices, 183, 32, 12, 12, search, 0xFFFFFFFF);
            }
            case LEFT -> {
                renderer.renderInGui(new ItemStack(Items.NAME_TAG), -22, 5);
                ScreenDrawing.texturedRect(matrices, -22, 32, 12, 12, search, 0xFFFFFFFF);
            }
        }

        if (!this.handler.getSlot(0).getStack().getItem().equals(Items.AIR) && !noPacks) {
            citLeft.active = true;
            citRight.active = true;
        } else if(!noPacks) {
            citLeft.active = false;
            citRight.active = false;
        }
        //if(this.handler.getSlot(0).getStack().getItem().equals(Items.NAME_TAG) && !noPacks){
        //    cemLeft.active = true;
        //    cemRight.active = true;
        //} else if(!noPacks){
        //    cemLeft.active = false;
        //    cemRight.active = false;
        //}

    }


}

