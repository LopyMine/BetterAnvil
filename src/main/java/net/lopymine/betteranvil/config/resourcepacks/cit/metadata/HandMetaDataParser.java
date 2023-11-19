package net.lopymine.betteranvil.config.resourcepacks.cit.metadata;

import net.minecraft.util.Hand;

import org.jetbrains.annotations.Nullable;

public class HandMetaDataParser {
    @Nullable
    public static Hand getHand(String hand) {
        if (hand.equalsIgnoreCase("off")) return Hand.OFF_HAND;
        if (hand.equalsIgnoreCase("main")) return Hand.MAIN_HAND;
        return null;
    }
}
