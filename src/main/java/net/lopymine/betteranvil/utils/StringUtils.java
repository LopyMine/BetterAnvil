package net.lopymine.betteranvil.utils;

import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

public class StringUtils {
    public static boolean c(String text, String anotherText) {
        return text.toLowerCase().replace("ё", "e").contains(anotherText.toLowerCase().replace("ё", "e"));
    }

    public static boolean s(String text, String anotherText) {
        return text.toLowerCase().replace("ё", "e").startsWith(anotherText.toLowerCase().replace("ё", "e"));
    }

    public static Text trimText(String text, int width) {
        String trimmedText = MinecraftClient.getInstance().textRenderer.trimToWidth(text, width);
        if (!text.equals(trimmedText)) trimmedText = trimmedText.trim() + "...";

        return Text.of(trimmedText);
    }
}
