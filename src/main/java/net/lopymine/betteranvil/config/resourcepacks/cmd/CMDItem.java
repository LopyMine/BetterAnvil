package net.lopymine.betteranvil.config.resourcepacks.cmd;

import java.util.Objects;
import org.jetbrains.annotations.Nullable;

public class CMDItem {

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

    public void setResourcePack(@Nullable String resourcePack) {
        this.resourcePack = resourcePack;
    }

    @Nullable
    public String getServerResourcePack() {
        return serverResourcePack;
    }

    public void setServerResourcePack(@Nullable String serverResourcePack) {
        this.serverResourcePack = serverResourcePack;
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
