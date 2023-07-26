package net.lopymine.betteranvil.gui.widgets.buttons;

import io.github.cottonmc.cotton.gui.client.LibGui;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WItem;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.HorizontalAlignment;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;
import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.gui.panels.Painters;
import net.lopymine.betteranvil.gui.tooltip.MyTooltipBuilder;
import net.lopymine.betteranvil.gui.tooltip.TooltipBuilder;
import net.lopymine.betteranvil.modmenu.BetterAnvilConfigManager;
import net.lopymine.betteranvil.modmenu.enums.CITButtonTexture;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class WRenameButton extends WWidget {

    private final Identifier MY_BUTTON = new Identifier(BetterAnvil.ID, "gui/itembutton/button.png");
    private final Identifier MY_BUTTON_RENAME = new Identifier(BetterAnvil.ID, "gui/itembutton/button_rename.png");
    private final Identifier MY_BUTTON_DARK = new Identifier(BetterAnvil.ID, "gui/itembutton/button_dark.png");
    private static final Identifier MY_BUTTON_FOCUS = new Identifier(BetterAnvil.ID, "gui/itembutton/button_focus.png");
    private WItem itemIcon;
    private Text text;
    private Runnable onClick;
    private Runnable onCtrlClick;
    private Runnable onCtrlPress;
    private Text itemNameToolTip = Text.of("");
    private Text resourcePackToolTip = Text.of("");
    private static boolean shift = false;
    private static boolean ctrl = false;
    private final Identifier actualTexture;
    private List<String> lore = new ArrayList<>();
    private final BetterAnvilConfigManager config = BetterAnvilConfigManager.INSTANCE;
    private final int SHIFT_KEY = config.SHIFT_KEY;
    private final int CTRL_KEY = config.CTRL_KEY;
    private List<ItemStack> stacks = new ArrayList<>();
    private final MinecraftClient mc = MinecraftClient.getInstance();

    public WRenameButton(Text text, WItem itemIcon) {
        this.text = text;
        this.itemIcon = itemIcon;
        this.actualTexture = getActualTexture();
    }

    public WRenameButton(Text text) {
        this.text = text;
        this.actualTexture = getActualTexture();
    }

    public WRenameButton() {
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

    @Override
    public void setSize(int x, int y) {
        super.setSize(155, 32);
    }

    public void setIcon(ItemStack stack) {
        this.itemIcon = new WItem(stack);
    }

    public void setIcon(SpawnEggItem item) {
        if (item == null) {
            this.stacks.add(Items.PIG_SPAWN_EGG.getDefaultStack());
            return;
        }

        this.itemIcon = new WItem(item.getDefaultStack());
    }

    public WRenameButton setText(Text text) {
        this.text = text;
        return this;
    }

    public void setLore(List<String> lore) {
        this.lore = lore;
    }

    public void setStacks(List<ItemStack> stacks) {
        this.stacks = stacks;
    }

    public void addStack(ItemStack stack) {
        this.stacks.add(stack);
    }

    public List<ItemStack> getStacks() {
        return stacks;
    }

    public void setOnCtrlClick(@Nullable Runnable onClick) {
        this.onCtrlClick = onClick;
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

    public void setOnCtrlPress(Runnable onCtrlPress) {
        this.onCtrlPress = onCtrlPress;
    }

    @Override
    public void renderTooltip(MatrixStack matrices, int x, int y, int tX, int tY) {
        Painters painters = new Painters();

        MyTooltipBuilder builder = new MyTooltipBuilder();
        addTooltip(builder);

        painters.renderTextTooltip(matrices,builder.lines,stacks,x+tX-4,y+tY-4,mc.currentScreen,mc.textRenderer,mc.getItemRenderer(),shift);
    }

    public void addTooltip(TooltipBuilder builder) {
        builder.add(itemNameToolTip);

        if (shift) {

            builder.add(Text.of(""));
            builder.add(Text.translatable("better_anvil.rename_button.tooltip.resource_pack"));
            builder.add(resourcePackToolTip);

            if (!lore.isEmpty()) {
                builder.add(Text.of(""));
                builder.add(Text.translatable("better_anvil.rename_button.tooltip.lore"));
                for (String s : lore) {
                    builder.add(Text.of(s));
                }
            }

            if (!stacks.isEmpty()) {
                builder.add(Text.of(""));
                Text text = stacks.size() > 1 ? Text.translatable("better_anvil.rename_button.tooltip.items") : Text.translatable("better_anvil.rename_button.tooltip.item");
                builder.add(text);
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
            itemIcon.paint(matrices, x + 7, y + 7, mouseX, mouseY);
        }

        if (text != null) {
            int color = 0xE0E0E0;
            if (!LibGui.isDarkMode() && actualTexture != MY_BUTTON_RENAME) {
                color = 0x262626;
            }
            ScreenDrawing.drawString(matrices, text.asOrderedText(), HorizontalAlignment.LEFT, x + 31, y + 12, width, color);

        }
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

    private Identifier getActualTexture() {
        CITButtonTexture citButtonTexture = config.BUTTON_TEXTURE;
        if (citButtonTexture == CITButtonTexture.THEME) {
            return LibGui.isDarkMode() ? MY_BUTTON_DARK : MY_BUTTON;
        }
        if (citButtonTexture == CITButtonTexture.RENAME) {
            return MY_BUTTON_RENAME;
        }
        return MY_BUTTON;
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
            if(onCtrlPress != null) onCtrlPress.run();
        }
    }

}
