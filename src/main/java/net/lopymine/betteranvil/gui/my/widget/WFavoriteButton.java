package net.lopymine.betteranvil.gui.my.widget;

import io.github.cottonmc.cotton.gui.client.LibGui;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;
import io.github.cottonmc.cotton.gui.widget.WToggleButton;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import io.github.cottonmc.cotton.gui.widget.data.Texture;
import net.lopymine.betteranvil.BetterAnvil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class WFavoriteButton extends WWidget {

    private boolean isOn = false;

    private static final Identifier FAVORITE_OFF = new Identifier(BetterAnvil.MOD_ID, "gui/favoriteoff.png");

    private static final Identifier FAVORITE_OFF_DARK = new Identifier(BetterAnvil.MOD_ID, "gui/favoriteoffdark.png");
    private static final Identifier FAVORITE_ON  = new Identifier(BetterAnvil.MOD_ID, "gui/favoriteon.png");

    private static final Identifier FAVORITE_FOCUS  = new Identifier(BetterAnvil.MOD_ID, "gui/focuson.png");

    private static final Identifier FAVORITE_FOCUS_DARK  = new Identifier(BetterAnvil.MOD_ID, "gui/focusondark.png");
    private static final Identifier FAVORITE_FOCUS_OFF  = new Identifier(BetterAnvil.MOD_ID, "gui/focusoff.png");

    @Nullable protected Consumer<Boolean> onToggle = null;
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
        onToggle(this.isOn);
        return InputResult.PROCESSED;
    }

    @Override
    public boolean canFocus() {
        return false;
    }
    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        ScreenDrawing.texturedRect(matrices, x, y, 16,16, getActualTexture(),0xFFFFFFFF);

        if (isHovered() || isFocused()) {
            ScreenDrawing.texturedRect(matrices, x, y, 16, 16, getActualFocusTexture() , 0xFFFFFFFF);
        }
    }

    private Identifier getActualTexture(){
        if(this.isOn){
            return FAVORITE_ON;
        }
        return LibGui.isDarkMode() ? FAVORITE_OFF_DARK : FAVORITE_OFF;
    }

    private Identifier getActualFocusTexture(){
        if(this.isOn){
            return LibGui.isDarkMode() ? FAVORITE_FOCUS_DARK : FAVORITE_FOCUS;
        }
        return FAVORITE_FOCUS_OFF;
    }

    public WFavoriteButton setOnToggle(@Nullable Consumer<Boolean> onToggle) {
        this.onToggle = onToggle;
        return this;
    }

    private void onToggle(boolean on) {
        if (this.onToggle != null) {
            this.onToggle.accept(on);
        }
    }
}
