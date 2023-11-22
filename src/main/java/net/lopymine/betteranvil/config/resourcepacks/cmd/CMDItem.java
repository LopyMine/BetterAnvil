package net.lopymine.betteranvil.config.resourcepacks.cmd;

import net.lopymine.betteranvil.resourcepacks.ResourcePackItem;

import java.util.Objects;
import org.jetbrains.annotations.Nullable;

public class CMDItem implements ResourcePackItem<CMDItem> {

    private final int id;
    private final String item;
    @Nullable
    private String resourcePack;
    @Nullable
    private String serverResourcePack;

    public CMDItem(String item, int id) {
        this.item = item;
        this.id = id;
        resourcePack = null;
    }

    public int getId() {
        return id;
    }

    public String getItem() {
        return item;
    }

    @Nullable
    public String getResourcePack() {
        return resourcePack;
    }

    public CMDItem setResourcePack(@Nullable String resourcePack) {
        this.resourcePack = resourcePack;
        return this;
    }

    @Nullable
    public String getServerResourcePack() {
        return serverResourcePack;
    }

    public CMDItem setServerResourcePack(@Nullable String serverResourcePack) {
        this.serverResourcePack = serverResourcePack;
        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CMDItem cmdItem = (CMDItem) o;
        return id == cmdItem.id && item.equals(cmdItem.item) && Objects.equals(resourcePack, cmdItem.resourcePack) && Objects.equals(serverResourcePack, cmdItem.serverResourcePack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(item, resourcePack, serverResourcePack, id);
    }
}
