package net.lopymine.betteranvil.gui.widgets.buttons;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WToggleButton;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;

import net.lopymine.betteranvil.BetterAnvil;

import java.util.function.Consumer;
import org.jetbrains.annotations.Nullable;

public class WStarButton extends WToggleButton {
    public WStarButton() {
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));

        this.isOn = !this.isOn;
        onToggle(this.isOn);
        return InputResult.PROCESSED;
    }

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
        State state = this.getState(isOn);

        float px = 1 / 14f;

        float buttonLeft = 0;
        float buttonTop = (isHovered() ? 7 : 0) * px;
        float buttonWidth = 7 * px;
        ScreenDrawing.texturedRect(context, x, y, 16, 16, state.getTexture(), buttonLeft, buttonTop, buttonLeft + buttonWidth, buttonTop + buttonWidth, 0xFFFFFFFF);
    }

    @Override
    public boolean canFocus() {
        return false;
    }

    @Override
    public boolean canResize() {
        return false;
    }

    @Override
    public int getWidth() {
        return 16;
    }

    @Override
    public int getHeight() {
        return 16;
    }

    private State getState(boolean isOn) {
        if (isOn) {
            return State.STARRED;
        } else {
            return State.getState(shouldRenderInDarkMode());
        }
    }

    public WStarButton setOnToggle(@Nullable Consumer<Boolean> onToggle) {
        this.onToggle = onToggle;
        return this;
    }

    public boolean getToggle() {
        return this.isOn;
    }

    public void setToggle(boolean on) {
        this.isOn = on;
    }

    public enum State {
        STARRED(BetterAnvil.i("textures/gui/star/star.png")),
        LIGHT(BetterAnvil.i("textures/gui/star/star_light.png")),
        DARK(BetterAnvil.i("textures/gui/star/star_dark.png"));

        private final Identifier texture;

        State(Identifier texture) {
            this.texture = texture;
        }

        public static State getState(boolean bl) {
            return (bl ? State.DARK : State.LIGHT);
        }

        public Identifier getTexture() {
            return texture;
        }
    }
}
