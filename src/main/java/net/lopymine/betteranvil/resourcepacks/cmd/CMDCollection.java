package net.lopymine.betteranvil.resourcepacks.cmd;

import java.util.ArrayList;

public class CMDCollection {

    private ArrayList<CMDItem> customModelDataItems;

    public CMDCollection(ArrayList<CMDItem> c){
        this.customModelDataItems = c;
    }

    public ArrayList<CMDItem> getItems() {
        return customModelDataItems;
    }

    public void setCustomModelDataItems(ArrayList<CMDItem> customModelDataItems) {
        this.customModelDataItems = customModelDataItems;
    }
}
