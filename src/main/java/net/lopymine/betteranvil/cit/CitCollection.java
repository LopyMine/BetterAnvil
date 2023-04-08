package net.lopymine.betteranvil.cit;

import java.util.Collection;

public class CitCollection {
    private final Collection<CitItems> citItemsCollection;


    public CitCollection(Collection<CitItems> citItemsCollection) {

        this.citItemsCollection = citItemsCollection;
    }

    public Collection<CitItems> getCitItemsCollection() {
        return citItemsCollection;
    }
}
