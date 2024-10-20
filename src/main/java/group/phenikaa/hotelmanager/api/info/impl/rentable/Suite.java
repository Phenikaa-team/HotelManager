package group.phenikaa.hotelmanager.api.info.impl.rentable;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
import group.phenikaa.hotelmanager.api.utility.enums.RentableType;

//TODO: extra options
public class Suite extends ConcreteRentable {

    public Suite(RentableStatus isAvailable, long price, String id) {
        super(RentableType.Suite, isAvailable, price, id);
    }
}
