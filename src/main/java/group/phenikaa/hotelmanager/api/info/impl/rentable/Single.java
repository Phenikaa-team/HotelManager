package group.phenikaa.hotelmanager.api.info.impl.rentable;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.utility.enums.RentableStatus;
import group.phenikaa.hotelmanager.api.utility.enums.RentableType;

//TODO: what if user want rent it have more than 1 :lol:
public class Single extends ConcreteRentable {

    public Single(RentableStatus isAvailable, long price, String id) {
        super(RentableType.Single, isAvailable, price, id);
    }
}
