package group.phenikaa.hotelmanager.api.info.impl.rentable;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;

public class Suite extends AbstractRentable {

    public Suite(RentableStatus isAvailable, long price, String id) {
        super("Suite", isAvailable, price, id);
    }
}
