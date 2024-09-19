package group.phenikaa.hotelmanager.api.info.impl.rentable;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;

public class Deluxe extends AbstractRentable {

    public Deluxe(RentableStatus isAvailable, long price, String id) {
        super("Single", isAvailable, price, id);
    }
}
