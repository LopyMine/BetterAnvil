package net.lopymine.betteranvil.config.resourcepacks.cit;

import net.minecraft.util.Hand;

import net.lopymine.betteranvil.config.resourcepacks.cit.metadata.CountMetaDataParser.CountMetaData;
import net.lopymine.betteranvil.config.resourcepacks.cit.metadata.DamageMetaDataParser.DamageMetaData;
import net.lopymine.betteranvil.config.resourcepacks.cit.metadata.EnchantmentLevelsMetaDataParser.EnchantmentLevelsMetaData;

import java.util.*;
import org.jetbrains.annotations.*;

public class CITItem {
    private final String items;
    private final String customName;

    private final CountMetaData countMetaData;
    private final DamageMetaData damageMetaData;
    private final EnchantmentLevelsMetaData enchantmentLevels;
    private final List<String> enchantments;
    private final List<String> lore;
    private final Hand hand;

    private String resourcePack = null;
    private String serverResourcePack = null;

    private CITItem(String items, String customName, CountMetaData countMetaData, DamageMetaData damageMetaData, List<String> enchantments, EnchantmentLevelsMetaData enchantmentLevels, List<String> lore, Hand hand) {
        this.items = items;
        this.customName = customName;
        this.countMetaData = countMetaData;
        this.damageMetaData = damageMetaData;
        this.enchantmentLevels = enchantmentLevels;
        this.enchantments = enchantments;
        this.lore = lore;
        this.hand = hand;
    }

    @NotNull
    public LinkedHashSet<String> getCustomNames() {
        return new LinkedHashSet<>(Arrays.stream(customName.split("\\|")).toList());
    }

    @NotNull
    public String getCustomName() {
        return customName;
    }

    @NotNull
    public String getItem() {
        return items;
    }

    @NotNull
    public List<String> getItems() {
        return Arrays.stream(items.split(" ")).toList();
    }

    @Nullable
    public String getResourcePack() {
        return resourcePack;
    }

    public CITItem setResourcePack(String resourcePack) {
        this.resourcePack = resourcePack;
        return this;
    }

    @Nullable
    public String getServerResourcePack() {
        return serverResourcePack;
    }

    public CITItem setServerResourcePack(String serverResourcePack) {
        this.serverResourcePack = serverResourcePack;
        return this;
    }

    @Nullable
    public List<String> getLore() {
        return lore;
    }

    @Nullable
    public DamageMetaData getDamageMetaData() {
        return damageMetaData;
    }

    @Nullable
    public CountMetaData getCountMetaData() {
        return countMetaData;
    }

    @Nullable
    public EnchantmentLevelsMetaData getEnchantmentLevels() {
        return enchantmentLevels;
    }

    @Nullable
    public List<String> getEnchantments() {
        return enchantments;
    }

    @Nullable
    public Hand getHand() {
        return hand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CITItem item = (CITItem) o;
        return items.equals(item.items) && customName.equals(item.customName); // && Objects.equals(countMetaData, item.countMetaData) && Objects.equals(damageMetaData, item.damageMetaData) && Objects.equals(enchantments, item.enchantments) && Objects.equals(lore, item.lore);
    }

    @Override
    public int hashCode() {
        return Objects.hash(items, customName);//, countMetaData, damageMetaData, enchantments, lore);
    }

    public static class Builder {
        @Nullable
        private String items = null;
        @Nullable
        private String customName = null;
        @Nullable
        private CountMetaData countMetaData = null;
        @Nullable
        private DamageMetaData damage = null;
        @Nullable
        private EnchantmentLevelsMetaData enchantmentLevels = null;
        @Nullable
        private List<String> enchantments = null;
        @Nullable
        private List<String> lore = null;
        @Nullable
        private Hand hand = null;
        @Nullable
        private String resourcePack = null;
        @Nullable
        private String serverResourcePack = null;

        public Builder() {
        }

        public CITItem.Builder items(@Nullable String items) {
            this.items = items;
            return this;
        }

        public CITItem.Builder customName(@Nullable String customName) {
            this.customName = customName;
            return this;
        }

        public CITItem.Builder count(@Nullable CountMetaData countMetaData) {
            this.countMetaData = countMetaData;
            return this;
        }

        public CITItem.Builder damage(@Nullable DamageMetaData damage) {
            this.damage = damage;
            return this;
        }

        public CITItem.Builder enchantments(List<String> enchantments) {
            this.enchantments = enchantments;
            return this;
        }

        public CITItem.Builder enchantmentLevels(@Nullable EnchantmentLevelsMetaData enchantmentLevels) {
            this.enchantmentLevels = enchantmentLevels;
            return this;
        }

        public CITItem.Builder lore(@Nullable List<String> lore) {
            this.lore = lore;
            return this;
        }

        public CITItem.Builder resourcePack(@Nullable String resourcePack) {
            this.resourcePack = resourcePack;
            return this;
        }

        public CITItem.Builder serverResourcePack(@Nullable String serverResourcePack) {
            this.serverResourcePack = serverResourcePack;
            return this;
        }

        public CITItem.Builder hand(Hand hand) {
            this.hand = hand;
            return this;
        }

        @Nullable
        public CITItem build() {
            if (items == null || customName == null) return null;

            CITItem item = new CITItem(items, customName, countMetaData, damage, enchantments, enchantmentLevels, lore, hand);
            item.setResourcePack(resourcePack);
            item.setServerResourcePack(serverResourcePack);

            return item;
        }
    }
}
