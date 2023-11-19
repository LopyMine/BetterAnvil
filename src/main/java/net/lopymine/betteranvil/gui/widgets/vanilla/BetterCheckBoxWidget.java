package net.lopymine.betteranvil.gui.widgets.vanilla;

import net.minecraft.client.gui.widget.CheckboxWidget;
import net.minecraft.text.Text;

import org.jetbrains.annotations.Nullable;

public class BetterCheckBoxWidget extends CheckboxWidget {
    @Nullable
    private BetterCheckBoxWidget.OnChecking onChecking;

    public BetterCheckBoxWidget(int x, int y, int width, int height, Text message, boolean checked, boolean showMessage) {
        super(x, y, width, height, message, checked, showMessage);
    }

    @Override
    public void onPress() {
        super.onPress();
        if (onChecking != null) {
            onChecking.onCheck(isChecked());
        }
    }

    public BetterCheckBoxWidget setOnPress(@Nullable BetterCheckBoxWidget.OnChecking onChecking) {
        this.onChecking = onChecking;
        return this;
    }

    public interface OnChecking {
        void onCheck(boolean on);
    }
}
