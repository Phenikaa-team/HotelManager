package group.phenikaa.hotelmanager.api.info.impl.rentable;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;

public class Single extends AbstractRentable {

    public Single(RentableStatus isAvailable, long price, String id) {
        super("Single", isAvailable, price, id);
    }
}
