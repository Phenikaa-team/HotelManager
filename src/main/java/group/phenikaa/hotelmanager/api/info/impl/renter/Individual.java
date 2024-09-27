package group.phenikaa.hotelmanager.api.info.impl.renter;

import group.phenikaa.hotelmanager.api.info.api.AbstractRenter;
import group.phenikaa.hotelmanager.api.utility.enums.Country;

public class Individual extends AbstractRenter {
    private Enum<Country> country;

    public Individual(String name) {
        super("Individual", name);
    }
}
