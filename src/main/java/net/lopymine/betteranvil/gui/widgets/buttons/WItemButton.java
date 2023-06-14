package net.lopymine.betteranvil.gui.widgets.buttons;

import io.github.cottonmc.cotton.gui.client.LibGui;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.TooltipBuilder;
import io.github.cottonmc.cotton.gui.widget.WItem;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.client.BetterAnvilClient;
import net.lopymine.betteranvil.modmenu.BetterAnvilConfigManager;
import net.lopymine.betteranvil.modmenu.enums.CITButtonTexture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class WItemButton extends WWidget {

    private final Identifier MY_BUTTON = new Identifier(BetterAnvil.MOD_ID, "gui/mybutton.png");
    private final Identifier MY_BUTTON_RENAME = new Identifier(BetterAnvil.MOD_ID, "gui/mybuttonrename.png");
    private final Identifier MY_BUTTON_DARK = new Identifier(BetterAnvil.MOD_ID, "gui/mybuttondark.png");
    private static final Identifier MY_BUTTON_FOCUS = new Identifier(BetterAnvil.MOD_ID, "gui/mybuttonfocus.png");
    private WItem itemIcon;
    private ItemStack stack;
    private Text text;
    private Runnable onClick;
    private Runnable onCtrlClick;
    private Text itemNameToolTip = Text.of("");
    private Text resourcePackToolTip = Text.of("");
    private static boolean shift = false;
    private static boolean ctrl = false;
    private final Identifier actualTexture;
    private ArrayList<String> lore = new ArrayList<>();
    private final int SHIFT_KEY = BetterAnvilConfigManager.read().SHIFT_KEY;
    private final int CTRL_KEY = BetterAnvilConfigManager.read().CTRL_KEY;

    public WItemButton(Text text, WItem itemIcon) {
        this.text = text;
        this.itemIcon = itemIcon;
        this.actualTexture = getActualTexture();
    }

    public WItemButton(Text text) {
        this.text = text;
        this.actualTexture = getActualTexture();
    }

    public WItemButton() {
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

    public WItemButton setItemIcon(WItem itemIcon) {
        this.itemIcon = itemIcon;
        return this;
    }

    public WItemButton setText(Text text) {
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

        if (ctrl) {
            onCtrlClick.run();
            return InputResult.PROCESSED;
        }

        if (onClick != null) onClick.run();
        return InputResult.PROCESSED;

    }

    @Override
    public void addTooltip(TooltipBuilder tooltip) {
        tooltip.add(itemNameToolTip);

        if (shift) {

            tooltip.add(Text.of(""));
            tooltip.add(Text.translatable("gui.betteranvil.citmenu.itembutton.tooltip.resourcepack"));
            tooltip.add(resourcePackToolTip);

            if (!lore.isEmpty()) {
                tooltip.add(Text.of(""));
                tooltip.add(Text.translatable("gui.betteranvil.citmenu.itembutton.tooltip.lore"));
                for (String s : lore) {
                    tooltip.add(Text.of("§8" + s));
                }
            }

            if (stack != null) {
                tooltip.add(Text.of(""));
                tooltip.add(Text.translatable("gui.betteranvil.citmenu.itembutton.tooltip.item"));
                tooltip.add(stack.getItem().getName());
            }
        }

    }

    @Override
    public void paint(MatrixStack matrices, int x, int y, int mouseX, int mouseY) {

        ScreenDrawing.texturedRect(matrices, x, y, 155, 32, actualTexture, 0xFFFFFFFF);


        if (isHovered() || isFocused()) {
            ScreenDrawing.texturedRect(matrices, x, y, 155, 32, MY_BUTTON_FOCUS, 0xFFFFFFFF);

        }

        if (itemIcon != null) {
            itemIcon.paint(matrices, x + 8, y + 8, mouseX, mouseY);
        }

        if (text != null) {
            int color = 0xE0E0E0;
            if (!LibGui.isDarkMode() && actualTexture != MY_BUTTON_RENAME) {
                color = 0x262626;
            }
            ScreenDrawing.drawString(matrices, text.asOrderedText(), HorizontalAlignment.LEFT, x + 31, y + 12, width, color);

        }
    }

    public void setToolTip(Text itemNameToolTip) {
        this.itemNameToolTip = itemNameToolTip;
    }

    public void setResourcePackToolTip(Text resourcePackToolTip) {
        this.resourcePackToolTip = resourcePackToolTip;
    }

    public void setOnClick(@Nullable Runnable onClick) {
        this.onClick = onClick;
    }

    private Identifier getActualTexture() {
        //return LibGui.isDarkMode() ? MY_BUTTON_DARK : MY_BUTTON;
        CITButtonTexture citButtonTexture = BetterAnvilConfigManager.read().BUTTON_TEXTURE;
        if (citButtonTexture == CITButtonTexture.THEME) {
            return LibGui.isDarkMode() ? MY_BUTTON_DARK : MY_BUTTON;
        }
        if (citButtonTexture == CITButtonTexture.RENAME) {
            return MY_BUTTON_RENAME;
        }
        if (citButtonTexture == CITButtonTexture.RENAME_DARK_THEME) {
            return LibGui.isDarkMode() ? MY_BUTTON_RENAME : MY_BUTTON;
        }
        if (citButtonTexture == CITButtonTexture.RENAME_WHITE_THEME) {
            return LibGui.isDarkMode() ? MY_BUTTON_DARK : MY_BUTTON_RENAME;
        }
        return MY_BUTTON;
    }

    public void setOnCtrlClick(@Nullable Runnable onClick) {
        this.onCtrlClick = onClick;
    }

    @Override
    public void onKeyReleased(int ch, int key, int modifiers) {
        if (ch == SHIFT_KEY) {
            shift = false;
        }
        if (ch == CTRL_KEY) {
            ctrl = false;
        }
    }

    @Override
    public void onKeyPressed(int ch, int key, int modifiers) {
        if (ch == SHIFT_KEY) {
            shift = true;
        }
        if (ch == CTRL_KEY) {
            ctrl = true;
        }
    }

    public void setLore(ArrayList<String> lore) {
        this.lore = lore;
    }

    public void setStack(ItemStack stack) {
        this.stack = stack;
    }

    public ItemStack getStack() {
        return stack;
    }
}
