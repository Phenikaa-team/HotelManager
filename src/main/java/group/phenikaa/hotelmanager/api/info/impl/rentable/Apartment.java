package group.phenikaa.hotelmanager.api.info.impl.rentable;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;

public class Apartment extends AbstractRentable {

    public Apartment(RentableStatus isAvailable, long price) {
        super("Apartment", isAvailable, price);
    }
}
