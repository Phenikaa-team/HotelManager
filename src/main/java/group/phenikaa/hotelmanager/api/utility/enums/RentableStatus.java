package group.phenikaa.hotelmanager.api.utility.enums;

public enum RentableStatus {
    Available("Room is available for booking."),
    Occupied("Room is currently occupied."),
    Reserved("Room is reserved but no guest checked in yet."),
    UnderMaintenance("Room is under maintenance.");

    private final String description;

    RentableStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    @Override
    public String toString() {
        return this.name() + " - " + description;
    }
}
