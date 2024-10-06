package group.phenikaa.hotelmanager.api.info.impl.rentable;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
import group.phenikaa.hotelmanager.api.utility.enums.RentableType;

//TODO: >= 2 can't be rent
public class Double extends AbstractRentable {

    public Double(RentableStatus isAvailable, long price, String id) {
        super(RentableType.Double, isAvailable, price, id);
    }
}
