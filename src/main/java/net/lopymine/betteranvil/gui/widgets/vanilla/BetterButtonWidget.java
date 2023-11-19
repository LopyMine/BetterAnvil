package net.lopymine.betteranvil.gui.widgets.vanilla;

import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import org.jetbrains.annotations.Nullable;

public class BetterButtonWidget extends ButtonWidget {
    protected BetterButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, NarrationSupplier narrationSupplier) {
        super(x, y, width, height, message, onPress, narrationSupplier);
    }

    public static BetterBuilder builder(Text message, PressAction onPress) {
        return new BetterBuilder(message, onPress);
    }

    public static class BetterBuilder extends ButtonWidget.Builder {
        private final Text message;
        private final ButtonWidget.PressAction onPress;
        @Nullable
        private Tooltip tooltip;
        private int x;
        private int y;
        private int width = 150;
        private int height = 20;
        private ButtonWidget.NarrationSupplier narrationSupplier;

        public BetterBuilder(Text message, ButtonWidget.PressAction onPress) {
            super(message, onPress);
            this.message = message;
            this.onPress = onPress;
        }

        @Override
        public BetterBuilder position(int x, int y) {
            this.x = x;
            this.y = y;
            return this;
        }

        @Override
        public BetterBuilder width(int width) {
            this.width = width;
            return this;
        }

        @Override
        public BetterBuilder size(int width, int height) {
            this.width = width;
            this.height = height;
            return this;
        }

        @Override
        public BetterBuilder dimensions(int x, int y, int width, int height) {
            return this.position(x, y).size(width, height);
        }

        @Override
        public BetterBuilder tooltip(@Nullable Tooltip tooltip) {
            this.tooltip = tooltip;
            return this;
        }

        @Override
        public ButtonWidget.Builder narrationSupplier(ButtonWidget.NarrationSupplier narrationSupplier) {
            this.narrationSupplier = narrationSupplier;
            return this;
        }

        public BetterButtonWidget build() {
            BetterButtonWidget buttonWidget = new BetterButtonWidget(this.x, this.y, this.width, this.height, this.message, this.onPress, this.narrationSupplier);
            buttonWidget.setTooltip(this.tooltip);
            return buttonWidget;
        }
    }
}