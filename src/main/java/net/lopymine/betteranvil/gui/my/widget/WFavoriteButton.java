package net.lopymine.betteranvil.gui.my.widget;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;
import io.github.cottonmc.cotton.gui.widget.WToggleButton;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import net.lopymine.betteranvil.BetterAnvil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class WFavoriteButton extends WWidget {

    private boolean isOn = false;

    private static final Texture FAVORITE_OFF = new Texture(new Identifier(BetterAnvil.MOD_ID, "gui/favoriteoff.png"));
    private static final Texture FAVORITE_ON  = new Texture(new Identifier(BetterAnvil.MOD_ID, "gui/favoriteon.png"));
    public WFavoriteButton(){
    }
    @Override
    public boolean canResize() {
        return false;
    }

    public boolean getToggle() { return this.isOn; }
    public void setToggle(boolean on) { this.isOn = on; }

    @Override
    public InputResult onClick(int x, int y, int button) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));

        this.isOn = !this.isOn;
        return InputResult.PROCESSED;
    }

    @Override
    public boolean canFocus() {
        return false;
    }
    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        ScreenDrawing.texturedRect(matrices, x, y, 16,16, getActualTexture(),0xFFFFFFFF);
    }

    private Texture getActualTexture(){
        if(this.isOn){
            return FAVORITE_ON;
        }
        return FAVORITE_OFF;
    }
}
