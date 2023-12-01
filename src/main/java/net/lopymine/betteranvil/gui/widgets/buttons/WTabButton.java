package net.lopymine.betteranvil.gui.widgets.buttons;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import io.github.cottonmc.cotton.gui.widget.icon.Icon;

import net.lopymine.betteranvil.BetterAnvil;

import org.jetbrains.annotations.Nullable;

public class WTabButton extends WToggleButton {

    public static final BackgroundPainter SELECTED_TAB = BackgroundPainter.createLightDarkVariants(
            BackgroundPainter.createNinePatch(BetterAnvil.i("textures/gui/tab/selected_light.png")).setTopPadding(2),
            BackgroundPainter.createNinePatch(BetterAnvil.i("textures/gui/tab/selected_dark.png")).setTopPadding(2)
    );
    public static final BackgroundPainter UNSELECTED_TAB = BackgroundPainter.createLightDarkVariants(
            BackgroundPainter.createNinePatch(BetterAnvil.i("textures/gui/tab/unselected_light.png")),
            BackgroundPainter.createNinePatch(BetterAnvil.i("textures/gui/tab/unselected_dark.png"))
    );
    private final WPlainPanel panel = new WPlainPanel() {
        @Override
        public boolean shouldRenderInDarkMode() {
            return WTabButton.this.shouldRenderInDarkMode();
        }
    };
    private final Icon icon;
    private final String resourcePack;
    private int X = 0;
    private int Y = 0;

    public WTabButton(@Nullable String resourcePack, @Nullable Icon icon) {
        this.resourcePack = resourcePack;
        this.icon = icon;
        panel.setSize(28, 32);
        // TODO: 25.11.2023
    }

    @Override
    public boolean canResize() {
        return true;
    }

    public boolean getToggle() {
        return this.isOn;
    }

    @Override
    public void setToggle(boolean on) {
        this.setToggle(on, false);
    }

    public void setToggle(boolean on, boolean callConsumer) {
        this.isOn = on;

        if (this.isOn) {
            super.setSize(28, 34);
            super.setLocation(X, Y - 2);
        } else {
            super.setSize(28, 32);
            super.setLocation(X, Y);
        }

        if (callConsumer && onToggle != null) {
            onToggle.accept(on);
        }
    }

    @Override
    public void addTooltip(TooltipBuilder tooltip) {
        if (resourcePack != null) {
            tooltip.add(Text.of(resourcePack));
        }
        super.addTooltip(tooltip);
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));
        this.isOn = true;
        onToggle(true);

        super.setSize(28, 34);
        super.setLocation(X, Y - 2);

        return InputResult.PROCESSED;
    }

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
        int d = y;
        if (isOn) {
            d = d + 2;
        }

        (isOn ? SELECTED_TAB : UNSELECTED_TAB).paintBackground(context, x, d, panel);

        if (icon != null) {
            icon.paint(context, x + 5, d + (isOn ? 5 : 7), 18);
        }
    }

    @Override
    public void setLocation(int x, int y) {
        this.X = x;
        this.Y = y;
        super.setLocation(x, y);
    }

    @Override
    public void setSize(int x, int y) {
        super.setSize(28, 32);
    }

    @Override
    public int getWidth() {
        return 28;
    }

    @Override
    public int getHeight() {
        return isOn ? 32 : 34;
    }
}
