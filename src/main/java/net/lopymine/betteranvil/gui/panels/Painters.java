package net.lopymine.betteranvil.gui.panels;

import com.google.common.collect.Lists;
import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.widget.WPlainPanel;
import net.lopymine.betteranvil.BetterAnvil;
import net.lopymine.betteranvil.gui.tooltip.FakeBundleTooltipComponent;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.tooltip.HoveredTooltipPositioner;
import net.minecraft.client.gui.tooltip.TooltipComponent;
import net.minecraft.client.gui.tooltip.TooltipPositioner;
import net.minecraft.client.render.*;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import org.joml.Matrix4f;
import org.joml.Vector2ic;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.createLightDarkVariants;
import static io.github.cottonmc.cotton.gui.client.BackgroundPainter.createNinePatch;

public class Painters {
    public static BackgroundPainter listPainter = createLightDarkVariants(
            createNinePatch(new Identifier(BetterAnvil.ID, "gui/background_painters/list_background.png")),
            createNinePatch(new Identifier(BetterAnvil.ID, "gui/background_painters/list_background_dark.png"))
    );
    public static BackgroundPainter themePainter = createLightDarkVariants(
            createNinePatch(new Identifier(BetterAnvil.ID, "gui/background_painters/panel_light.png")),
            createNinePatch(new Identifier(BetterAnvil.ID, "gui/background_painters/panel_dark.png"))
    );
    public static BackgroundPainter tooltip = createNinePatch(new Identifier(BetterAnvil.ID, "gui/background_painters/tooltip.png"));

    public void renderTextTooltip(MatrixStack matrices, List<Text> list, List<ItemStack> items, int x, int y, Screen screen, TextRenderer textRenderer, ItemRenderer itemRenderer, boolean showItems){
        this.renderOrderedTextTooltip(matrices, Lists.transform(list, Text::asOrderedText),items,x,y,screen,textRenderer,itemRenderer, showItems);
    }
    public void renderOrderedTextTooltip(MatrixStack matrices, List<OrderedText> lists, List<ItemStack> items, int x, int y, Screen screen, TextRenderer textRenderer, ItemRenderer itemRenderer, boolean showItems){
        List<TooltipComponent> components = new ArrayList<>(lists.stream().map(TooltipComponent::of).toList());
        if(showItems){
            DefaultedList<ItemStack> stacks = DefaultedList.of();
            stacks.addAll(items);
            components.add(new FakeBundleTooltipComponent(items));
        }
        this.renderTooltipFromComponents(matrices, components,x,y, HoveredTooltipPositioner.INSTANCE,screen,textRenderer,itemRenderer);
    }

    public void renderTooltipFromComponents(MatrixStack matrices, List<TooltipComponent> components, int x, int y, TooltipPositioner positioner, Screen screen, TextRenderer textRenderer, ItemRenderer itemRenderer) {
        if (!components.isEmpty()) {
            int i = 0;
            int j = components.size() == 1 ? -2 : 0;

            TooltipComponent tooltipComponent;
            for(Iterator<TooltipComponent> var8 = components.iterator(); var8.hasNext(); j += tooltipComponent.getHeight()) {
                tooltipComponent = (TooltipComponent)var8.next();
                int k = tooltipComponent.getWidth(textRenderer);
                if (k > i) {
                    i = k;
                }
            }
            i += 8;
            j += 8;

            Vector2ic vector2ic = positioner.getPosition(screen, x, y, i, j);
            int n = vector2ic.x();
            int o = vector2ic.y();
            matrices.push();
            Matrix4f matrix4f = matrices.peek().getPositionMatrix();
            VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(Tessellator.getInstance().getBuffer());
            matrices.translate(0.0F, 0.0F, 400.0F);
            int q = o;

            WPlainPanel panel = new WPlainPanel();
            panel.setSize(i,j);
            tooltip.paintBackground(matrices,n,o,panel);

            int r;
            TooltipComponent tooltipComponent2;
            for(r = 0; r < components.size(); ++r) {
                tooltipComponent2 = (TooltipComponent)components.get(r);
                tooltipComponent2.drawText(textRenderer, n+4, q+4, matrix4f, immediate);
                q += tooltipComponent2.getHeight() + (r == 0 ? 2 : 0);
            }

            immediate.draw();
            q = o;

            for(r = 0; r < components.size(); ++r) {
                tooltipComponent2 = (TooltipComponent)components.get(r);
                tooltipComponent2.drawItems(textRenderer, n+4, q+4, matrices, itemRenderer,500);
                q += tooltipComponent2.getHeight() + (r == 0 ? 2 : 0);
            }

            matrices.pop();
        }
    }
}
