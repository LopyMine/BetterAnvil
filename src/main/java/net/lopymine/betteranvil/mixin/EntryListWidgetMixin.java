package net.lopymine.betteranvil.mixin;

import net.minecraft.client.gui.widget.EntryListWidget;
import org.spongepowered.asm.mixin.*;

import net.lopymine.betteranvil.utils.mixins.EntryListWidgetAccessor;

@Mixin(EntryListWidget.class)
public abstract class EntryListWidgetMixin implements EntryListWidgetAccessor{

    @Shadow protected abstract int getRowTop(int index);

    @Override
    public int betterAnvil$getRowTop(int index) {
        return this.getRowTop(index);
    }
}
