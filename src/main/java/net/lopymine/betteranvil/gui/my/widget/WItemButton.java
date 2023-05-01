package net.lopymine.betteranvil.gui.my.widget;

import io.github.cottonmc.cotton.gui.client.LibGui;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WItem;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.modmenu.BetterAnvilConfigManager;
import net.lopymine.betteranvil.modmenu.enums.CITButtonTexture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class WItemButton extends WWidget {

    private Identifier MY_BUTTON = new Identifier(BetterAnvil.MOD_ID, "gui/mybutton.png");
    private Identifier MY_BUTTON_RENAME = new Identifier(BetterAnvil.MOD_ID, "gui/mybuttonrename.png");
    private final Identifier MY_BUTTON_DARK = new Identifier(BetterAnvil.MOD_ID, "gui/mybuttondark.png");
    private static final Identifier MY_BUTTON_FOCUS = new Identifier(BetterAnvil.MOD_ID, "gui/mybuttonfocus.png");
    private WItem itemIcon;
    private Text text;
    private Runnable onClick;
    private Text toolTip;

    private final Identifier actualTexture;

    public WItemButton(Text text, WItem itemIcon){
        this.text = text;
        this.itemIcon = itemIcon;
        this.actualTexture = getActualTexture();
    }
    public WItemButton(Text text){
        this.text = text;
        this.actualTexture = getActualTexture();
    }
    public WItemButton(){
        this.actualTexture = getActualTexture();
    }

    @Override
    public boolean canResize() {
        return true;
    }

    @Override
    public boolean canFocus() {
        return true;
    }

    public WItemButton setItemIcon(WItem itemIcon){
        this.itemIcon = itemIcon;
        return this;
    }

    public WItemButton setText(Text text){
        this.text = text;
        return this;
    }

    @Override
    public void setSize(int x, int y) {
        super.setSize(155, 32);
    }

    @Override
    public InputResult onClick(int x, int y, int button) {

        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));

        if (onClick!=null) onClick.run();
        return InputResult.PROCESSED;

    }

    @Override
    public void addTooltip(TooltipBuilder tooltip) {
        if(toolTip != null){
            tooltip.add(toolTip);
        }
    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {

        ScreenDrawing.texturedRect(matrices, x, y, 155, 32, actualTexture, 0xFFFFFFFF);


        if (isHovered() || isFocused()) {
            ScreenDrawing.texturedRect(matrices, x, y, 155, 32, MY_BUTTON_FOCUS , 0xFFFFFFFF);
        }

        if (itemIcon != null) {
            itemIcon.paint(matrices, x + 7,y + 7, mouseX, mouseY);
        }

        if(text != null){
            int color = 0xE0E0E0;

            ScreenDrawing.drawStringWithShadow(matrices, text.asOrderedText(), HorizontalAlignment.LEFT, x + 31, y + 12, width, color);

        }
       //if(toolTip != null){
       //    TooltipBuilder tooltipBuilder = new TooltipBuilder();
       //    tooltipBuilder.add(toolTip);
       //    addTooltip(tooltipBuilder);
       //    super.renderTooltip(matrices, x, y, 10,10);
       //}
    }

    public void setToolTip(Text toolTip) {
        this.toolTip = toolTip;
    }

    public void setOnClick(@Nullable Runnable onClick) {
        this.onClick = onClick;
    }

    private Identifier getActualTexture(){
        //return LibGui.isDarkMode() ? MY_BUTTON_DARK : MY_BUTTON;
        CITButtonTexture citButtonTexture = BetterAnvilConfigManager.read().BUTTON_TEXTURE;
        if(citButtonTexture == CITButtonTexture.THEME){
            return LibGui.isDarkMode() ? MY_BUTTON_DARK : MY_BUTTON;
        }
        if(citButtonTexture == CITButtonTexture.RENAME){
            return MY_BUTTON_RENAME;
        }
        if(citButtonTexture == CITButtonTexture.RENAME_DARK_THEME){
            return LibGui.isDarkMode() ? MY_BUTTON_RENAME : MY_BUTTON;
        }
        if(citButtonTexture == CITButtonTexture.RENAME_WHITE_THEME){
            return LibGui.isDarkMode() ? MY_BUTTON_DARK : MY_BUTTON_RENAME;
        }
        return MY_BUTTON;
    }

}
