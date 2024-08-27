package group.phenikaa.hotelmanager.api.info.impl.rentable;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.utility.enums.RoomStatus;

public class Apartment extends AbstractRentable {
    protected Apartment(RoomStatus isAvailable, int roomNumber) {
        super(isAvailable, roomNumber);
    }
}
