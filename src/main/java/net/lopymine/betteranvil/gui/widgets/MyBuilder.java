package net.lopymine.betteranvil.gui.widgets;

import net.minecraft.client.gui.tooltip.Tooltip;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.jetbrains.annotations.Nullable;

public class MyBuilder extends ButtonWidget.Builder{
    private final Text message;
    private final ButtonWidget.PressAction onPress;
    @Nullable
    private Tooltip tooltip;
    private int x;
    private int y;
    private int width = 150;
    private int height = 20;
    private ButtonWidget.NarrationSupplier narrationSupplier;
    public MyBuilder(Text message, ButtonWidget.PressAction onPress) {
        super(message, onPress);
        this.message = message;
        this.onPress = onPress;
    }
@Override
    public MyBuilder position(int x, int y) {
        this.x = x;
        this.y = y;
        return this;
    }
    @Override
    public MyBuilder width(int width) {
        this.width = width;
        return this;
    }
    @Override
    public MyBuilder size(int width, int height) {
        this.width = width;
        this.height = height;
        return this;
    }
    @Override
    public MyBuilder dimensions(int x, int y, int width, int height) {
        return this.position(x, y).size(width, height);
    }
    @Override
    public MyBuilder tooltip(@Nullable Tooltip tooltip) {
        this.tooltip = tooltip;
        return this;
    }
    @Override
    public ButtonWidget.Builder narrationSupplier(ButtonWidget.NarrationSupplier narrationSupplier) {
        this.narrationSupplier = narrationSupplier;
        return this;
    }

    public MyButtonWidget build() {
        MyButtonWidget buttonWidget = new MyButtonWidget(this.x, this.y, this.width, this.height, this.message, this.onPress, this.narrationSupplier);
        buttonWidget.setTooltip(this.tooltip);
        return buttonWidget;
    }
}
