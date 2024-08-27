package group.phenikaa.hotelmanager.api.info.api;

import group.phenikaa.hotelmanager.api.utility.enums.RoomStatus;
import group.phenikaa.hotelmanager.api.utility.interfaces.IRentable;

public abstract class AbstractRentable implements IRentable {
    protected RoomStatus isAvailable;
    protected int roomNumber;

    protected AbstractRentable(RoomStatus isAvailable, int roomNumber) {
        this.isAvailable = isAvailable;
        this.roomNumber = roomNumber;
    }

    @Override
    public RoomStatus roomStatus() {
        return isAvailable;
    }

    @Override
    public int roomNumber() {
        return roomNumber;
    }

    @Override
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    @Override
    public void setAvailable(RoomStatus isAvailable) {
        this.isAvailable = isAvailable;
    }
}
