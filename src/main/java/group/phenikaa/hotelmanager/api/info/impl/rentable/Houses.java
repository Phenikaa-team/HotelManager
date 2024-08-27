package group.phenikaa.hotelmanager.api.info.impl.rentable;

import group.phenikaa.hotelmanager.api.info.api.AbstractRentable;
import group.phenikaa.hotelmanager.api.utility.enums.RoomStatus;

public class Houses extends AbstractRentable {
    protected Houses(RoomStatus isAvailable, int roomNumber) {
        super(isAvailable, roomNumber);
    }
}
