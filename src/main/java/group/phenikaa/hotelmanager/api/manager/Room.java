package group.phenikaa.hotelmanager.api.manager;

import group.phenikaa.hotelmanager.api.utility.interfaces.IRoom;

public class Room implements IRoom {
    private boolean isAvailable;
    private int roomNum;

    public Room(boolean isAvailable, int roomNum) {
        this.isAvailable = isAvailable;
        this.roomNum = roomNum;
    }

    @Override
    public boolean isAvailable() {
        return false;
    }

    @Override
    public int roomNum() {
        return 0;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNum = roomNumber;
    }

    public void setAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }
}
