package net.lopymine.betteranvil.resourcepacks;

public enum ResourcePackType {
    CIT("Custom Item Texture"),
    CMD("Custom Model Data"),
    CEM("Custom Entity Model");

    private final String fullName;

    ResourcePackType(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}
