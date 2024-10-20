package group.phenikaa.hotelmanager.api.info.impl.rentable;

import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
import group.phenikaa.hotelmanager.api.utility.enums.RentableType;

//TODO: extra options
public class Deluxe extends ConcreteRentable {

    public Deluxe(RentableStatus isAvailable, long price, String id) {
        super(RentableType.Deluxe, isAvailable, price, id);
    }
}
