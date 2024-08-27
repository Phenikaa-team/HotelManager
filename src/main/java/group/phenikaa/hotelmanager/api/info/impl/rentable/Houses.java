package group.phenikaa.hotelmanager.api.info.impl.rentable;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;

public class Houses extends AbstractRentable {

    public Houses(RentableStatus isAvailable, long price) {
        super("Houses", isAvailable, price);
    }
}
