package group.phenikaa.hotelmanager.api.info.impl.rentable;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;

public class PresidentialSuite extends AbstractRentable {

    public PresidentialSuite(RentableStatus isAvailable, long price, String id) {
        super("PresidentialSuite", isAvailable, price, id);
    }
}
