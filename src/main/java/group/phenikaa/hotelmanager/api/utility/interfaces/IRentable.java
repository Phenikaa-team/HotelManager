package group.phenikaa.hotelmanager.api.utility.interfaces;

import group.phenikaa.hotelmanager.api.utility.enums.RoomStatus;

public interface IRentable {
    RoomStatus roomStatus();
    int roomNumber();
    void setRoomNumber(int roomNumber);
    void setAvailable(RoomStatus isAvailable);
}
