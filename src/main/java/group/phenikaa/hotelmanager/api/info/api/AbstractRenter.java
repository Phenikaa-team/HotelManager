package group.phenikaa.hotelmanager.api.info.api;

import group.phenikaa.hotelmanager.api.utility.interfaces.IRenter;

public abstract class AbstractRenter implements IRenter {
    protected String name;
    protected int roomNumber;

    protected AbstractRenter(String name, int roomNumber) {
        this.name = name;
        this.roomNumber = roomNumber;
    }

    @Override
    public String name() {
        return name;
    }

    @Override
    public int roomNumber() {
        return roomNumber;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }
}
