package group.phenikaa.hotelmanager.api.utility.enums;

import group.phenikaa.hotelmanager.api.utility.interfaces.IStringProvider;

public enum RentableType implements IStringProvider {
    Single("Single Room, suitable for 1 person"),
    Double("Double Room, suitable for 2 persons"),
    Suite("Suite Room, luxury with separate living space"),
    Deluxe("Deluxe Room, high-end amenities"),
    Family("Family Room, suitable for families"),
    PresidentialSuite("Presidential Suite, the most luxurious room");

    private final String description;

    RentableType(String description) {
        this.description = description;
    }

    @Override
    public String url() {
        return description;
    }

    @Override
    public String toString() {
        return name() + " - " + description;
    }
}

