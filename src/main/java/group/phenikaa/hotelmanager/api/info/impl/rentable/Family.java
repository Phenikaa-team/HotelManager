package group.phenikaa.hotelmanager.api.info.impl.rentable;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
import group.phenikaa.hotelmanager.api.utility.enums.RentableType;

public class Family extends AbstractRentable {

    public Family(RentableStatus isAvailable, long price, String id) {
        super(RentableType.Family, isAvailable, price, id);
    }
}
