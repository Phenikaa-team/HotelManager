package group.phenikaa.hotelmanager.api.info.impl.renter;

import group.phenikaa.hotelmanager.api.info.api.AbstractRenter;

public class Individual extends AbstractRenter {
    private static Individual INSTANCE;

    public Individual(String name) {
        super("Individual", name);
    }

    public static Individual getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new Individual("Null");
        }
        return INSTANCE;
    }
}
