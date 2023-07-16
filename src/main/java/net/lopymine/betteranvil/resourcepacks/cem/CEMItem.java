package net.lopymine.betteranvil.resourcepacks.cem;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CEMItem {
    private String mob;
    private String name;
    private String resourcePack;
    private String serverResourcePack;

    public CEMItem(String mob, String name){
        this.mob = mob;
        this.name = name;
        resourcePack = null;
    }

    public String getName() {
        return name;
    }

    public List<String> getNames() {
        String[] names = name.split("\\|"); // split the string by "|" delimiter

        return Arrays.stream(names).toList();
    }

    public String getMob() {
        return mob;
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
        CEMItem item = (CEMItem) o;
        return mob.equals(item.mob) && name.equals(item.name) && Objects.equals(resourcePack, item.resourcePack) && Objects.equals(serverResourcePack, item.serverResourcePack);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mob, name, resourcePack, serverResourcePack);
    }
}
