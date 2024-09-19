package group.phenikaa.hotelmanager.api.info.impl.rentable;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;

public class Family extends AbstractRentable {

    public Family(RentableStatus isAvailable, long price, String id) {
        super("Family", isAvailable, price, id);
    }
}
