package group.phenikaa.hotelmanager.api.info.impl.rentable;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
import group.phenikaa.hotelmanager.api.utility.enums.RentableType;

public class Single extends AbstractRentable {

    public Single(RentableStatus isAvailable, long price, String id) {
        super(RentableType.Single, isAvailable, price, id);
    }
}
