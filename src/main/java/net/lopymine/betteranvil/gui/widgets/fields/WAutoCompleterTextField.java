package net.lopymine.betteranvil.gui.widgets.fields;

import net.minecraft.text.Text;

import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;

import net.lopymine.betteranvil.gui.search.SearchTags;

import java.util.*;
import java.util.function.Consumer;

public class WAutoCompleterTextField extends WTextField {

    private final List<String> resourcePacks;
    private int i = 0;

    public WAutoCompleterTextField(LinkedHashSet<String> resourcePacks, Text suggestion) {
        setSuggestion(suggestion);
        this.resourcePacks = new LinkedList<>(resourcePacks);
    }

    @Override
    public InputResult onMouseScroll(int x, int y, double amount) {
        if (resourcePacks == null) {
            return InputResult.IGNORED;
        }
        if (resourcePacks.isEmpty()) {
            return InputResult.IGNORED;
        }

        int a = (int) amount;
        SearchTags tag = SearchTags.getTag(getText());
        if (tag == SearchTags.RESOURCE_PACK) {
            String alias = tag.getAlias(getText());

            if (a == -1) {
                i = (i - 1 + resourcePacks.size()) % resourcePacks.size();
            }
            if (a == 1) {
                i = (i + 1) % resourcePacks.size();
            }

            setText(alias + " " + resourcePacks.get(i));
            return InputResult.PROCESSED;
        }

        return super.onMouseScroll(x, y, amount);
    }

    @Override
    public WAutoCompleterTextField setMaxLength(int max) {
        super.setMaxLength(max);
        return this;
    }

    @Override
    public WAutoCompleterTextField setChangedListener(Consumer<String> listener) {
        super.setChangedListener(listener);
        return this;
    }
}
