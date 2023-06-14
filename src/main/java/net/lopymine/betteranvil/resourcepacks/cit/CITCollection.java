package net.lopymine.betteranvil.resourcepacks.cit;

import java.util.ArrayList;

public class CITCollection {
    private final ArrayList<CITItem> citItemsCollection;

    public CITCollection(ArrayList<CITItem> citItemsCollection) {
        this.citItemsCollection = citItemsCollection;
    }

    public ArrayList<CITItem> getItems() {
        return citItemsCollection;
    }


    public void addCitItem(CITItem citItem){
        citItemsCollection.add(citItem);
    }
}
