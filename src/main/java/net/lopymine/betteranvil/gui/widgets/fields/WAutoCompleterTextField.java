package net.lopymine.betteranvil.gui.widgets.fields;

import net.minecraft.registry.Registries;
import net.minecraft.text.Text;

import io.github.cottonmc.cotton.gui.widget.WTextField;
import io.github.cottonmc.cotton.gui.widget.data.InputResult;

import net.lopymine.betteranvil.gui.search.SearchTags;

import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class WAutoCompleterTextField extends WTextField {

    private final AutoCompleter resourcePacks;
    private final AutoCompleter items;
    private final AutoCompleter enchantments;

    public WAutoCompleterTextField(LinkedHashSet<String> resourcePacks, Text suggestion) {
        setSuggestion(suggestion);
        this.resourcePacks = new AutoCompleter(new LinkedList<>(resourcePacks));
        this.items = new AutoCompleter(new LinkedList<>(Registries.ITEM.stream().flatMap(item -> Stream.of(item.getName().getString())).toList()));
        this.enchantments = new AutoCompleter(new LinkedList<>(Registries.ENCHANTMENT.stream().flatMap(enchantment -> Stream.of(Text.translatable(enchantment.getTranslationKey()).getString())).toList()));
    }

    @Override
    public InputResult onMouseScroll(int x, int y, double amount) {
        int a = (int) amount;
        SearchTags tag = SearchTags.getTag(getText());
        if (tag == null) {
            return InputResult.IGNORED;
        }

        String text = switch (tag) {
            case RESOURCE_PACK -> scrollAutoCompleter(a, resourcePacks);
            case ENCHANTMENTS -> scrollAutoCompleter(a, enchantments);
            case ITEM -> scrollAutoCompleter(a, items);
            default -> "";
        };

        if (text.isEmpty()) {
            return InputResult.IGNORED;
        }

        setText(tag.getAlias(getText()) + " " + text);
        return super.onMouseScroll(x, y, amount);
    }

    private String scrollAutoCompleter(int amount, AutoCompleter autoCompleter) {
        if (amount == -1) {
            autoCompleter.scroll = (autoCompleter.scroll - 1 + autoCompleter.list.size()) % autoCompleter.list.size();
        }
        if (amount == 1) {
            autoCompleter.scroll = (autoCompleter.scroll + 1) % autoCompleter.list.size();
        }
        return autoCompleter.list.get(autoCompleter.scroll);
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

    public static class AutoCompleter {
        private final List<String> list;
        private int scroll = 0;

        private AutoCompleter(List<String> list) {
            this.list = list;
        }
    }
}
