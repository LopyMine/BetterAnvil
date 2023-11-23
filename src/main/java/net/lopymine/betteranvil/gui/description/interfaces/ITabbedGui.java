package net.lopymine.betteranvil.gui.description.interfaces;

import net.lopymine.betteranvil.gui.widgets.buttons.WTabButton;

import java.util.*;

public interface ITabbedGui<I> extends IGui<I> {

    void clearTabs();

    WTabButton createDefaultTab(LinkedHashSet<I> list);

    WTabButton createFavoriteTab(LinkedHashSet<I> list);

    void initTabs();

    void addTabs(List<WTabButton> tabsSub);

    void nextSwitch();

    void backSwitch();

    void initSwitchers();
}
