package net.lopymine.betteranvil.gui.widgets.buttons;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.impl.LibGuiCommon;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.lopymine.betteranvil.BetterAnvil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class WTabButton extends WWidget {
    private boolean isOn = false;
    private static final BackgroundPainter SELECTED_TAB = BackgroundPainter.createLightDarkVariants(
            BackgroundPainter.createNinePatch(new Identifier(BetterAnvil.ID, "gui/tab/selected_light.png")).setTopPadding(2),
            BackgroundPainter.createNinePatch(new Identifier(BetterAnvil.ID,"gui/tab/selected_dark.png")).setTopPadding(2)
    );

    private static final BackgroundPainter UNSELECTED_TAB = BackgroundPainter.createLightDarkVariants(
            BackgroundPainter.createNinePatch(new Identifier(BetterAnvil.ID,"gui/tab/unselected_light.png")),
            BackgroundPainter.createNinePatch(new Identifier(BetterAnvil.ID,"gui/tab/unselected_dark.png"))
    );

    private final WPlainPanel panel = new WPlainPanel();
    private final ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
    private Identifier icon;
    private ItemStack item;
    private String resourcePack;
    private int X = 0;
    private int Y = 0;
    private int w = 20;
    private int h = 20;
    @Nullable
    protected Consumer<Boolean> onToggle = null;

    public WTabButton() {
        panel.setSize(28, 32);
    }

    @Override
    public boolean canResize() {
        return true;
    }

    public boolean getToggle() {
        return this.isOn;
    }

    @Override
    public void addTooltip(TooltipBuilder tooltip) {
        if(resourcePack != null){
            tooltip.add(Text.of(resourcePack));
        }
        super.addTooltip(tooltip);
    }

    public void setToggle(boolean on) {
        this.isOn = on;
        if(this.isOn){
            super.setSize(28,34);
            super.setLocation(X,Y-2);
        } else {
            super.setSize(28,32);
            super.setLocation(X,Y);
        }
    }



    @Override
    public InputResult onClick(int x, int y, int button) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));

        this.isOn = true;
        onToggle(this.isOn);

        if(this.isOn){
            super.setSize(28,34);
            super.setLocation(X,Y-2);
        } else {
            super.setSize(28,32);
            super.setLocation(X,Y);
        }

        return InputResult.PROCESSED;
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {
        int d = y;
        if(isOn){
            d = d + 2;
        }
        (isOn ? SELECTED_TAB : UNSELECTED_TAB).paintBackground(matrices, x, d, panel);

        if (icon != null) {
            ScreenDrawing.texturedRect(matrices, x + 4, d + (isOn ? 4 : 6), w, h, icon, 0xFFFFFFFF);
        }

        if(item != null){
            itemRenderer.renderInGui(matrices,item,x + 6,d + (isOn ? 5 : 7));
        }

    }

    @Override
    public void setLocation(int x, int y) {
        this.X = x;
        this.Y = y;
        super.setLocation(x, y);
    }

    public WTabButton setOnToggle(@Nullable Consumer<Boolean> onToggle) {
        this.onToggle = onToggle;
        return this;
    }
    public Consumer<Boolean> getOnToggle() {
        return onToggle;
    }

    @Override
    public void setSize(int x, int y) {
        super.setSize(28, 32);
    }

    private void onToggle(boolean on) {
        if (this.onToggle != null) {
            this.onToggle.accept(on);
        }
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public void setIcon(Identifier icon) {
        this.icon = icon;
    }

    public void setResourcePack(String resourcePack) {
        this.resourcePack = resourcePack;
    }

    public String getResourcePack() {
        return resourcePack;
    }

}
