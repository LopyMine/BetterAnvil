package net.lopymine.betteranvil.gui.widgets.list;

import io.github.cottonmc.cotton.gui.widget.WScrollBar;
import io.github.cottonmc.cotton.gui.widget.data.*;

public class WScrollBarExt extends WScrollBar {
    public WScrollBarExt(Axis vertical) {
        super(vertical);
    }

    @Override
    public InputResult onMouseScroll(int x, int y, double amount) {
        setValue(getValue() + (int) -amount);
        return InputResult.PROCESSED;
    }
}
