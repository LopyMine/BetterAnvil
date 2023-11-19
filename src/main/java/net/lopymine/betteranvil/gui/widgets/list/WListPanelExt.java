package net.lopymine.betteranvil.gui.widgets.list;

import io.github.cottonmc.cotton.gui.widget.*;
import io.github.cottonmc.cotton.gui.widget.data.Axis;

import java.util.List;
import java.util.function.*;

public class WListPanelExt<D, W extends WWidget> extends WListPanel<D, W> {

    public WListPanelExt(List<D> data, Supplier<W> supplier, BiConsumer<D, W> configurator) {
        super(data, supplier, configurator);
        this.data = data;
        this.supplier = supplier;
        this.configurator = configurator;
        scrollBar = new WScrollBarExt(Axis.VERTICAL);
        scrollBar.setMaxValue(data.size());
        scrollBar.setParent(this);
    }
}
