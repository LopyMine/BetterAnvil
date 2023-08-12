package net.lopymine.betteranvil.gui.widgets;

import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

import static net.lopymine.betteranvil.BetterAnvil.MYLOGGER;

public class MyButtonWidget extends ButtonWidget {
    protected MyButtonWidget(int x, int y, int width, int height, Text message, PressAction onPress, NarrationSupplier narrationSupplier) {
        super(x, y, width, height, message, onPress, narrationSupplier);
    }

    public static MyBuilder builder(Text message, PressAction onPress) {
        return new MyBuilder(message, onPress);
    }


    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        MYLOGGER.info("My button is pressed, why?");
        return false;
    }





















}
