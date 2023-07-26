package net.lopymine.betteranvil.resourcepacks.cmd;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class CMDCollection {

    private LinkedHashSet<CMDItem> customModelDataItems;

    public CMDCollection(LinkedHashSet<CMDItem> c){
        this.customModelDataItems = c;
    }

    public LinkedHashSet<CMDItem> getItems() {
        return customModelDataItems;
    }

    public void setCustomModelDataItems(LinkedHashSet<CMDItem> customModelDataItems) {
        this.customModelDataItems = customModelDataItems;
    }
}
