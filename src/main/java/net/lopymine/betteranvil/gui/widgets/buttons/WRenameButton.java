package net.lopymine.betteranvil.gui.widgets.buttons;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.item.TooltipData;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.*;

import io.github.cottonmc.cotton.gui.client.*;
import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.*;

import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.config.BetterAnvilConfig;
import net.lopymine.betteranvil.config.enums.ButtonTextures;
import net.lopymine.betteranvil.gui.description.interfaces.IConfigAccessor;
import net.lopymine.betteranvil.gui.tooltip.ItemsTooltipComponent;
import net.lopymine.betteranvil.gui.tooltip.builder.TooltipBuilder;
import net.lopymine.betteranvil.gui.tooltip.builder.*;
import net.lopymine.betteranvil.gui.tooltip.utils.TooltipComponentsData;
import net.lopymine.betteranvil.config.resourcepacks.cit.metadata.CountMetaDataParser.CountMetaData;
import net.lopymine.betteranvil.config.resourcepacks.cit.metadata.DamageMetaDataParser.DamageMetaData;
import net.lopymine.betteranvil.config.resourcepacks.cit.metadata.EnchantmentLevelsMetaDataParser.EnchantmentLevelsMetaData;
import net.lopymine.betteranvil.utils.*;

import java.util.*;
import java.util.stream.Stream;
import org.jetbrains.annotations.*;

public class WRenameButton extends WWidget {

    public static final Identifier MY_BUTTON = new Identifier(BetterAnvil.MOD_ID, "gui/button/button.png");
    public static final Identifier MY_BUTTON_RENAME = new Identifier(BetterAnvil.MOD_ID, "gui/button/button_rename.png");
    public static final Identifier MY_BUTTON_DARK = new Identifier(BetterAnvil.MOD_ID, "gui/button/button_dark.png");
    public static final Identifier MY_BUTTON_FOCUS = new Identifier(BetterAnvil.MOD_ID, "gui/button/button_focus.png");

    public static final Formatting TOOLTIP_FORMATTING = Formatting.BLUE;

    private static boolean hasDownShift = false;
    private static boolean hasDownCtrl = false;

    @Nullable
    private final WItem icon;
    @Nullable
    private final Text text;
    @Nullable
    private final Runnable onClick;
    @Nullable
    private final Runnable onCtrlClick;
    @Nullable
    private final Runnable onCtrlDown;
    @NotNull
    private final Text tooltipTitle;
    @Nullable
    private final String resourcePack;
    @Nullable
    private final List<String> lore;
    @Nullable
    private final List<ItemStack> items;
    private final boolean showItemsInTooltip;
    @Nullable
    private final List<Enchantment> enchantments;
    @Nullable
    private final DamageMetaData damageMetaData;
    @Nullable
    private final CountMetaData countMetaData;
    @Nullable
    private final EnchantmentLevelsMetaData enchantmentLevels;

    public WRenameButton(@Nullable WItem icon, @NotNull String text, @Nullable Runnable onClick, @Nullable Runnable onCtrlClick, @Nullable Runnable onCtrlDown, @Nullable String resourcePack, @Nullable List<String> lore, @Nullable List<ItemStack> items, boolean showItemsInTooltip, @Nullable List<Enchantment> enchantments, @Nullable DamageMetaData damageMetaData, @Nullable CountMetaData countMetaData, @Nullable EnchantmentLevelsMetaData enchantmentLevels) {
        this.icon = icon;
        this.text = StringUtils.trimText(text, 100);
        this.onClick = onClick;
        this.onCtrlClick = onCtrlClick;
        this.onCtrlDown = onCtrlDown;
        this.tooltipTitle = Text.of(text);
        this.resourcePack = resourcePack;
        this.lore = lore;
        this.items = items;
        this.showItemsInTooltip = showItemsInTooltip;
        this.enchantments = enchantments;
        this.enchantmentLevels = enchantmentLevels;
        this.damageMetaData = damageMetaData;
        this.countMetaData = countMetaData;
    }

