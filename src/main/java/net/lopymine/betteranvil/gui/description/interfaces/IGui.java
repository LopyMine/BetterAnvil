package net.lopymine.betteranvil.gui.description.interfaces;

import io.github.cottonmc.cotton.gui.widget.WPlainPanel;

import net.lopymine.betteranvil.gui.panels.WConfigPanel;

import java.util.LinkedHashSet;
import java.util.function.BiConsumer;

public interface IGui<I> {
    void createFavoriteListPanel(WPlainPanel root, LinkedHashSet<I> list, BiConsumer<I, WConfigPanel> consumer);

    void createMainListPanel(WPlainPanel root, LinkedHashSet<I> list, BiConsumer<I, WConfigPanel> consumer);
}
