package group.phenikaa.hotelmanager.api.utility.interfaces;

import group.phenikaa.hotelmanager.api.utility.enums.RoomStatus;

public interface IRoom {
    RoomStatus roomStatus();
    String roomNum();
}
