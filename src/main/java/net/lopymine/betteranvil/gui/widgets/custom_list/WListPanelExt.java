package net.lopymine.betteranvil.gui.widgets.custom_list;

import io.github.cottonmc.cotton.gui.widget.WListPanel;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import io.github.cottonmc.cotton.gui.widget.data.Axis;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class WListPanelExt<D,W extends WWidget> extends WListPanel<D, W> {

    public WListPanelExt(List data, Supplier supplier, BiConsumer configurator) {
        super(data, supplier, configurator);
        this.data = data;
        this.supplier = supplier;
        this.configurator = configurator;
        scrollBar = new WScrollBarExt(Axis.VERTICAL);
        scrollBar.setMaxValue(data.size());
        scrollBar.setParent(this);
    }
}
