package group.phenikaa.hotelmanager.api.info.impl.rentable;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;

public class Rooms extends AbstractRentable {

    public Rooms(RentableStatus isAvailable, long price) {
        super("Rooms", isAvailable, price);
    }
}
