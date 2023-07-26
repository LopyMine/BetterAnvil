package net.lopymine.betteranvil.gui.widgets;

import io.github.cottonmc.cotton.gui.client.LibGui;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.gui.widgets.enums.Switcher;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class WSwitcher extends WWidget {

    private static final Identifier SWITCH_RIGHT = new Identifier(BetterAnvil.ID, "gui/switchers/sright.png");
    private static final Identifier SWITCH_LEFT = new Identifier(BetterAnvil.ID, "gui/switchers/sleft.png");
    private static final Identifier SWITCH_RIGHT_DARK = new Identifier(BetterAnvil.ID, "gui/switchers/sdright.png");
    private static final Identifier SWITCH_LEFT_DARK = new Identifier(BetterAnvil.ID, "gui/switchers/sdleft.png");
    private static final Identifier SWITCH_FOCUS_RIGHT  = new Identifier(BetterAnvil.ID, "gui/switchers/sfright.png");
    private static final Identifier SWITCH_FOCUS_LEFT  = new Identifier(BetterAnvil.ID, "gui/switchers/sfleft.png");
    private static final int X = 13;
    private static final int Y = 22;
    private Runnable onClick;
    private final Identifier switcher;
    private final Identifier switcherFocus;

    public WSwitcher(Switcher swit){
        Identifier switcherFocus1;
        Identifier switcher1;
        switcher1 = SWITCH_RIGHT;
        switcherFocus1 = SWITCH_FOCUS_RIGHT;
        switch (swit){
            case LEFT -> {
                switcher1 = getTexture(SWITCH_LEFT_DARK, SWITCH_LEFT);
                switcherFocus1 = SWITCH_FOCUS_LEFT;
            }
            case RIGHT -> {
                switcher1 = getTexture(SWITCH_RIGHT_DARK, SWITCH_RIGHT);
                switcherFocus1 = SWITCH_FOCUS_RIGHT;
            }
        }
        switcherFocus = switcherFocus1;
        switcher = switcher1;
    }
    @Override
    public void setSize(int x, int y) {
        super.setSize(X, Y);
    }

    @Override
    public InputResult onClick(int x, int y, int button) {

        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));

        if (onClick!=null) onClick.run();
        return InputResult.PROCESSED;
    }

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
        ScreenDrawing.texturedRect(context,x,y, X,Y, switcher,0xFFFFFFFF);

        if (isHovered() || isFocused()) {
            ScreenDrawing.texturedRect(context, x, y, X, Y, switcherFocus, 0xFFFFFFFF);
        }

    }

    private Identifier getTexture(Identifier v1, Identifier v2){
        return LibGui.isDarkMode() ? v1 : v2;
    }
    public void setOnClick(@Nullable Runnable onClick) {
        this.onClick = onClick;
    }
}