    public static Builder builder(String text) {
        return new Builder(text);
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

    @Override
    public void renderTooltip(DrawContext context, int x, int y, int tX, int tY) {
        boolean bl = Screen.hasShiftDown();

        List<Text> title = List.of(tooltipTitle);

        Optional<TooltipData> data = Optional.empty();

        if (bl) {
            MyTooltipBuilder builder = new MyTooltipBuilder();
            addTooltip(builder);

            List<TooltipComponent> components = new ArrayList<>(builder.lines.stream().map(Text::asOrderedText).map(TooltipComponent::of).toList());
            if (items != null && !items.isEmpty() && showItemsInTooltip) {
                components.add(new ItemsTooltipComponent(items, shouldRenderInDarkMode()));
            }

            data = Optional.of(new TooltipComponentsData(components));
        }

        context.drawTooltip(MinecraftClient.getInstance().textRenderer, title, data, x + tX, y + tY);
    }

    public void addTooltip(TooltipBuilder builder) {
        if (hasDownShift) {
            builder.add(Text.of(""));
            builder.add(Text.translatable("better_anvil.rename_button.tooltip.resource_pack"));
            builder.add(ScreenTexts.space().append(Text.literal(resourcePack).formatted(TOOLTIP_FORMATTING)));

            if (lore != null && !lore.isEmpty()) {
                builder.add(Text.of(""));
                builder.add(Text.translatable("better_anvil.rename_button.tooltip.lore"));
                for (String text : lore) {
                    builder.add(ScreenTexts.space().append(text));
                }
            }

            if (enchantments != null && !enchantments.isEmpty()) {
                builder.add(Text.of(""));
                Text text = enchantments.size() > 1 ? Text.translatable("better_anvil.rename_button.tooltip.enchantments") : Text.translatable("better_anvil.rename_button.tooltip.enchantment");
                builder.add(text);
                for (Enchantment enchantment : enchantments) {
                    int level = enchantment.getMaxLevel();
                    if(enchantmentLevels != null) {
                        level = enchantmentLevels.maxLevel();
                    }
                    builder.add(ScreenTexts.space().append(enchantment.getName(level).copy().formatted(TOOLTIP_FORMATTING)));
                }
            }

            if (damageMetaData != null) {
                builder.add(Text.of(""));
                builder.add(Text.translatable("better_anvil.rename_button.tooltip.damage"));
                float min = damageMetaData.minDamage();
                float max = damageMetaData.maxDamage();
                boolean bl = false;

                if (items != null) {
                    bl = items.size() == 1;
                }

                if (bl) {
                    ItemStack itemStack = items.get(0);
                    min = (int) (itemStack.getMaxDamage() * min);
                    max = (int) (itemStack.getMaxDamage() * max);
                }

                String damage = damageMetaData.hasMaxDamage() ? min + "-" + max : String.valueOf(min);
                builder.add(ScreenTexts.space().append(Text.literal(damage + (bl ? "" : "%")).formatted(TOOLTIP_FORMATTING)));
            }

            if (countMetaData != null) {
                builder.add(Text.of(""));
                builder.add(Text.translatable("better_anvil.rename_button.tooltip.count"));
                float min = countMetaData.minCount();
                float max = countMetaData.maxCount();
                boolean bl = false;

                if (items != null) {
                    bl = items.size() == 1;
                }

                if (bl) {
                    ItemStack itemStack = items.get(0);
                    min = (int) (itemStack.getMaxCount() * min);
                    max = (int) (itemStack.getMaxCount() * max);
                }

                String count = countMetaData.hasMaxCount() ? min + "-" + max : String.valueOf(min);
                builder.add(ScreenTexts.space().append(Text.literal(count + (bl ? "" : "%"))).formatted(TOOLTIP_FORMATTING));
            }

            if (items != null && !items.isEmpty() && showItemsInTooltip) {
                builder.add(Text.of(""));
                Text text = items.size() > 1 ? Text.translatable("better_anvil.rename_button.tooltip.items") : Text.translatable("better_anvil.rename_button.tooltip.item");
                builder.add(text);
            }
        }
    }

    @Override
    public void paint(DrawContext context, int x, int y, int mouseX, int mouseY) {
        ScreenDrawing.texturedRect(context, x, y, 155, 32, getActualTexture(), 0xFFFFFFFF);

        if (isHovered() || isFocused()) {
            ScreenDrawing.texturedRect(context, x, y, 155, 32, MY_BUTTON_FOCUS, 0xFFFFFFFF);
        }

        if (icon != null) {
            icon.paint(context, x + 7, y + 7, mouseX, mouseY);
        }

        if (text != null) {
            int color = 0xE0E0E0;
            if (!LibGui.isDarkMode() && getActualTexture() != MY_BUTTON_RENAME) {
                color = 0x262626;
            }

            ScreenDrawing.drawString(context, text.asOrderedText(), HorizontalAlignment.LEFT, x + 31, y + 12, width, color);
        }
    }

    @Override
    public InputResult onClick(int x, int y, int button) {
        MinecraftClient.getInstance().getSoundManager().play(PositionedSoundInstance.master(SoundEvents.UI_BUTTON_CLICK, 1.0F));

        if (hasDownCtrl && onCtrlClick != null) {
            onCtrlClick.run();
            return InputResult.PROCESSED;
        }

        if (onClick != null) {
            onClick.run();
        }

        return InputResult.PROCESSED;

    }

    private Identifier getActualTexture() {
        if (getHost() instanceof IConfigAccessor description) {
            ButtonTextures buttonTexture = description.getConfig().buttonTextureEnum;

            if (buttonTexture == ButtonTextures.THEME) {
                return description.getConfig().isDarkMode ? MY_BUTTON_DARK : MY_BUTTON;
            }

            if (buttonTexture == ButtonTextures.RENAME) {
                return MY_BUTTON_RENAME;
            }
        }

        return shouldRenderInDarkMode() ? MY_BUTTON_DARK : MY_BUTTON;
    }

    @Override
    public InputResult onKeyReleased(int ch, int key, int modifiers) {
        if (getHost() instanceof IConfigAccessor description) {
            BetterAnvilConfig config = description.getConfig();

            if (ch == config.shiftKey) {
                hasDownShift = false;
                return InputResult.PROCESSED;
            }
            if (ch == config.ctrlKey) {
                hasDownCtrl = false;
                return InputResult.PROCESSED;
            }
        }

        return InputResult.IGNORED;
    }

    @Override
    public InputResult onKeyPressed(int ch, int key, int modifiers) {
        if (getHost() instanceof IConfigAccessor description) {
            BetterAnvilConfig config = description.getConfig();

            hasDownShift = ch == config.shiftKey;
            hasDownCtrl = ch == config.ctrlKey;

            if (hasDownCtrl && onCtrlDown != null) {
                onCtrlDown.run();
            }

            return InputResult.PROCESSED;
        }

        return InputResult.IGNORED;
    }

    @Override
    public int getWidth() {
        return 155;
    }

    @Override
    public int getHeight() {
        return 32;
    }


    public static class Builder {
        @Nullable
        private WItem icon;
        @NotNull
        private final String string;

        @Nullable
        private DamageMetaData damageMetaData;
        @Nullable
        private CountMetaData countMetaData;
        @Nullable
        private EnchantmentLevelsMetaData enchantmentLevels;

        @Nullable
        private String resourcePack;
        @Nullable
        private List<String> lore;
        @Nullable
        private List<Enchantment> enchantments;
        @Nullable
        private List<ItemStack> items;
        private boolean showItemsInTooltip;

        @Nullable
        private Runnable onClick;
        @Nullable
        private Runnable onCtrlClick;
        @Nullable
        private Runnable onCtrlDown;

        public Builder(@NotNull String text) {
            this.string = text;
        }

        public Builder setIcon(WItem icon) {
            this.icon = icon;
            return this;
        }

        public Builder setDamageMetaData(DamageMetaData damageMetaData) {
            this.damageMetaData = damageMetaData;
            return this;
        }

        public Builder setCountMetaData(CountMetaData countMetaData) {
            this.countMetaData = countMetaData;
            return this;
        }

        public Builder setResourcePack(String resourcePack) {
            this.resourcePack = resourcePack;
            return this;
        }

        public Builder setLore(List<String> lore) {
            this.lore = lore;
            return this;
        }

        public Builder setEnchantments(List<String> enchantments) {
            if (enchantments != null) {
                this.enchantments = enchantments.stream().flatMap(s -> {
                    Enchantment enchantment = ItemUtils.getEnchantmentByName(s);
                    return Stream.ofNullable(enchantment);
                }).toList();
            }
            return this;
        }

        public Builder setEnchantmentLevels(EnchantmentLevelsMetaData enchantmentLevels) {
            this.enchantmentLevels = enchantmentLevels;
            return this;
        }

        public Builder setItems(List<ItemStack> items, boolean showInTooltip) {
            this.items = items;
            this.showItemsInTooltip = showInTooltip;
            return this;
        }

        public Builder setOnClick(Runnable onClick) {
            this.onClick = onClick;
            return this;
        }

        public Builder setOnCtrlClick(Runnable onCtrlClick) {
            this.onCtrlClick = onCtrlClick;
            return this;
        }

        public Builder setOnCtrlDown(Runnable onCtrlDown) {
            this.onCtrlDown = onCtrlDown;
            return this;
        }

        public WRenameButton build() {
            return new WRenameButton(icon, string, onClick, onCtrlClick, onCtrlDown, resourcePack, lore, items, showItemsInTooltip, enchantments, damageMetaData, countMetaData, enchantmentLevels);
        }
    }
}
