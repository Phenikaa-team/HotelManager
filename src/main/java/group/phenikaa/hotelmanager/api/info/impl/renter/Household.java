package group.phenikaa.hotelmanager.api.info.impl.renter;

import group.phenikaa.hotelmanager.api.info.api.AbstractRenter;
import group.phenikaa.hotelmanager.api.utility.enums.Country;

public class Household extends AbstractRenter {
    private int residentsNumber;
    private boolean haveKids;
    private Enum<Country> country;

    public Household(String name) {
        super("Household", name);
    }

}
