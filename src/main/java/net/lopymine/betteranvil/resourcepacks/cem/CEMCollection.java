package net.lopymine.betteranvil.resourcepacks.cem;

import java.util.ArrayList;
import java.util.LinkedHashSet;

public class CEMCollection {
    private LinkedHashSet<CEMItem> cemItems;

    public CEMCollection(LinkedHashSet<CEMItem> c){
        this.cemItems = c;
    }

    public LinkedHashSet<CEMItem> getItems() {
        return cemItems;
    }

    public void setItems(LinkedHashSet<CEMItem> cemItems) {
        this.cemItems = cemItems;
    }
}
