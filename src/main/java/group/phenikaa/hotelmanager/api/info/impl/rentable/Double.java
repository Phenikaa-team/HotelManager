package group.phenikaa.hotelmanager.api.info.impl.rentable;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;

public class Double extends AbstractRentable {

    public Double(RentableStatus isAvailable, long price, String id) {
        super("Double", isAvailable, price, id);
    }
}
