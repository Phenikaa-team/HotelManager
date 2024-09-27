package group.phenikaa.hotelmanager.api.utility.enums;

import group.phenikaa.hotelmanager.api.utility.interfaces.IStringProvider;

public enum RentableStatus implements IStringProvider {
    Available("Room is available for booking."),
    Occupied("Room is currently occupied."),
    Reserved("Room is reserved but no guest checked in yet."),
    UnderMaintenance("Room is under maintenance.");

    private final String description;

    RentableStatus(String description) {
        this.description = description;
    }

    @Override
    public String url() {
        return description;
    }

    @Override
    public String toString() {
        return this.name() + " - " + description;
    }
}
