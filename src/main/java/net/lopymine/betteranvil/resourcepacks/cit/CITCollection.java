package net.lopymine.betteranvil.resourcepacks.cit;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;

public class CITCollection {
    private final LinkedHashSet<CITItem> citItemsCollection;

    public CITCollection(LinkedHashSet<CITItem> citItemsCollection) {
        this.citItemsCollection = citItemsCollection;
    }

    public LinkedHashSet<CITItem> getItems() {
        return citItemsCollection;
    }


    public void addCitItem(CITItem citItem){
        citItemsCollection.add(citItem);
    }
}
