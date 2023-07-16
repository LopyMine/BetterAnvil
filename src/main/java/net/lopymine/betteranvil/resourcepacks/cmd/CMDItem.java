package net.lopymine.betteranvil.resourcepacks.cmd;

import java.util.Objects;

public class CMDItem {

    private String item;
    private String resourcePack;

    private String serverResourcePack;
    private int id;

    public CMDItem(String item, int id){
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

    public void setResourcePack(String resourcePack) {
        this.resourcePack = resourcePack;
    }

    public String getResourcePack() {
        return resourcePack;
    }

    public void setServerResourcePack(String serverResourcePack) {
        this.serverResourcePack = serverResourcePack;
    }

    public String getServerResourcePack() {
        return serverResourcePack;
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
