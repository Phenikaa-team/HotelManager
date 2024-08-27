package group.phenikaa.hotelmanager.api.manager;

import group.phenikaa.hotelmanager.api.utility.enums.RoomStatus;
import group.phenikaa.hotelmanager.api.utility.interfaces.IRoom;

public class Room implements IRoom {
    public RoomStatus isAvailable;
    public String roomNum;

    public Room(RoomStatus isAvailable, String roomNum) {
        this.isAvailable = isAvailable;
        this.roomNum = roomNum;
    }

    @Override
    public RoomStatus roomStatus() {
        return isAvailable;
    }

    @Override
    public String roomNum() {
        return roomNum;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNum = roomNumber;
    }

    public void setAvailable(RoomStatus isAvailable) {
        this.isAvailable = isAvailable;
    }
}
